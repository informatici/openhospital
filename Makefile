SHELL = /bin/bash
OH_VERSION ?= $(shell git describe --abbrev=0 --tags)
POH_VERSION ?= "1.0"

.PHONY: build clone-all clean clean-downloads dw-all dw-jre-all dw-mysql-all compile compile-all docs-all

all: build

# Clean targets
clean:
	git clean -xdff
clean-downloads:
	rm -rf *.zip *.tar.gz

assemble: build

build: compile-all dw-all

compile-all: compile docs-all CHANGELOG

# Compile application binaries
compile: clone-all
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