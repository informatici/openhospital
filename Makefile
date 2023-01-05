##############################################################################
#
#  Makefile for building Open Hospital releases.
#  To list the available targets issue: make help
#  The following environment variables can be set before running make:
#  -> OH_VERSION: Open Hospital version
# 
##############################################################################
SHELL := /bin/bash
.SHELLFLAGS := -eu -o pipefail -c
.ONESHELL: 			# single recipes are run in one bash session, instead of one per line
.DELETE_ON_ERROR:		# delete the target if its recipe failed
##############################################################################

# Open Hospital version
OH_VERSION ?= $(shell git describe --abbrev=0 --tags)

# software distribution
CLIENT := OpenHospital-$(OH_VERSION)-multiarch-client
WIN32 := OpenHospital-$(OH_VERSION)-windows_i686-portable
WIN64 := OpenHospital-$(OH_VERSION)-windows_x86_64-portable
LINUX32 := OpenHospital-$(OH_VERSION)-linux_i686-portable
LINUX64 := OpenHospital-$(OH_VERSION)-linux_x86_64-portable

# JAVA and MySQL / MariaDB
# download url
JAVA_URL := https://cdn.azul.com/zulu/bin
#MYSQL_URL := https://downloads.mariadb.com/MariaDB/
MYSQL_URL := https://archive.mariadb.org

# software versions
JRE_32_VER := zulu11.60.19-ca-fx-jre11.0.17
JRE_64_VER := zulu11.60.19-ca-fx-jre11.0.17
MYSQL_WIN32_VER := 10.6.5
MYSQL_WIN64_VER := 10.6.11
MYSQL_LINUX32_VER := 10.5.18
MYSQL_LINUX64_VER := 10.6.11

# help file
TXTFILE := OH-readme.txt

# internal variables
JRE_WIN32 := $(JRE_32_VER)-win_i686.zip
JRE_WIN64 := $(JRE_64_VER)-win_x64.zip
JRE_LINUX32 := $(JRE_32_VER)-linux_i686.tar.gz
JRE_LINUX64 := $(JRE_64_VER)-linux_x64.tar.gz
MYSQL_WIN32 := mariadb-$(MYSQL_WIN32_VER)-win32.zip
MYSQL_WIN64 := mariadb-$(MYSQL_WIN64_VER)-winx64.zip
MYSQL_LINUX32 := mariadb-$(MYSQL_LINUX32_VER)-linux-i686.tar.gz
MYSQL_LINUX64 := mariadb-$(MYSQL_LINUX64_VER)-linux-systemd-x86_64.tar.gz

####################################################################
# targets
.PHONY: clean clean-releases clone-all compile-all build-all

####################################################################
# default target -> all
all: clone-all compile-all release-files

####################################################################
help:
	@echo -e "----------------------------"
	@echo -e "--->  Usage: make [target]"
	@echo -e ""
	@echo -e "Main targets available:"
	@echo -e "\tall (default), clean, clone-all, compile-all, build-all, release-files"
	@echo -e ""
	@echo -e "Clone targets:"
	@echo -e "\tclone-core, clone-gui, clone-ui, clone-api, clone-doc"
	@echo -e ""
	@echo -e "Compile targets:"
	@echo -e "\tcompile-core, compile-gui, compile-ui, compile-api"
	@echo -e ""
	@echo -e "Build (clone + compile) targets:"
	@echo -e "\tbuild-core, build-gui, build-ui, build-api"
	@echo -e ""
	@echo -e "Documentation targets:"
	@echo -e "\tcompile-doc, admin-manual, user-manual, readme"
	@echo -e ""
	@echo -e "OH release-files targets:"
	@echo -e "\t$(CLIENT).zip"
	@echo -e "\t$(WIN32).zip"
	@echo -e "\t$(WIN64).zip"
	@echo -e "\t$(LINUX32).tar.gz"
	@echo -e "\t$(LINUX64).tar.gz"
	@echo -e ""

####################################################################
# Clean targets
clean: clean-repos clean-downloads clean-releases
clean-repos:
	rm -rf openhospital-core openhospital-gui openhospital-ui openhospital-api openhospital-doc
clean-downloads:
	rm -rf zulu*.zip zulu*.tar.gz mariadb*.zip mariadb*.tar.gz
clean-releases:
	rm -rf OpenHospital-$(OH_VERSION)-* CHANGELOG.* *.pdf
clean-all:
	git clean -xdff

####################################################################
# Clone targets
clone-all: clone-core clone-gui clone-ui clone-api clone-doc

####################################################################
# Compile targets
compile-all: compile-core compile-ui compile-api compile-doc 

####################################################################
# Build targets
build-all: build-core build-ui build-api

####################################################################
# Clone repositories of OH components
clone-core:
	git clone --depth=1 -b $(OH_VERSION) https://github.com/informatici/openhospital-core.git openhospital-core
clone-gui:
	git clone --depth=1 -b $(OH_VERSION) https://github.com/informatici/openhospital-gui.git openhospital-gui
clone-ui:
	# git clone --depth=1 -b $(OH_VERSION) https://github.com/informatici/openhospital-ui.git openhospital-ui
	git clone --depth=1 https://github.com/informatici/openhospital-ui.git openhospital-ui
clone-api:
	git clone --depth=1 https://github.com/informatici/openhospital-api.git openhospital-api
clone-doc:
	git clone --depth=1 -b $(OH_VERSION) https://github.com/informatici/openhospital-doc.git
####################################################################
# Compile application binaries

# Java Core
compile-core:
	# pushd openhospital-core
	mvn --quiet -T 1.5C install
	# popd

# Java GUI
compile-gui:
	# pushd openhospital-gui
	mvn --quiet -T 1.5C package
	# popd

# Web UI
compile-ui:
	pushd openhospital-ui
	npm install -f
	popd

# Web API
compile-api:
	pushd openhospital-api
	mvn -T 1.5C package
	popd

####################################################################
# Build (clone + compile) targets

build-core: clone-core compile-core
build-gui: clone-gui build-core
build-ui: clone-ui compile-ui
build-api: build-core clone-api compile-api

####################################################################
# Generate documentation
compile-doc: admin-manual user-manual readme 
admin-manual: 
	asciidoctor-pdf ./openhospital-doc/doc_admin/AdminManual.adoc -a allow-uri-read -o AdminManual.pdf -v
user-manual:
	asciidoctor-pdf ./openhospital-doc/doc_user/UserManual.adoc -o UserManual.pdf -v
readme: 
	pushd oh-bundle
	echo "OH - Open Hospital Client | Portable " > $(TXTFILE)
	tail -n+2 OH-README.md >> $(TXTFILE)
	sed /\`\`/d -i $(TXTFILE)
	popd

####################################################################
# Generate version changelog
CHANGELOG: 
	pushd openhospital-core
	lasttag=$(shell git tag -l --sort=-v:refname | head -1)
	secondlasttag=$(shell git tag -l --sort=-v:refname | head -2 | tail -n 1)
	popd
	cp CHANGELOG_TEMPLATE.md CHANGELOG.md
	sed -i "s/VERSION/$(OH_VERSION)/g" CHANGELOG.md
	sed -i "s/SECONDLASTTAG/$${secondlasttag//$$'\n'/\\n}/g" CHANGELOG.md
	sed -i "s/LASTTAG/$${lasttag//$$'\n'/\\n}/g" CHANGELOG.md

####################################################################
# Assemble targets
release-files: $(CLIENT).zip $(WIN32).zip $(WIN64).zip $(LINUX32).tar.gz $(LINUX64).tar.gz CHANGELOG
	# generate changelog 
	echo "SHA256 Checksum:" >> CHANGELOG.md
	echo "" >> CHANGELOG.md
	echo "\`\`\`" >> CHANGELOG.md
	# generate SHA256SUM
	sha256sum $(CLIENT).zip | tee -a "CHANGELOG.md" 
	sha256sum $(WIN32).zip | tee -a "CHANGELOG.md" 
	sha256sum $(WIN64).zip | tee -a "CHANGELOG.md" 
	sha256sum $(LINUX32).tar.gz | tee -a "CHANGELOG.md" 
	sha256sum $(LINUX64).tar.gz | tee -a "CHANGELOG.md" 
	echo "\`\`\`" >> CHANGELOG.md
	# mkdir -p release-files
	# mv /$(CLIENT).zip $(WIN32).zip $(WIN64).zip $(LINUX32).tar.gz $(LINUX64).tar.gz release-files/

####################################################################
# Create OH release packages

$(CLIENT).zip: 
	# create directories and copy files
	mkdir -p $(CLIENT)/doc
	mkdir -p $(CLIENT)/oh
	cp LICENSE $(CLIENT)
	cp -rf ./oh-bundle/* $(CLIENT)/
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(CLIENT)/oh
	mv $(CLIENT)/oh/oh.* $(CLIENT)
	cp -rf ./openhospital-core/sql $(CLIENT)/
	cp -f ./openhospital-gui/oh.ico $(CLIENT)/
	# Set oh folder
	sed -i 's/^\$$script\:OH_DIR\=\".\"/\$$script\:OH_DIR\=\"oh\"/g' $(CLIENT)/oh.ps1
	sed -i 's/set\ OH_DIR=\".\"/\set\ OH_DIR\=\"oh\"/g' $(CLIENT)/oh.bat
	sed -i 's/^\OH_DIR\=\".\"/OH_DIR\=\"oh\"/g' $(CLIENT)/oh.sh
	# Set client mode in startup scripts
	sed -i 's/^\#$$script\:OH_MODE\=\"PORTABLE\"/\$$script\:OH_MODE\=\"CLIENT\"/g' $(CLIENT)/oh.ps1
	sed -i 's/^\#OH_MODE\=PORTABLE/OH_MODE\=CLIENT/g' $(CLIENT)/oh.sh
	# give exec permissions to startup script
	chmod 755 $(CLIENT)/oh.sh
	# copy manuals
	cp *.pdf $(CLIENT)/doc
	# create package
	zip -r -q $(CLIENT).zip $(CLIENT)

$(WIN32).zip:
	# create directories and copy files
	mkdir -p $(WIN32)/doc
	mkdir -p $(WIN32)/oh
	cp LICENSE $(WIN32)
	cp -rf ./oh-bundle/* $(WIN32)
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(WIN32)/oh
	mv $(WIN32)/oh/oh.* $(WIN32)
	cp -a ./openhospital-core/sql $(WIN32)/
	cp -f ./openhospital-gui/oh.ico $(WIN32)/
	# remove unnecessary files
	rm -f $(WIN32)/OH-linux-changelog.md
	rm -f $(WIN32)/oh.sh
	# Set oh folder
	sed -i 's/^\$$script\:OH_DIR\=\".\"/\$$script\:OH_DIR\=\"oh\"/g' $(WIN32)/oh.ps1
	sed -i 's/set\ OH_DIR=\".\"/\set\ OH_DIR\=\"oh\"/g' $(WIN32)/oh.bat
	# copy manuals
	cp *.pdf $(WIN32)/doc
	# download JAVA JRE
	wget -q -nc $(JAVA_URL)/$(JRE_WIN32)
	# download MariaDB / MySQL
	wget -q -nc $(MYSQL_URL)/mariadb-$(MYSQL_WIN32_VER)/win32-packages/$(MYSQL_WIN32)
	# create package
	unzip -u $(JRE_WIN32) -d $(WIN32)
	unzip -u $(MYSQL_WIN32) -d $(WIN32)
	zip -r -q $(WIN32).zip $(WIN32)

$(WIN64).zip:
	# create directories and copy files
	mkdir -p $(WIN64)/doc
	mkdir -p $(WIN64)/oh
	cp LICENSE $(WIN64)
	cp -rf ./oh-bundle/* $(WIN64)
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(WIN64)/oh
	mv $(WIN64)/oh/oh.* $(WIN64)
	cp -a ./openhospital-core/sql $(WIN64)/
	cp -f ./openhospital-gui/oh.ico $(WIN64)/
	# remove unnecessary files
	rm -f $(WIN64)/OH-linux-changelog.md
	rm -f $(WIN64)/oh.sh
	# Set new root folder
	sed -i 's/^\$$script\:OH_DIR\=\".\"/\$$script\:OH_DIR\=\"oh\"/g' $(WIN64)/oh.ps1
	sed -i 's/set\ OH_DIR=\".\"/\set\ OH_DIR\=\"oh\"/g' $(WIN64)/oh.bat
	# copy manuals
	cp *.pdf $(WIN64)/doc
	# download JAVA JRE
	wget -q -nc $(JAVA_URL)/$(JRE_WIN64)
	# download MariaDB / MySQL
	wget -q -nc $(MYSQL_URL)/mariadb-$(MYSQL_WIN64_VER)/winx64-packages/$(MYSQL_WIN64)
	# create package
	unzip -u $(JRE_WIN64) -d $(WIN64)
	unzip -u $(MYSQL_WIN64) -d $(WIN64)
	zip -r -q $(WIN64).zip $(WIN64)

$(LINUX32).tar.gz:
	# create directories and copy files
	mkdir -p $(LINUX32)/doc
	mkdir -p $(LINUX32)/oh
	cp LICENSE $(LINUX32)
	cp -rf ./oh-bundle/* $(LINUX32)
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(LINUX32)/oh
	mv $(LINUX32)/oh/oh.* $(LINUX32)
	cp -a ./openhospital-core/sql $(LINUX32)/
	cp -f ./openhospital-gui/oh.ico $(LINUX32)/
	# remove unnecessary files
	rm -f $(LINUX32)/OH-win-changelog.md
	rm -f $(LINUX32)/oh.bat
	rm -f $(LINUX32)/oh.ps1
	# Set oh folder
	sed -i 's/^\OH_DIR\=\".\"/OH_DIR\=\"oh\"/g' $(LINUX32)/oh.sh
	# give exec permissions to startup script
	chmod 755 $(LINUX32)/oh.sh
	# copy manuals
	cp *.pdf $(LINUX32)/doc
	# download JAVA JRE
	wget -q -nc $(JAVA_URL)/$(JRE_LINUX32)
	# download MariaDB / MySQL
	wget -q -nc $(MYSQL_URL)/mariadb-$(MYSQL_LINUX32_VER)/bintar-linux-x86/$(MYSQL_LINUX32)
	# create package
	tar xz -C $(LINUX32) -f $(JRE_LINUX32)
	tar xz -C $(LINUX32) -f $(MYSQL_LINUX32)
	tar -czf $(LINUX32).tar.gz $(LINUX32)

$(LINUX64).tar.gz:
	# create directories and copy files
	mkdir -p $(LINUX64)/doc
	mkdir -p $(LINUX64)/oh
	cp LICENSE $(LINUX64)
	cp -rf ./oh-bundle/* $(LINUX64)
	cp -rf ./openhospital-gui/target/OpenHospital20/* $(LINUX64)/oh
	mv $(LINUX64)/oh/oh.* $(LINUX64)
	cp -a ./openhospital-core/sql $(LINUX64)/
	cp -f ./openhospital-gui/oh.ico $(LINUX64)/
	# remove unnecessary files
	rm -f $(LINUX64)/OH-win-changelog.md
	rm -f $(LINUX64)/oh.bat
	rm -f $(LINUX64)/oh.ps1
	# Set oh folder
	sed -i 's/^\OH_DIR\=\".\"/OH_DIR\=\"oh\"/g' $(LINUX64)/oh.sh
	# give exec permissions to startup script
	chmod 755 $(LINUX64)/oh.sh
	# copy manuals
	cp *.pdf $(LINUX64)/doc
	# download JAVA JRE
	wget -q -nc $(JAVA_URL)/$(JRE_LINUX64)
	# download MariaDB / MySQL
	wget -q -nc $(MYSQL_URL)/mariadb-$(MYSQL_LINUX64_VER)/bintar-linux-systemd-x86_64/$(MYSQL_LINUX64)
	# create package
	tar xz -C $(LINUX64) -f $(JRE_LINUX64)
	tar xz -C $(LINUX64) -f $(MYSQL_LINUX64)
	tar -czf $(LINUX64).tar.gz $(LINUX64)

