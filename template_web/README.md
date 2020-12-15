# Template
Template en Bootstrap 4, Thymeleaf 3, Spring Boot 2 para JBoss.

Configuración de librería Oracle, Maven local:
```
mvn install:install-file -Dfile=C:\\OracleJDBC\\ojdbc7.jar-DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0 -Dpackaging=jar
```

Configuración de librería Oracle, pom.xml:
```
<dependency>
   <groupId>com.oracle</groupId>
   <artifactId>ojdbc7</artifactId>
   <version>12.1.0</version>
</dependency>
```

Configuración de conexión a la base de datos:
```
url: jdbc:oracle:thin:@//10.1.201.238:1521/XE
username: MGR_TEMPLATE
password: t3mpl4t3
```

Nombre datasource en Servidor de Aplicaciones:
```
java:/dsTemplate
```