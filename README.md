# AES Java Implementation

This project is an AES implementation in Java for a college assignment, don't use it for anything serious. This code was 100% human-made by me.
## How to use

### Option 1: Download the JAR

Download the JAR from the [latest release](https://github.com/uninhm/AES-java/releases/).

Then add the JAR to your Java project's dependencies and import the `AES` class:

```java
import aes.AES;

import java.util.HexFormat;

public class Main {
    public static void main(String[] args) {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f");
        System.out.println(HexFormat.of().formatHex(aes.encrypt("Hello World!")));
    }
}
```

The AES variant (128, 192, 256) will be deduced from the key length.

### Option 2: Install with Maven

Clone this repository and install the library into your local Maven repository:

```bash
mvn install
```

Then add the following dependency to your project's `pom.xml`:

```xml
<dependency>
    <groupId>dev.uninhm.aes</groupId>
    <artifactId>AES-java</artifactId>
    <version>1.1.0</version>
</dependency>
```

You can then import and use the library normally like in the code shown in [option 1](#option-1-download-the-jar).

Note: The package also exports the `KeyGenerator` class so that you can experiment with it or debug your own implementation. See [the docs](https://uninhm.github.io/AES-java/aes/KeyGenerator.html).
