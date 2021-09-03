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
WIN32 := OpenHospital-$(OH_VERSION)-windows_i686-portable
WIN64 := OpenHospital-$(OH_VERSION)-windows_x86_64-portable
LINUX32 := OpenHospital-$(OH_VERSION)-linux_i686-portable
LINUX64 := OpenHospital-$(OH_VERSION)-linux_x86_64-portable
JRE_WIN32 := jre-win32.zip
JRE_WIN64 := jre-win64.zip
JRE_LINUX32 := jre-linux32.tar.gz
JRE_LINUX64 := jre-linux64.tar.gz
MYSQL_WIN32 := mysql-win32.zip
MYSQL_WIN64 := mysql-win64.zip
MYSQL_LINUX32 := mysql-linux32.tar.gz
MYSQL_LINUX64 := mysql-linux64.tar.gz
MYSQL_VERSION := 10.2.40

.PHONY: clone-all clean clean-downloads dw-all dw-jre-all dw-mysql-all compile-all docs-all

all: compile-all dw-all release-files

help:
	@echo -e "Main make targets available:\n\tall (default), clean, clone-all, dw-all, compile-all, docs-all, dw-jre-all, dw-mysql-all, oh-admin-manual.pdf, oh-user-manual.pdf, core, gui, doc"
	@echo -e "\t$(WIN32).zip, $(WIN64).zip $(LINUX32).tar.gz, $(LINUX64).tar.gz, $(CLIENT).zip"
	@echo -e "\t$(JRE_WIN32), $(JRE_WIN64), $(JRE_LINUX32), $(JRE_LINUX64), $(MYSQL_WIN32), $(MYSQL_WIN64) $(MYSQL_LINUX32), $(MYSQL_LINUX64)"

# Clean targets
clean: clean-downloads
	rm -rf release-files openhospital-core openhospital-gui openhospital-doc poh-linux* poh-win* CHANGELOG CHANGELOG.md *.pdf
clean-all:
	git clean -xdff
clean-downloads:
	rm -rf *.zip *.tar.gz

compile-all: gui/target/OpenHospital20/bin/OH-gui.jar docs-all CHANGELOG 

# Assemble targets
release-files: $(CLIENT).zip $(WIN32).zip $(WIN64).zip $(LINUX32).tar.gz $(LINUX64).tar.gz
	echo "Checksum:" >> CHANGELOG.md
	echo "\`\`\`" >> CHANGELOG.md
	sha256sum $(CLIENT).zip >> CHANGELOG.md
	sha256sum $(WIN32).zip >> CHANGELOG.md
	sha256sum $(WIN64).zip >> CHANGELOG.md
	sha256sum $(LINUX32).tar.gz >> CHANGELOG.md
	sha256sum $(LINUX64).tar.gz >> CHANGELOG.md
	echo "\`\`\`" >> CHANGELOG.md
	mkdir -p release-files
	mv $(CLIENT).zip $(WIN32).zip $(WIN64).zip $(LINUX32).tar.gz $(LINUX64).tar.gz release-files/
	ls release-files

$(CLIENT).zip: compile-all
	mkdir -p $(CLIENT)/doc
	mkdir -p $(CLIENT)/oh
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(CLIENT)/oh
	cp -rf ./openhospital-core/sql $(CLIENT)/
	cp -f ./openhospital-gui/oh.ico $(CLIENT)/
	rm -rf $(CLIENT)/generate_changelog.sh
	rm -rf $(CLIENT)/oh/README.md
	cp OH-README.md LICENSE CHANGELOG $(CLIENT)
	cp *.pdf $(CLIENT)/doc
	zip -r $(CLIENT).zip $(CLIENT)

$(WIN32).zip: compile-all dw-all
	mkdir -p $(WIN32)/doc
	cp -rf ./poh-bundle-win/* $(WIN32)
	unzip $(JRE_WIN32) -d $(WIN32)
	unzip $(MYSQL_WIN32) -d $(WIN32) -x "*/lib/*"
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(WIN32)/oh
	cp -a ./openhospital-core/sql $(WIN32)/
	cp -f ./openhospital-gui/oh.ico $(WIN32)/
	rm -rf $(WIN32)/oh/generate_changelog.sh
	cp OH-README.md OH-win-changelog.md LICENSE CHANGELOG $(WIN32)
	# Workaround to force JAVA to 32bit to have DICOM working
	sed -i '/script:JAVA_ARCH=32/s/^#//g' $(WIN32)/oh.ps1
	cp *.pdf $(WIN32)/doc
	zip -r $(WIN32).zip $(WIN32)

$(WIN64).zip: compile-all dw-all
	mkdir -p $(WIN64)/doc
	cp -rf ./poh-bundle-win/* $(WIN64)
	unzip $(JRE_WIN64) -d $(WIN64)
	unzip $(MYSQL_WIN64) -d $(WIN64) -x "*/lib/*"
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(WIN64)/oh
	cp -a ./openhospital-core/sql $(WIN64)/
	cp -f ./openhospital-gui/oh.ico $(WIN64)/
	rm -rf $(WIN64)/oh/generate_changelog.sh
	cp OH-README.md OH-win-changelog.md LICENSE CHANGELOG $(WIN64)
	cp *.pdf $(WIN64)/doc
	zip -r $(WIN64).zip $(WIN64)

$(LINUX32).tar.gz: compile-all dw-all
	mkdir -p $(LINUX32)/doc
	cp -rf ./poh-bundle-linux-x32/* $(LINUX32)
	tar xz -C $(LINUX32) -f $(JRE_LINUX32)
	tar xz -C $(LINUX32) -f $(MYSQL_LINUX32) --exclude="*/lib/*"
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(LINUX32)/oh
	cp -a ./openhospital-core/sql $(LINUX32)/
	cp -f ./openhospital-gui/oh.ico $(LINUX32)/
	rm -rf $(LINUX32)/oh/generate_changelog.sh
	cp OH-README.md OH-linux-changelog.md LICENSE CHANGELOG $(LINUX32)
	cp *.pdf $(LINUX32)/doc
	tar -cvzf $(LINUX32).tar.gz $(LINUX32)

$(LINUX64).tar.gz: compile-all dw-all
	mkdir -p $(LINUX64)/doc
	cp -rf ./poh-bundle-linux-x64/* $(LINUX64)
	tar xz -C $(LINUX64) -f $(JRE_LINUX64)
	tar xz -C $(LINUX64) -f $(MYSQL_LINUX64) --exclude="*/lib/*"
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(LINUX64)/oh
	cp -a ./openhospital-core/sql $(LINUX64)/
	cp -f ./openhospital-gui/oh.ico $(LINUX64)/
	rm -rf $(LINUX64)/oh/generate_changelog.sh
	cp OH-README.md OH-linux-changelog.md LICENSE CHANGELOG $(LINUX64)
	cp *.pdf $(LINUX64)/doc
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
docs-all: doc oh-admin-manual.pdf oh-user-manual.pdf
oh-admin-manual.pdf: openhospital-doc
	asciidoctor-pdf ./openhospital-doc/doc_admin/AdminManual.adoc -a allow-uri-read -o AdminManual.pdf -v
oh-user-manual.pdf: openhospital-doc
	asciidoctor-pdf ./openhospital-doc/doc_user/UserManual.adoc -o UserManual.pdf -v

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
dw-jre-all: $(JRE_LINUX32) $(JRE_LINUX64) $(JRE_WIN32) $(JRE_WIN64)
dw-mysql-all: $(MYSQL_LINUX32) $(MYSQL_LINUX64) $(MYSQL_WIN32) $(MYSQL_WIN64)
$(JRE_LINUX32):
	wget -q -nc https://cdn.azul.com/zulu/bin/zulu11.50.19-ca-jre11.0.12-linux_i686.tar.gz -O $(JRE_LINUX32)
$(JRE_LINUX64):
	wget -q -nc https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/OpenJDK11U-jre_x64_linux_hotspot_11.0.11_9.tar.gz -O $(JRE_LINUX64)
$(JRE_WIN32):
	wget -q -nc https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u292-b10/OpenJDK8U-jre_x86-32_windows_hotspot_8u292b10.zip -O $(JRE_WIN32)
$(JRE_WIN64):
	wget -q -nc https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/OpenJDK11U-jre_x86-32_windows_hotspot_11.0.11_9.zip -O $(JRE_WIN64)
$(MYSQL_LINUX32):
	wget -q -nc https://downloads.mariadb.com/MariaDB/mariadb-$(MYSQL_VERSION)/bintar-linux-x86/mariadb-$(MYSQL_VERSION)-linux-i686.tar.gz -O $(MYSQL_LINUX32)
$(MYSQL_LINUX64):
	wget -q -nc https://downloads.mariadb.com/MariaDB/mariadb-$(MYSQL_VERSION)/bintar-linux-x86_64/mariadb-$(MYSQL_VERSION)-linux-x86_64.tar.gz -O $(MYSQL_LINUX64)
$(MYSQL_WIN32):
	wget -q -nc https://downloads.mariadb.org/f/mariadb-$(MYSQL_VERSION)/win32-packages/mariadb-$(MYSQL_VERSION)-win32.zip -O $(MYSQL_WIN32)
$(MYSQL_WIN64):
	wget -q -nc https://downloads.mariadb.com/MariaDB/mariadb-$(MYSQL_VERSION)/winx64-packages/mariadb-$(MYSQL_VERSION)-winx64.zip -O $(MYSQL_WIN64)
