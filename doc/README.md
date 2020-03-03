# openhospital-doc
Documentation in Asciidoc of project https://github.com/informatici/openhospital

## to generate PDF from adoc files (you need Ruby 2.3 or greater) 

* Install the right plugin in order to obtain pdf files:

        gem install ascidoctor-pdf --pre
 
* Convert file from .adoc to .pdf using this command:

        asciidoctor-pdf nomeFile.adoc [-o path/to/nomeFile.pdf]

* Convert file from .adoc to .html using this command: 

        asciidoctor nomeFile.adoc
