# juzraai's Toolbox [![Release](https://jitpack.io/v/juzraai/toolbox.svg)](https://jitpack.io/#juzraai/toolbox) ![Build Status Images](https://travis-ci.org/juzraai/author-tagger.svg)

**Collection of my reusable codes. :)**



## Getting started

The easiest way to use *Toolbox* is adding it as dependency from [JitPack.io](https://jitpack.io/#juzraai/toolbox). Follow the link to get information on how to do this, click on the green *"Get it"* button besides the latest version.



## License

This project is licensed under **Free Public License 1.0.0**, please see `LICENSE` file for details.



## Features

class                                  | description
---------------------------------------|----------------
`hu.juzraai.toolbox.cache.*Cache`      | Caching
`hu.juzraai.toolbox.hash.MD5`          | MD5 hashing
`hu.juzraai.toolbox.meta.Dependencies` | Dependency checking
`hu.juzraai.toolbox.net.Proxy`         | Proxy settings
`hu.juzraai.toolbox.test.Check`        | Preconditions

See their *javadoc* for more information.

Some features need dependencies to be added to your build file. I designed *Toolbox* to look for the required classes and inform you which dependencies are needed.



## Version history

### 16.06-SNAPSHOT

Currently in **master**, will be released on *June 1st, 2016*.

* Switching from semantic versioning to datecode, like Ubuntu
    1. there's no framework to use major version for
    2. I always add new features besides fixes, so bugfix number is also unnecessary
    3. I don't do releases too often, but when I do, the version number will tell the date! :D
* Added cache interface and gzip file cache implementation
* Improved dependency helper
* Improved MD5 hashing

### 0.2.0

* Added dependency helper mechanism
* Added preconditions
* Added preconditions and logging into existing features

### 0.1.0

* Added MD5 hashing
* Added proxy settings