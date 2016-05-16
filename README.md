# juzraai's Toolbox [![Release](https://jitpack.io/v/juzraai/toolbox.svg)](https://jitpack.io/#juzraai/toolbox)

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

### 0.3.0-SNAPSHOT (currently in **master** but not released yet)

* Added cache interface and gzip file cache implementation (awaits testing)
* Improved dependency helper

### 0.2.0

* Added dependency helper mechanism
* Added preconditions
* Added preconditions and logging into existing features

### 0.1.0

* Added MD5 hashing
* Added proxy settings