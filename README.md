# juzraai's Toolbox [![Release](https://jitpack.io/v/juzraai/toolbox.svg)](https://jitpack.io/#juzraai/toolbox) [![Build Status](https://travis-ci.org/juzraai/toolbox.svg?branch=master)](https://travis-ci.org/juzraai/toolbox) [![codebeat badge](https://codebeat.co/badges/75cfa9a0-f7d8-417d-88b3-6fcc73f4f9a3)](https://codebeat.co/projects/github-com-juzraai-toolbox)

*Collection of my reusable codes. :)*



## Getting started

The easiest way to use *Toolbox* is adding it as dependency from [JitPack.io](https://jitpack.io/#juzraai/toolbox). Follow the link to get information on how to do this, click on the green *"Get it"* button besides the latest version.



## License

This project is licensed under **Free Public License 1.0.0**, please see `LICENSE` file for details.



## Features

class                                       | description
--------------------------------------------|----------------
`hu.juzraai.toolbox.cache.*Cache`           | Caching
`hu.juzraai.toolbox.data.Identifiable`      | Helper interface
`hu.juzraai.toolbox.data.OrmLiteDatabase`   | Utility for ORMLite
`hu.juzraai.toolbox.data.PaginationContext` | Pagination helper
`hu.juzraai.toolbox.hash.MD5`               | MD5 hashing
`hu.juzraai.toolbox.jdbc.*ConnectionString` | JDBC connection string builders
`hu.juzraai.toolbox.log.LoggerFactory`      | Wrapper for SLF4J's LoggerFactory
`hu.juzraai.toolbox.log.LoggerSetup`        | Log configurator utility
`hu.juzraai.toolbox.meta.Dependencies`      | Dependency checking
`hu.juzraai.toolbox.net.Proxy`              | Proxy settings
`hu.juzraai.toolbox.parse.Regex`            | Regex helper
`hu.juzraai.toolbox.test.Check`             | Preconditions

See their *javadoc* for more information.

Some features need dependencies to be added to your build file. I designed *Toolbox* to look for the required classes and inform you which dependencies are needed.



## Version history

### 17.06

* Added silent method variants to `OrmLiteDatabase`
* Added `@Indexed` annotation to `OrmLiteDatabase`
* Added `outputOnlyToConsole` method to `LoggerSetup`
* Fixed `mysql-connector-java` bug in `DependencyConstants`

### 16.08

* Added logger setup utility and removed `log4j.properties`

### 16.07

* Added pagination helper
* Added expiration feature to `Cache` and `FileCache` implementations
* Added `Identifiable` helper interface
* Added JDBC connection string builders for MySQL and SQLite
* Added `OrmLiteDatabase` utility

### 16.06

* Switching from semantic versioning to datecode, like Ubuntu
    1. there's no framework to use major version for
    2. I always add new features besides fixes, so bugfix number is also unnecessary
    3. I don't do releases too often, but when I do, the version number will tell the date! :D
* Added cache interface and file cache implementations
* Added regex helper
* Improved code quality by adding `@Nonnull` and `@CheckForNull` annotations
* Improved dependency helper
* Improved MD5 hashing

### 0.2.0

* Added dependency helper mechanism
* Added preconditions
* Added preconditions and logging into existing features

### 0.1.0

* Added MD5 hashing
* Added proxy settings