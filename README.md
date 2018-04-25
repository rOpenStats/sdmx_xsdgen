# sdmx_xsdgen
Converts DSD files to SDX. Project for compiling with supported version of Java (sources files are originally for Java-1.5)

[sdmx download site](https://sdmx.org/?page_id=4696)


# Build

```
cd ../xsdgen-lib-2.0-1.3.1-src/
#mvn package 
# Failed because No test samples dir included in downloaded files
# i.e. sdmx_2_0/samples/estat_sts/ESTAT_STS_v2.2.xml
mvn package -Dmaven.test.skip=true
```

```
cd ../xsdgen-lib-2.1-1.3.1-src/
#mvn package 
# Failed because No test samples dir included in downloaded files
# i.e. sdmx_2_1/samples/demography/demography.xml
mvn package -Dmaven.test.skip=true
```

```
cd ../xsdgen-app-1.3.1-src/
mvn package 
```


# Run
```
cd ../xsdgen-app-1.3.1-src/
java -jar target/xsdgen-app-1.3.1.jar
```

