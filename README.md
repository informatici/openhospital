## Download

![GitHub all releases](https://img.shields.io/github/downloads/informatici/openhospital/total?label=GitHub%20Downloads)
![GitHub release (latest by date)](https://img.shields.io/github/downloads/informatici/openhospital/latest/total?label=latest)
[ [Download latest](https://github.com/informatici/openhospital/releases/latest) ] [ [All releases](https://github.com/informatici/openhospital/releases) ]

![SourceForge](https://img.shields.io/sourceforge/dt/openhospital?color=33ccff&label=SourceForge%20Downloads)
![SourceForge](https://img.shields.io/sourceforge/dt/openhospital/v1.10.0?color=33ccff&label=latest&logoColor=33ccff)
[ [Download latest (mirror)](https://sourceforge.net/projects/openhospital/) ] [ [All releases (mirror)](https://sourceforge.net/projects/openhospital/files/) ]

# Open Hospital

[Open Hospital][openhospital]OH - Open Hospital (https://www.open-hospital.org/) is a free and open-source Health Information Management System (HIMS) software application.
Open Hospital is deployed as a desktop application that can be used in a standalone, single user mode (PORTABLE mode)
or in a client / server network configuration (CLIENT mode), where multiple clients and users connect to the same database server.

OH is developed in Java and it is based on open-source tools and libraries; it runs on any computer, requires low resources and is designed to work without an internet connection.

Open Hospital is composed by the following components, hosted in separated repositories:
 - [OH Core][core], a library that contains the business logic and the data abstraction layer
 - [OH GUI][gui], which provides a graphical user interface (GUI) made with Java Swing
 - [OH Doc][doc], which contains the user and admin documentation in Asciidoc format
 - [OH UI][ui], a web user interface that consists of a React SPA (single page application) [*WIP*]
 - [OH API][api], a web server that exposes REST APIs over the Core component, and it's used by the UI component [*WIP*]. 

This repository is used to assemble the portable (or all-in-one) packages of Open Hospital, which you can download [here][releases].

## How to contribute

There are several ways in which you can contribute to Open Hospital:

- try the [desktop application][releases] or the early versions of the [web UI][ui]
- request new features or report issues on [JIRA][jira] ([here][good-first]'s a list of *good-first-issues*)
- improve the [documentation][doc]
- contribute code patches to one of the components

## Documentation

Read on about Open Hospital:

 - on the official [website][openhospital]
 - [user][user-man] and [admin][admin-man] manuals
 - [wiki]
 - [FAQ][faq]

## Community

You can reach out to the community of contributors by joining 
our [Slack workspace][slack] or by subscribing to our [mailing list][ml].


## How to create OH packages

<details><summary>:construction_worker: :package:</summary>
To create the Open Hospital packages,
make sure to have installed the following dependencies on a Linux machine:
JDK 8+, Maven, asciidoctor-pdf, zip, GNU make.

Then follow these simple steps:

 1. Clone this repository:

        git clone https://github.com/informatici/openhospital

 2. Run the script that compiles the components of Open Hospital, and assembles the portable distributions:

        cd openhospital
        make
    
    You can also parallelize some make tasks by using the `-j` flag (e.g. `make -j4`)
    or use intermediate targets to build single parts of the distribution -
    use `make help` to see a list of available targets.
</details>

 [openhospital]: https://www.open-hospital.org/
 [core]: https://github.com/informatici/openhospital-core
 [gui]: https://github.com/informatici/openhospital-gui
 [ui]: https://github.com/informatici/openhospital-ui
 [api]: https://github.com/informatici/openhospital-api
 [doc]: https://github.com/informatici/openhospital-doc
 [releases]: https://github.com/informatici/openhospital/releases
 [jira]: https://openhospital.atlassian.net/browse/OP
 [good-first]: https://openhospital.atlassian.net/browse/OP-188?filter=10206
 [user-man]: https://github.com/informatici/openhospital-doc/blob/master/doc_user/UserManual.adoc
 [admin-man]: https://github.com/informatici/openhospital-doc/blob/master/doc_admin/AdminManual.adoc
 [faq]: https://openhospital.atlassian.net/wiki/spaces/OH/pages/568951013/Getting+Started+FAQ
 [wiki]: https://openhospital.atlassian.net/wiki/spaces/OH/overview
 [slack]: https://join.slack.com/t/openhospitalworkspace/shared_invite/enQtOTc1Nzc0MzE2NjQ0LWIyMzRlZTU5NmNlMjE2MDcwM2FhMjRkNmM4YzI0MTAzYTA0YTI3NjZiOTVhMDZlNWUwNWEzMjE5ZDgzNWQ1YzE
 [ml]: https://sourceforge.net/projects/openhospital/lists/openhospital-devel
