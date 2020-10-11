SHELL = /bin/bash
OH_VERSION ?= $(shell git describe --abbrev=0 --tags)
POH_VERSION ?= "1.0"

.PHONY: build clone-all clean clean-downloads

all: build

clean: clean-downloads
	git clean -xdff

clean-downloads:
	rm -rf /tmp/oh-download

assemble: build

build: clone-all
	mvn -T 1.5C package

clone-all: core gui doc
	@echo Cloned all projects.
core:
	git clone -b $(OH_VERSION) https://github.com/informatici/openhospital-core.git core
gui:
	git clone -b $(OH_VERSION) https://github.com/informatici/openhospital-gui.git gui
doc:
	git clone -b $(OH_VERSION) https://github.com/informatici/openhospital-doc.git doc

docs-all: oh-admin-manual.pdf oh-user-manual.pdf
oh-admin-manual.pdf:
	asciidoctor-pdf ./doc/doc_admin/AdminManual.adoc -o oh-admin-manual.pdf
oh-user-manual.pdf: 
	asciidoctor-pdf ./doc/doc_user/UserManual.adoc -o oh-user-manual.pdf
