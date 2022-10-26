# [LibreSSL](https://www.libressl.org/) test vector generator

A slightly over-engineered tool to generate test vectors for [LibreSSL](https://www.libressl.org/) tests.
This works by having sets of input data which is then used with `javax.crypto` to preform cryptographic operations and then outputs test vectors with the input and expected output data.

This is written in Java to allow [LibreSSL](https://www.libressl.org/)'s outputs to be validated against something else to make sure they are correct, if LibreSSL's output doesn't match, then it may mean that there is a problem inside LibreSSL or Java. If the test outputs were generated using LibreSSL/OpenSSL then it would be possible that it's generating the data wrong and no one would know.

## Generated test vectors
As of October 26, 2022; this generates test vectors for:
 - Blowfish ([`bf_test.c`](https://cvsweb.openbsd.org/cgi-bin/cvsweb/src/regress/lib/libcrypto/bf/bf_test.c)): CBC, CFB64, OFB64
 - RC2 ([`rc2_test.c`](https://cvsweb.openbsd.org/cgi-bin/cvsweb/src/regress/lib/libcrypto/rc2/rc2_test.c)): CBC, CFB64, OFB64

## Usage
```
java -jar libressl-test-gen.jar <algorithm>
        <algorithm>   The algorithm to generate test vectors for.
```


## Building

### If you have Gradle 7.0+ installed:
Simply run `gradle build` in the project directory.

### If you do not have Gradle installed:
**Unix**: Run `./gradlew build` in the project directory.  
**Windows**: Run `./gradlew.bat build` in the project directory.

### Build output
The build output can be found in `./build/`.  
The compiled `.jar` file can be found at `./build/libs/libressl-test-gen.jar`


## Contact
If you don't want to install Java to run this; need help adding more generators; or something else, contact me and I may be able to help you:
 - Email: [`joshua@hypera.dev`](mailto:joshua@hypera.dev)