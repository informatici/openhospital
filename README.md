# Open Hospital

[Open Hospital][openhospital] (OH) is an electronic health record (EHR) application.
Open Hospital is currently provided as a desktop application written in Java
and it's designed to work without an internet connection.

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

> NB: GitHub Issues are disabled on the repositories of individual components. 
They are enabled on this repository to collect bug reports from end users and issues about this repository only. 
Instead, Jira is used to collect issues and plan the development across all OH components.

## Documentation

Read on about Open Hospital:

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
 [contributing]: https://github.com/informatici/openhospital/blob/master/CONTRIBUTING.md
 [jira]: https://openhospital.atlassian.net/browse/OP
 [good-first]: https://openhospital.atlassian.net/browse/OP-188?filter=10206
 [user-man]: https://github.com/informatici/openhospital-doc/blob/master/doc_user/UserManual.adoc
 [admin-man]: https://github.com/informatici/openhospital-doc/blob/master/doc_admin/AdminManual.adoc
 [faq]: https://openhospital.atlassian.net/wiki/spaces/OH/pages/568951013/Getting+Started+FAQ
 [wiki]: https://openhospital.atlassian.net/wiki/spaces/OH/overview
 [slack]: https://join.slack.com/t/openhospitalworkspace/shared_invite/enQtOTc1Nzc0MzE2NjQ0LWIyMzRlZTU5NmNlMjE2MDcwM2FhMjRkNmM4YzI0MTAzYTA0YTI3NjZiOTVhMDZlNWUwNWEzMjE5ZDgzNWQ1YzE
 [ml]: https://sourceforge.net/projects/openhospital/lists/openhospital-devel
