SHELL = /bin/bash
OH_VERSION ?= $(shell git describe --abbrev=0 --tags)
POH_VERSION ?= 1.0
FULL = openhospital-$(OH_VERSION)
WIN = poh-win32-$(POH_VERSION)-core-$(OH_VERSION)
LINUX32 = poh-linux-x32-$(POH_VERSION)-core-$(OH_VERSION)
LINUX64 = poh-linux-x64-$(POH_VERSION)-core-$(OH_VERSION)

.PHONY: build clone-all clean clean-downloads dw-all dw-jre-all dw-mysql-all compile compile-all docs-all

all: compile-all dw-all assemble-all

all: compile-all dw-all release-files

# Clean targets
clean:
	git clean -xdff
clean-downloads:
	rm -rf *.zip *.tar.gz

compile-all: gui/target/OpenHospital20/bin/OH-gui.jar docs-all CHANGELOG database.sql

# Assemble targets
release-files: $(FULL).zip $(WIN).zip $(LINUX32).tar.gz $(LINUX64).tar.gz
	checksum="$(shell sha256sum $(FULL).zip $(WIN).zip $(LINUX32).tar.gz $(LINUX64).tar.gz)"; \
	checksum=$${checksum//$$'\n'/\\n}; \
	sed -i "s/CHECKSUM/$$checksum/g" CHANGELOG.md
	mkdir -p release-files
	mv $(FULL).zip $(WIN).zip $(LINUX32).tar.gz $(LINUX64).tar.gz release-files/
	ls release-files

$(FULL).zip: compile-all
	mkdir -p $(FULL)/doc $(FULL)/mysql
	cp -rf ./gui/target/OpenHospital20/* $(FULL)
	cp -rf ./core/mysql/db/* $(FULL)/mysql
	rm -rf $(FULL)/generate_changelog.sh
	cp LICENSE $(FULL)
	cp CHANGELOG $(FULL)
	cp *.pdf $(FULL)/doc
	zip -r $(FULL).zip $(FULL)

$(WIN).zip: compile-all dw-all
	mkdir -p $(WIN)/oh/doc
	cp -rf ./poh-bundle-win/* $(WIN)
	unzip -f jre-win.zip -d $(WIN)
	unzip -f mysql-win.zip -d $(WIN) -x "*/lib/*"
	cp -rf ./gui/target/OpenHospital20/* $(WIN)/oh
	rm -rf $(WIN)/oh/generate_changelog.sh
	cp *.sql POH-README.md POH-win-changelog.md LICENSE CHANGELOG $(WIN)
	cp *.pdf $(WIN)/oh/doc
	zip -r $(WIN).zip $(WIN)

$(LINUX32).tar.gz: compile-all dw-all
	mkdir -p $(LINUX32)/oh/doc
	cp -rf ./poh-bundle-linux-x32/* $(LINUX32)
	tar xz -C $(LINUX32) -f jre-linux32.tar.gz
	tar xz -C $(LINUX32) -f mysql-linux32.tar.gz --exclude="*/lib/*"
	cp -rf ./gui/target/OpenHospital20/* $(LINUX32)/oh
	rm -rf $(LINUX32)/oh/generate_changelog.sh
	cp *.sql POH-README.md POH-linux-changelog.md LICENSE CHANGELOG $(LINUX32)
	cp *.pdf $(LINUX32)/oh/doc
	tar -cvzf $(LINUX32).tar.gz $(LINUX32)

$(LINUX64).tar.gz: compile-all dw-all
	mkdir -p $(LINUX64)/oh/doc
	cp -rf ./poh-bundle-linux-x64/* $(LINUX64)
	tar xz -C $(LINUX64) -f jre-linux64.tar.gz
	tar xz -C $(LINUX64) -f mysql-linux64.tar.gz --exclude="*/lib/*"
	cp -rf ./gui/target/OpenHospital20/* $(LINUX64)/oh
	rm -rf $(LINUX64)/oh/generate_changelog.sh
	cp *.sql POH-README.md POH-linux-changelog.md LICENSE CHANGELOG $(LINUX64)
	cp *.pdf $(LINUX64)/oh/doc
	tar -cvzf $(LINUX64).tar.gz $(LINUX64)

# Compile application binaries
gui/target/OpenHospital20/bin/OH-gui.jar: clone-all
	mvn -T 1.5C package

# Clone repositories of OH components
clone-all: core gui doc
core:
	git clone -b $(OH_VERSION) https://github.com/informatici/openhospital-core.git core
gui:
	git clone -b $(OH_VERSION) https://github.com/informatici/openhospital-gui.git gui
doc:
	git clone -b $(OH_VERSION) https://github.com/informatici/openhospital-doc.git doc

# Compile documentation
docs-all: doc oh-admin-manual.pdf oh-user-manual.pdf
oh-admin-manual.pdf: doc
	asciidoctor-pdf ./doc/doc_admin/AdminManual.adoc -o oh-admin-manual.pdf
oh-user-manual.pdf: doc
	asciidoctor-pdf ./doc/doc_user/UserManual.adoc -o oh-user-manual.pdf

# Create database dump
database.sql: core
	docker-compose -f core/docker-compose.yml up -d; \
	echo -n "Waiting for MySQL to start."; \
	until docker exec -i core_database_1 mysqldump --protocol tcp -h localhost -u isf -pisf123 --no-tablespaces oh > database.sql 2>dump_error.log; \
	do echo -n "."; sleep 2; done; \
	docker-compose -f core/docker-compose.yml down; \
	if grep Error dump_error.log; then exit 1; fi; \
	
# Create changelog file
CHANGELOG: core
	pushd core; \
	lasttag=$(shell git tag -l --sort=-v:refname | head -1); \
	secondlasttag=$(shell git tag -l --sort=-v:refname | head -2 | tail -n 1); \
	popd; \
	cp CHANGELOG_TEMPLATE.md CHANGELOG.md; \
	sed -i "s/VERSION/$(OH_VERSION)/g" CHANGELOG.md; \
	sed -i "s/SECONDLASTTAG/$${secondlasttag//$$'\n'/\\n}/g" CHANGELOG.md; \
	sed -i "s/LASTTAG/$${lasttag//$$'\n'/\\n}/g" CHANGELOG.md; \
	head --lines=-4 CHANGELOG.md > CHANGELOG

# Download JRE and MySQL
dw-all: dw-jre-all dw-mysql-all
dw-jre-all: jre-linux32.tar.gz jre-linux64.tar.gz jre-win.zip
dw-mysql-all: mysql-linux32.tar.gz mysql-linux64.tar.gz mysql-win.zip
jre-linux32.tar.gz:
	wget -q -nc https://cdn.azul.com/zulu/bin/zulu11.39.15-ca-jre11.0.7-linux_i686.tar.gz -O jre-linux32.tar.gz
jre-linux64.tar.gz:
	wget -q -nc https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.7%2B10/OpenJDK11U-jre_x64_linux_hotspot_11.0.7_10.tar.gz -O jre-linux64.tar.gz
jre-win.zip:
	wget -q -nc https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.7%2B10.2/OpenJDK11U-jre_x86-32_windows_hotspot_11.0.7_10.zip -O jre-win.zip
mysql-linux32.tar.gz:
	wget -q -nc https://downloads.mysql.com/archives/get/p/23/file/mysql-5.7.30-linux-glibc2.12-i686.tar.gz -O mysql-linux32.tar.gz
mysql-linux64.tar.gz:
	wget -q -nc https://downloads.mysql.com/archives/get/p/23/file/mysql-5.7.30-linux-glibc2.12-x86_64.tar.gz -O mysql-linux64.tar.gz
mysql-win.zip:
	wget -q -nc https://downloads.mysql.com/archives/get/p/23/file/mysql-5.7.30-win32.zip -O mysql-win.zip