##############################################################################
#
#  Makefile for building Open Hospital releases.
#  To list the available targets issue: make help
#  The following environment variables can be set before running make:
#    OH_VERSION: Open Hospital version
# 
##############################################################################
SHELL := /bin/bash
.SHELLFLAGS := -eu -o pipefail -c
.ONESHELL: 			# single recipes are run in one bash session, instead of one per line
.DELETE_ON_ERROR:		# delete the target if its recipe failed
OH_VERSION ?= $(shell git describe --abbrev=0 --tags)
CLIENT := OpenHospital-$(OH_VERSION)-multiarch-client
WIN32 := OpenHospital-$(OH_VERSION)-win32-portable
LINUX32 := OpenHospital-$(OH_VERSION)-linux_i686-portable
LINUX64 := OpenHospital-$(OH_VERSION)-linux_x86_64-portable
JRE_WIN := jre-win.zip
JRE_LINUX32 := jre-linux32.tar.gz
JRE_LINUX64 := jre-linux64.tar.gz
MYSQL_WIN := mysql-win.zip
MYSQL_LINUX32 := mysql-linux32.tar.gz
MYSQL_LINUX64 := mysql-linux64.tar.gz

.PHONY: clone-all clean clean-downloads dw-all dw-jre-all dw-mysql-all compile-all docs-all

all: compile-all dw-all release-files

help:
	@echo -e "Main make targets available:\n\tall (default), clean, clone-all, dw-all, compile-all, docs-all, dw-jre-all, dw-mysql-all, oh-admin-manual.pdf, oh-user-manual.pdf, core, gui, doc"
	@echo -e "\t$(WIN32).zip, $(LINUX32).tar.gz, $(LINUX64).tar.gz, $(CLIENT).zip"
	@echo -e "\t$(JRE_WIN), $(JRE_LINUX32), $(JRE_LINUX64), $(MYSQL_WIN), $(MYSQL_LINUX32), $(MYSQL_LINUX64)"

# Clean targets
clean: clean-downloads
	rm -rf release-files openhospital-core openhospital-gui openhospital-doc poh-linux* poh-win* CHANGELOG CHANGELOG.md *.pdf
clean-all:
	git clean -xdff
clean-downloads:
	rm -rf *.zip *.tar.gz

compile-all: gui/target/OpenHospital20/bin/OH-gui.jar docs-all CHANGELOG 

# Assemble targets
release-files: $(CLIENT).zip $(WIN32).zip $(LINUX32).tar.gz $(LINUX64).tar.gz
	echo "Checksum:" >> CHANGELOG.md
	echo "\`\`\`" >> CHANGELOG.md
	sha256sum $(CLIENT).zip >> CHANGELOG.md
	sha256sum $(WIN32).zip >> CHANGELOG.md
	sha256sum $(LINUX32).tar.gz >> CHANGELOG.md
	sha256sum $(LINUX64).tar.gz >> CHANGELOG.md
	echo "\`\`\`" >> CHANGELOG.md
	mkdir -p release-files
	mv $(CLIENT).zip $(WIN32).zip $(LINUX32).tar.gz $(LINUX64).tar.gz release-files/
	ls release-files

$(CLIENT).zip: compile-all
	mkdir -p $(CLIENT)/doc
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(CLIENT)
	cp -rf ./openhospital-core/sql $(CLIENT)/
	rm -rf $(CLIENT)/generate_changelog.sh
	cp LICENSE $(CLIENT)
	cp CHANGELOG $(CLIENT)
	cp *.pdf $(CLIENT)/doc
	zip -r $(CLIENT).zip $(CLIENT)

$(WIN32).zip: compile-all dw-all
	mkdir -p $(WIN32)/oh/doc
	cp -rf ./poh-bundle-win/* $(WIN32)
	unzip $(JRE_WIN) -d $(WIN32)
	unzip $(MYSQL_WIN) -d $(WIN32) -x "*/lib/*"
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(WIN32)/oh
	cp -a ./openhospital-core/sql $(WIN32)/
	rm -rf $(WIN32)/oh/generate_changelog.sh
	cp POH-README.md POH-win-changelog.md LICENSE CHANGELOG $(WIN32)
	cp *.pdf $(WIN32)/oh/doc
	zip -r $(WIN32).zip $(WIN32)

$(LINUX32).tar.gz: compile-all dw-all
	mkdir -p $(LINUX32)/oh/doc
	cp -rf ./poh-bundle-linux-x32/* $(LINUX32)
	tar xz -C $(LINUX32) -f $(JRE_LINUX32)
	tar xz -C $(LINUX32) -f $(MYSQL_LINUX32) --exclude="*/lib/*"
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(LINUX32)/oh
	cp -a ./openhospital-core/sql $(LINUX32)/
	rm -rf $(LINUX32)/oh/generate_changelog.sh
	cp POH-README.md POH-linux-changelog.md LICENSE CHANGELOG $(LINUX32)
	cp *.pdf $(LINUX32)/oh/doc
	tar -cvzf $(LINUX32).tar.gz $(LINUX32)

$(LINUX64).tar.gz: compile-all dw-all
	mkdir -p $(LINUX64)/oh/doc
	cp -rf ./poh-bundle-linux-x64/* $(LINUX64)
	tar xz -C $(LINUX64) -f $(JRE_LINUX64)
	tar xz -C $(LINUX64) -f $(MYSQL_LINUX64) --exclude="*/lib/*"
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(LINUX64)/oh
	cp -a ./openhospital-core/sql $(LINUX64)/
	rm -rf $(LINUX64)/oh/generate_changelog.sh
	cp POH-README.md POH-linux-changelog.md LICENSE CHANGELOG $(LINUX64)
	cp *.pdf $(LINUX64)/oh/doc
	tar -cvzf $(LINUX64).tar.gz $(LINUX64)

# Compile application binaries
gui/target/OpenHospital20/bin/OH-gui.jar: clone-all
	mvn -T 1.5C package

# Clone repositories of OH components
clone-all: core gui doc
core:
	git clone -b $(OH_VERSION) https://github.com/informatici/openhospital-core.git openhospital-core
gui:
	git clone -b $(OH_VERSION) https://github.com/informatici/openhospital-gui.git openhospital-gui
doc:
	git clone -b $(OH_VERSION) https://github.com/informatici/openhospital-doc.git openhospital-doc

# Compile documentation
docs-all: openhospital-doc oh-admin-manual.pdf oh-user-manual.pdf
oh-admin-manual.pdf: openhospital-doc
	asciidoctor-pdf ./openhospital-doc/doc_admin/AdminManual.adoc -o oh-admin-manual.pdf
oh-user-manual.pdf: openhospital-doc
	asciidoctor-pdf ./openhospital-doc/doc_user/UserManual.adoc -o oh-user-manual.pdf

# Create changelog file
CHANGELOG: core
	pushd openhospital-core
	lasttag=$(shell git tag -l --sort=-v:refname | head -1)
	secondlasttag=$(shell git tag -l --sort=-v:refname | head -2 | tail -n 1)
	popd
	cp CHANGELOG_TEMPLATE.md CHANGELOG.md
	sed -i "s/VERSION/$(OH_VERSION)/g" CHANGELOG.md
	sed -i "s/SECONDLASTTAG/$${secondlasttag//$$'\n'/\\n}/g" CHANGELOG.md
	sed -i "s/LASTTAG/$${lasttag//$$'\n'/\\n}/g" CHANGELOG.md
	cp CHANGELOG.md CHANGELOG

# Download JRE and MySQL
dw-all: dw-jre-all dw-mysql-all
dw-jre-all: $(JRE_LINUX32) $(JRE_LINUX64) $(JRE_WIN)
dw-mysql-all: $(MYSQL_LINUX32) $(MYSQL_LINUX64) $(MYSQL_WIN)
$(JRE_LINUX32):
	wget -q -nc https://cdn.azul.com/zulu/bin/zulu11.45.27-ca-jre11.0.10-linux_i686.tar.gz -O $(JRE_LINUX32)
$(JRE_LINUX64):
	wget -q -nc https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/OpenJDK11U-jre_x64_linux_hotspot_11.0.11_9.tar.gz -O $(JRE_LINUX64)
$(JRE_WIN):
	wget -q -nc https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/OpenJDK11U-jre_x86-32_windows_hotspot_11.0.11_9.zip -O $(JRE_WIN)
$(MYSQL_LINUX32):
	wget -q -nc https://downloads.mariadb.com/MariaDB/mariadb-10.2.39/bintar-linux-x86/mariadb-10.2.39-linux-i686.tar.gz -O $(MYSQL_LINUX32)
$(MYSQL_LINUX64):
	wget -q -nc https://downloads.mariadb.com/MariaDB/mariadb-10.2.39/bintar-linux-x86_64/mariadb-10.2.39-linux-x86_64.tar.gz -O $(MYSQL_LINUX64)
$(MYSQL_WIN):
	wget -q -nc https://downloads.mariadb.com/MariaDB/mariadb-10.2.39/winx64-packages/mariadb-10.2.39-winx64.zip -O $(MYSQL_WIN)
