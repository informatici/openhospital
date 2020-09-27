
all: build

build:
	./build_poh.sh

clean: clean-downloads
	git clean -xdff

clean-downloads:
	rm -rf /tmp/oh-download
