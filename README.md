[Truffle](https://github.com/oracle/graal/tree/master/truffle/) which is library for programming language implementations for [GraalVM](https://www.graalvm.org/) has **NFI** for native function call. However Polyglot applications cannot use it because it is **internal language** for Truffle.

nfiwrapper gives ability to use NFI from Polyglot applications. Thus you can call C functions directly without [JNI](https://docs.oracle.com/javase/jp/11/docs/specs/jni/index.html).

# Requirements

GraalVM 20.1.0 (JDK 11) or later

* You need to change some code and [distribution.xml](src/main/assembly/distribution.xml) if you want to use it on GraalVM JDK 8.

# How to build

```
$ export JAVA_HOME=$GRAALVM_HOME
$ mvn package
```

You can see the following files after building in `target`:

* `truffle-nfi-wrapper.jar`
    * nfiwrapper
* `truffle-nfi-wrapper-<version>-component.jar`
    * nfiwrapper component for `gu`

# Install

Install `truffle-nfi-wrapper-<version>-component.jar` via [gu](https://www.graalvm.org/docs/reference-manual/install-components/).

You can see nfiwrapper on `gu list` if instlation is succeeded like following:

```
$ $GRAALVM_HOME/bin/gu list
ComponentId              Version             Component name      Origin
--------------------------------------------------------------------------------
js                       20.2.0              Graal.js
nfiwrapper               0.1.0               Truffle NFI Wrapper
graalvm                  20.2.0              GraalVM Core
```

## Local install

```
$ $GRAALVM_HOME/bin/gu install -L target/truffle-nfi-wrapper-0.1.0-component.jar
```

## Install from online catalog

You can install nfiwrapper via [content catalog](https://github.com/YaSuenag/nfiwrapper/blob/master/nfiwrapper-catalog-java11.properties) on this repository. This catalog contains information for GraalVM 20.1.0 and 20.2.0.

```
$ ~/graalvm/graalvm-ce-java11-20.2.0/bin/gu install -C https://raw.githubusercontent.com/YaSuenag/nfiwrapper/master/nfiwrapper-catalog-java11.properties nfiwrapper
```

# Try nfiwrapper

nfiwrapper passes all of commands to NFI, so you can refer [document of Truffle NFI](https://github.com/oracle/graal/blob/master/truffle/docs/NFI.md) for use.

1. Load library ( `load` or `default` )
2. Get member from function symbol
3. Bind signature
4. Invoke

You can refer [document of Truffle NFI](https://github.com/oracle/graal/blob/master/truffle/docs/NFI.md) to know signature format and/or type mapping between C and Truffle NFI.

For example, [examples/GetPID.java](examples/GetPID.java) would get PID via [getpid()](https://linuxjm.osdn.jp/html/LDP_man-pages/man2/getpid.2.html) (on Linux) or [GetCurrentProcessId()](https://docs.microsoft.com/en-us/windows/win32/api/processthreadsapi/nf-processthreadsapi-getcurrentprocessid) (on Windows).

```
$ $GRAALVM_HOME/bin/javac GetPID.java
$ $GRAALVM_HOME/bin/java GetPID
PID: 980
```

Also you can use nfiwrapper on [polyglot](https://www.graalvm.org/docs/reference-manual/polyglot/#running-polyglot-applications) command. Following code is example on `polyglot` on Linux.

```
$ $GRAALVM_HOME/bin/polyglot --shell
GraalVM MultiLanguage Shell 20.2.0
Copyright (c) 2013-2019, Oracle and/or its affiliates
  JavaScript version 20.2.0
  TruffleNFIWrapper version 0.1.0
Usage:
  Use Ctrl+L to switch language and Ctrl+D to exit.
  Enter -usage to get a list of available commands.
js> library = Polyglot.eval('nfiwrapper', 'load "libc.so.6"')
{}
js> getpid = library['getpid'].bind('():SINT32')
Native Symbol
js> getpid()
806
```

## Crash example

[MemSetCrash.java](examples/MemSetCrash.java) is crash example at `memset()` with writing `NULL`. It is useful to check coredump and/or hs_err log. This example is for Linux only.
