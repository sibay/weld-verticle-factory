# Weld CDI Vert.x Verticle Factory

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat)](http://doge.mit-license.org)

A vert.x verticle factory with dependency injection based on weld CDI.

## Installation

### Gradle

```gradle
compile 'de.notizwerk:weld-verticle-factory:1.0.0'
```

### Maven

```xml
<dependency>
    <groupId>de.notizwerk</groupId>
    <artifactId>weld-verticle-factory</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

Include the jar in your classpath and deploy your verticle with the ```wcdi``` prefix. To setup the weld container correctly you must provide
the packages which should be scanned for beans and _cdi'ed_ verticles. To do that set the system property 

```vertx-weld-cdi-scanned-package-classes```

to a comma separated list of class names (like _"org.myorg.verticles.FirstVerticle,org.myorg.beans.MyBean"_)  The packages in which the classes reside, are scanned recursively fo beans. Disable the recursion with ```-Dvertx-weld-cdi-recursive-scan=false``` 

Here a short bootstrap example:
```java
System.setProperty(WeldVerticleFactory.SCANNED_PACKAGES_PROP,"de.notizwerk.weldcdi.examples.Bootstrap");
System.setProperty(WeldVerticleFactory.RECURSIVE_SCAN_PROP,"true"); // is true by default
Vertx vertx = Vertx.vertx();
vertx.deployVerticle("wcdi:de.notizwerk.weldcdi.examples.MapperVerticle");
```


There is example code in the examples directory. You can run the example with

```bash
git clone https://github.com/sibay/weld-verticle-factory.git
cd weld-verticle-factory
./gradlew shadow
java -jar weld-verticle-factory-X.Y.Z-SNAPSHOT-all.jar
```

## References

* [Vert.x](vertx.io)
* [Weld](http://weld.cdi-spec.org/)

## Author

[Notizwerk](notizwerk.de)

## License

This project is licensed under the MIT license. See the [LICENSE](LICENSE.txt) file for more info.
