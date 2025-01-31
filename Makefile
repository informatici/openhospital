##############################################################################
#
#  Makefile for building Open Hospital release packages.
#
#  -> To list the available targets:  make help
#
#  -> To build a specific version of Open Hospital, set the
#  following environment variables before running make:
#
#     OH_VERSION: x.x.x [Open Hospital version - e.g. 1.4.2]
# 
#  -> Example: to build development version of Open Hospital:
#     
#     make clean && export OH_VERSION=develop && make
# 
##############################################################################
SHELL := /bin/bash
.SHELLFLAGS := -eu -o pipefail -c
.ONESHELL: 			# single recipes are run in one bash session, instead of one per line
.DELETE_ON_ERROR:		# delete the target if its recipe failed
##############################################################################

# Open Hospital version
OH_VERSION ?= $(shell git describe --abbrev=0 --tags)
RELEASE_DATE := $(shell date +'%d/%m/%Y')

# software distribution
CLIENT := OpenHospital-$(OH_VERSION)-multiarch-client
WIN32 := OpenHospital-$(OH_VERSION)-windows_i686-portable
WIN64 := OpenHospital-$(OH_VERSION)-windows_x86_64-portable
LINUX32 := OpenHospital-$(OH_VERSION)-linux_i686-portable
LINUX64 := OpenHospital-$(OH_VERSION)-linux_x86_64-portable
FULLDISTRO := OpenHospital-$(OH_VERSION)-x86_64-EXPERIMENTAL

# JAVA and MySQL / MariaDB
# download url
JAVA_URL := https://cdn.azul.com/zulu/bin
MYSQL_URL := https://archive.mariadb.org

# software versions
JRE_32_VER := zulu17.48.15-ca-jre17.0.10
JRE_64_VER := zulu17.48.15-ca-jre17.0.10
MYSQL_WIN32_VER := 10.6.5
MYSQL_WIN64_VER := 10.6.16
MYSQL_LINUX32_VER := 10.5.23
MYSQL_LINUX64_VER := 10.6.16

# help file
TXTFILE := OH-readme.txt

# oh api jar/war
OH_API_JAR := openhospital-api-0.1.0.jar
OH_API_WAR := openhospital-api-0.1.0.war
OH_PUBLIC_URL := oh-ui

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
	@echo -e "\tclone-[core|gui|ui|api|doc]"
	@echo -e ""
	@echo -e "Compile targets:"
	@echo -e "\tcompile-[core|gui|ui|api|doc]"
	@echo -e ""
	@echo -e "Build (clone + compile) targets:"
	@echo -e "\tbuild-[core|gui|ui|api|doc]"
	@echo -e ""
	@echo -e "EXPERIMENTAL - Build full distro (full release core+gui+ui+api for Linux and Windows"
	@echo -e "\trelease-full-distro"
	@echo -e ""
	@echo -e "Documentation targets:"
	@echo -e "\tcompile-doc, admin-manual, user-manual, readme, release-notes, contributors"
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
	rm -rf OpenHospital-$(OH_VERSION)* RELEASE_NOTES RELEASE_NOTES.md CONTRIBUTORS* *.pdf
clean-all:
	git clean -xdff

####################################################################
# Clone targets
clone-all: clone-core clone-gui clone-ui clone-api clone-doc

####################################################################
# Compile targets
compile-all: compile-core compile-gui compile-ui compile-api compile-doc 

####################################################################
# Build targets
build-all: build-core build-ui build-api build-doc

####################################################################
# Build (clone + compile) targets
build-core: clone-core compile-core
build-gui: clone-gui compile-gui
build-ui: clone-ui compile-ui
build-api: clone-api build-core compile-api
build-doc: clone-doc compile-doc

####################################################################
# Assemble release targets
release-files: $(CLIENT).zip $(WIN32).zip $(WIN64).zip $(LINUX32).tar.gz $(LINUX64).tar.gz $(FULLDISTRO).zip release-contributors

# EXPERIMENTAL - release full distro
release-full-distro: build-core build-ui build-api build-gui build-doc $(FULLDISTRO).zip

####################################################################
# Clone repositories of OH components
clone-core:
	if [ -d "openhospital-core" ]; then cd openhospital-core; git checkout -B $(OH_VERSION); git pull;
	else
		git clone --depth=1 -b $(OH_VERSION) https://github.com/informatici/openhospital-core.git openhospital-core
	fi
clone-gui:
	if [ -d "openhospital-gui" ]; then cd openhospital-gui; git checkout -B $(OH_VERSION); git pull;
	else
		git clone --depth=1 -b tomcat_update https://github.com/mizzioisf/openhospital-gui.git openhospital-gui
	fi
clone-ui:
	if [ -d "openhospital-ui" ]; then cd openhospital-ui; git checkout -B $(OH_VERSION); git pull;
	else
		git clone --depth=1 -b tomcat_update https://github.com/mizzioisf/openhospital-ui.git openhospital-ui
	fi
clone-api:
	if [ -d "openhospital-api" ]; then cd openhospital-api; git checkout -B $(OH_VERSION); git pull;
	else
		git clone --depth=1 -b $(OH_VERSION) https://github.com/informatici/openhospital-api.git openhospital-api
	fi
clone-doc:
	if [ -d "openhospital-doc" ]; then cd openhospital-doc; git checkout -B $(OH_VERSION); git pull;
	else
		git clone --depth=1 -b $(OH_VERSION) https://github.com/informatici/openhospital-doc.git
	fi
####################################################################
# Compile application binaries
# OH Core
compile-core:
	pushd openhospital-core
	#git checkout $(OH_VERSION) -b $(OH_VERSION)
	mvn --quiet -T 1.5C install
	popd
# OH GUI
compile-gui:
	pushd openhospital-gui
	mvn --quiet -T 1.5C install
	popd
# Web UI
compile-ui:
	pushd openhospital-ui 
	# show npm version
	npm -v
	npm install
	#npm audit fix
	#npx update-browserslist-db@latest
	# build
	npm run build:prod
	popd
# Web API
compile-api:
	pushd openhospital-api
	# build API JAR - outdated
	# mvn --quiet -T 1.5C install
	# build API WAR for tomcat
	mvn --quiet -P war -T 1.5C install
	popd

####################################################################
# Generate documentation
compile-doc: admin-manual user-manual readme contributors release-notes
# manuals
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
# Generate contributors file
contributors:
	curl -s https://api.github.com/repos/informatici/openhospital-core/contributors?anon=0 | grep -e name -e login > ./CONTRIBUTORS-core.tmp  || echo "Error downloading core contributors";
	curl -s https://api.github.com/repos/informatici/openhospital-gui/contributors?anon=0 | grep -e name -e login > ./CONTRIBUTORS-gui.tmp  || echo "Error downloading gui contributors";
	curl -s https://api.github.com/repos/informatici/openhospital-ui/contributors?anon=0 | grep -e name -e login > ./CONTRIBUTORS-ui.tmp  || echo "Error downloading ui contributors";
	curl -s https://api.github.com/repos/informatici/openhospital-api/contributors?anon=0 | grep -e name -e login > ./CONTRIBUTORS-api.tmp  || echo "Error downloading api contributors";
	curl -s https://api.github.com/repos/informatici/openhospital-doc/contributors?anon=0 | grep -e name -e login > ./CONTRIBUTORS-doc.tmp  || echo "Error downloading doc contributors";
	# generate final file
	# # cat CONTRIBUTORS | sed -e s/^[^@]*//g
	# sed -e -e s/^.*\"name\"\:\ \"//g -e s/^.*\:\ \"/@/g -e s/\"\,//g -i CONTRIBUTORS.tmp # working alternative
	cat CONTRIBUTORS-*.tmp > CONTRIBUTORS.tmp
	sed -e s/^.*\"name\"\:\ \"//g -e s/^.*\"login\"\:\ \"/@/g -e s/\"\,//g CONTRIBUTORS.tmp
	cat ./CONTRIBUTORS.tmp | sort -u > CONTRIBUTORS
####################################################################
# Generate release notes file
release-notes:
	# add OH version
	pushd openhospital-core
	lasttag=$(shell git tag -l --sort=-v:refname | head -1)
	secondlasttag=$(shell git tag -l --sort=-v:refname | head -2 | tail -n 1)
	popd
	cp RELEASE_NOTES_TEMPLATE.md RELEASE_NOTES.md
	sed -i "s/VERSION/$(OH_VERSION)/g" RELEASE_NOTES.md
	sed -i "s/SECONDLASTTAG/$${secondlasttag//$$'\n'/\\n}/g" RELEASE_NOTES.md
	sed -i "s/LASTTAG/$${lasttag//$$'\n'/\\n}/g" RELEASE_NOTES.md
	sed -i "s|RELEASE_DATE|$(RELEASE_DATE)|g" RELEASE_NOTES.md
	head -6 RELEASE_NOTES.md > RELEASE_NOTES

####################################################################
# Generate final release notes file with cheksums and contributors
release-checksums: release-notes
	# add SHA256 checksum section
	echo "<details>" >> RELEASE_NOTES.md
	echo "<summary> SHA256 release packages checksums (click to expand) </summary>" >> RELEASE_NOTES.md
	echo "" >> RELEASE_NOTES.md
	echo "\`\`\`" >> RELEASE_NOTES.md
	# generate SHA256SUM
	sha256sum $(CLIENT).zip | tee -a "RELEASE_NOTES.md" 
	sha256sum $(WIN32).zip | tee -a "RELEASE_NOTES.md" 
	sha256sum $(WIN64).zip | tee -a "RELEASE_NOTES.md" 
	sha256sum $(LINUX32).tar.gz | tee -a "RELEASE_NOTES.md" 
	sha256sum $(LINUX64).tar.gz | tee -a "RELEASE_NOTES.md" 
	sha256sum $(FULLDISTRO).zip | tee -a "RELEASE_NOTES.md" 
	echo "\`\`\`" >> RELEASE_NOTES.md
	echo "</details>" >> RELEASE_NOTES.md
	echo "" >> RELEASE_NOTES.md
release-contributors: contributors release-notes release-checksums
	# add contributors section
	echo "<details>" >> RELEASE_NOTES.md
	echo "<summary> Contributors (click to expand) </summary>" >> RELEASE_NOTES.md
		cat CONTRIBUTORS >> RELEASE_NOTES.md
	echo "</details>" >> RELEASE_NOTES.md
	echo "" >> RELEASE_NOTES.md

####################################################################
# Create OH release packages

################
# Client package
$(CLIENT).zip: 
	# create directories and copy files
	mkdir -p $(CLIENT)/doc
	mkdir -p $(CLIENT)/oh
	cp CONTRIBUTORS LICENSE RELEASE_NOTES UPDATING $(CLIENT)
	cp -a ./oh-bundle/* $(CLIENT)/
	cp -a ./openhospital-gui/target/OpenHospital20/* $(CLIENT)/oh
	mv $(CLIENT)/oh/oh.* $(CLIENT)
	cp -a ./openhospital-core/sql $(CLIENT)/
	cp -f ./openhospital-gui/oh.ico $(CLIENT)/
	# remove unnecessary files
	rm -f $(CLIENT)/oh/README.md
	rm -f $(CLIENT)/ohmac.sh
	# copy manuals
	cp *.pdf $(CLIENT)/doc
	# Set oh folder
	sed -i 's/^\$$script\:OH_DIR\=\".\"/\$$script\:OH_DIR\=\"oh\"/g' $(CLIENT)/oh.ps1
	sed -i 's/set\ OH_DIR=\".\"/\set\ OH_DIR\=\"oh\"/g' $(CLIENT)/oh.bat
	sed -i 's/^\OH_DIR\=\".\"/OH_DIR\=\"oh\"/g' $(CLIENT)/oh.sh
	# Set OH mode to CLIENT in startup scripts
	sed -i 's/^\#$$script\:OH_MODE\=\"PORTABLE\"/\$$script\:OH_MODE\=\"CLIENT\"/g' $(CLIENT)/oh.ps1
	sed -i 's/^\#OH_MODE\=PORTABLE/OH_MODE\=CLIENT/g' $(CLIENT)/oh.sh
	# give exec permissions to startup script
	chmod 755 $(CLIENT)/oh.sh
	# create package
	zip -r -q $(CLIENT).zip $(CLIENT)

#######################
# Windows 32bit package
$(WIN32).zip:
	# create directories and copy files
	mkdir -p $(WIN32)/doc
	mkdir -p $(WIN32)/oh
	cp CONTRIBUTORS LICENSE RELEASE_NOTES UPDATING $(WIN32)
	cp -a ./oh-bundle/* $(WIN32)
	cp -a ./openhospital-gui/target/OpenHospital20/* $(WIN32)/oh
	mv $(WIN32)/oh/oh.* $(WIN32)
	cp -a ./openhospital-core/sql $(WIN32)/
	cp -f ./openhospital-gui/oh.ico $(WIN32)/
	# remove unnecessary files
	rm -f $(WIN32)/oh.sh
	rm -f $(WIN32)/ohmac.sh
	rm -f $(WIN32)/oh/README.md
	# copy manuals
	cp *.pdf $(WIN32)/doc
	# Set oh folder
	sed -i 's/^\$$script\:OH_DIR\=\".\"/\$$script\:OH_DIR\=\"oh\"/g' $(WIN32)/oh.ps1
	sed -i 's/set\ OH_DIR=\".\"/\set\ OH_DIR\=\"oh\"/g' $(WIN32)/oh.bat
	# download JAVA JRE
	wget -q -nc $(JAVA_URL)/$(JRE_WIN32)
	# download MariaDB / MySQL
	wget -q -nc $(MYSQL_URL)/mariadb-$(MYSQL_WIN32_VER)/win32-packages/$(MYSQL_WIN32)
	# unzip external software to be included in the package
	unzip -u -q $(JRE_WIN32) -d $(WIN32)
	unzip -u -q $(MYSQL_WIN32) -d $(WIN32)
	# create package
	zip -r -q $(WIN32).zip $(WIN32)

#######################
# Windows 64bit package
$(WIN64).zip:
	# create directories and copy files
	mkdir -p $(WIN64)/doc
	mkdir -p $(WIN64)/oh
	cp CONTRIBUTORS LICENSE RELEASE_NOTES UPDATING $(WIN64)
	cp -a ./oh-bundle/* $(WIN64)
	cp -a ./openhospital-gui/target/OpenHospital20/* $(WIN64)/oh
	mv $(WIN64)/oh/oh.* $(WIN64)
	cp -a ./openhospital-core/sql $(WIN64)/
	cp -f ./openhospital-gui/oh.ico $(WIN64)/
	# remove unnecessary files
	rm -f $(WIN64)/oh.sh
	rm -f $(WIN64)/ohmac.sh
	rm -f $(WIN64)/oh/README.md
	# copy manuals
	cp *.pdf $(WIN64)/doc
	# Set new root folder
	sed -i 's/^\$$script\:OH_DIR\=\".\"/\$$script\:OH_DIR\=\"oh\"/g' $(WIN64)/oh.ps1
	sed -i 's/set\ OH_DIR=\".\"/\set\ OH_DIR\=\"oh\"/g' $(WIN64)/oh.bat
	# download JAVA JRE
	wget -q -nc $(JAVA_URL)/$(JRE_WIN64)
	# download MariaDB / MySQL
	wget -q -nc $(MYSQL_URL)/mariadb-$(MYSQL_WIN64_VER)/winx64-packages/$(MYSQL_WIN64)
	# unzip external software to be included in the package
	unzip -u -q $(JRE_WIN64) -d $(WIN64)
	unzip -u -q $(MYSQL_WIN64) -d $(WIN64)
	# create package
	zip -r -q $(WIN64).zip $(WIN64)

#######################
# Linux 32bit package
$(LINUX32).tar.gz:
	# create directories and copy files
	mkdir -p $(LINUX32)/doc
	mkdir -p $(LINUX32)/oh
	cp CONTRIBUTORS LICENSE RELEASE_NOTES UPDATING $(LINUX32)
	cp -a ./oh-bundle/* $(LINUX32)
	cp -a ./openhospital-gui/target/OpenHospital20/* $(LINUX32)/oh
	mv $(LINUX32)/oh/oh.* $(LINUX32)
	cp -a ./openhospital-core/sql $(LINUX32)/
	cp -f ./openhospital-gui/oh.ico $(LINUX32)/
	# remove unnecessary files
	rm -f $(LINUX32)/oh.bat
	rm -f $(LINUX32)/oh.ps1
	rm -f $(LINUX32)/ohmac.sh
	rm -f $(LINUX32)/oh/README.md
	# copy manuals
	cp *.pdf $(LINUX32)/doc
	# Set oh folder
	sed -i 's/^\OH_DIR\=\".\"/OH_DIR\=\"oh\"/g' $(LINUX32)/oh.sh
	# give exec permissions to startup script
	chmod 755 $(LINUX32)/oh.sh
	# download JAVA JRE
	wget -q -nc $(JAVA_URL)/$(JRE_LINUX32)
	# download MariaDB / MySQL
	wget -q -nc $(MYSQL_URL)/mariadb-$(MYSQL_LINUX32_VER)/bintar-linux-x86/$(MYSQL_LINUX32)
	# unzip external software to be included in the package
	tar xz -C $(LINUX32) -f $(JRE_LINUX32)
	tar xz -C $(LINUX32) -f $(MYSQL_LINUX32)
	# create package
	tar -czf $(LINUX32).tar.gz $(LINUX32)

#######################
# Linux 64bit package
$(LINUX64).tar.gz:
	# create directories and copy files
	mkdir -p $(LINUX64)/doc
	mkdir -p $(LINUX64)/oh
	cp CONTRIBUTORS LICENSE RELEASE_NOTES UPDATING $(LINUX64)
	cp -a ./oh-bundle/* $(LINUX64)
	cp -a ./openhospital-gui/target/OpenHospital20/* $(LINUX64)/oh
	mv $(LINUX64)/oh/oh.* $(LINUX64)
	cp -a ./openhospital-core/sql $(LINUX64)/
	cp -f ./openhospital-gui/oh.ico $(LINUX64)/
	# remove unnecessary files
	rm -f $(LINUX64)/oh.bat
	rm -f $(LINUX64)/oh.ps1
	rm -f $(LINUX64)/ohmac.sh
	rm -f $(LINUX64)/oh/README.md
	# copy manuals
	cp *.pdf $(LINUX64)/doc
	# Set oh folder
	sed -i 's/^\OH_DIR\=\".\"/OH_DIR\=\"oh\"/g' $(LINUX64)/oh.sh
	# give exec permissions to startup script
	chmod 755 $(LINUX64)/oh.sh
	# download JAVA JRE
	wget -q -nc $(JAVA_URL)/$(JRE_LINUX64)
	# download MariaDB / MySQL
	wget -q -nc $(MYSQL_URL)/mariadb-$(MYSQL_LINUX64_VER)/bintar-linux-systemd-x86_64/$(MYSQL_LINUX64)
	# unzip external software to be included in the package
	tar xz -C $(LINUX64) -f $(JRE_LINUX64)
	tar xz -C $(LINUX64) -f $(MYSQL_LINUX64)
	# create package
	tar -czf $(LINUX64).tar.gz $(LINUX64)

####################################################################
#
# EXPERIMENTAL - full distro with UI+API server (Tomcat)
#
####################################################################
$(FULLDISTRO).zip:
	# create directories and copy files
	mkdir -p $(FULLDISTRO)/doc
	mkdir -p $(FULLDISTRO)/oh
	cp CONTRIBUTORS LICENSE RELEASE_NOTES UPDATING $(FULLDISTRO)
	cp -a ./oh-bundle/* $(FULLDISTRO)
	cp -a ./openhospital-gui/target/OpenHospital20/* $(FULLDISTRO)/oh
	mv $(FULLDISTRO)/oh/oh.* $(FULLDISTRO)
	mv $(FULLDISTRO)/oh/ohmac.* $(FULLDISTRO)
	cp -a ./openhospital-core/sql $(FULLDISTRO)/
	cp -f ./openhospital-gui/oh.ico $(FULLDISTRO)/
	# copy EXTRA files
	cp ./oh-extra/oh-api-readme.txt $(FULLDISTRO)/
	# copy manuals
	cp *.pdf $(FULLDISTRO)/doc
	# Set oh folder
	sed -i 's/^\$$script\:OH_DIR\=\".\"/\$$script\:OH_DIR\=\"oh\"/g' $(FULLDISTRO)/oh.ps1
	sed -i 's/set\ OH_DIR=\".\"/\set\ OH_DIR\=\"oh\"/g' $(FULLDISTRO)/oh.bat
	sed -i 's/^\OH_DIR\=\".\"/OH_DIR\=\"oh\"/g' $(FULLDISTRO)/oh.sh
	sed -i 's/^\OH_DIR\=\".\"/OH_DIR\=\"oh\"/g' $(FULLDISTRO)/ohmac.sh
	# give exec permissions to startup script
	chmod 755 $(FULLDISTRO)/oh.sh
	# copy API war
	cp -a ./openhospital-api/target/$(OH_API_WAR) $(FULLDISTRO)/oh/bin
	# copy API configuration file
	cp ./openhospital-api/rsc/application.properties.dist $(FULLDISTRO)/oh/rsc/
	# copy generated API WAR file
	cp -a ./openhospital-api/target/$(OH_API_WAR) $(FULLDISTRO)/oh/bin
	# copy UI content
	mkdir -p $(FULLDISTRO)/oh/$(OH_PUBLIC_URL)
	cp -a ./openhospital-ui/build/* $(FULLDISTRO)/oh/$(OH_PUBLIC_URL)
	# Set OH mode to EXPERIMENTAL in startup scripts
	# EXPERT_MODE="off"
	sed -i 's/^\#$$script\:EXPERT_MODE\=\"off\"/\$$script\:EXPERT_MODE\=\"on\"/g' $(CLIENT)/oh.ps1
	sed -i 's/^\#EXPERT_MODE\=off/EXPERT_MODE\=on/g' $(CLIENT)/oh.sh
	# create package
	zip -r -q $(FULLDISTRO).zip $(FULLDISTRO)
####################################################################
