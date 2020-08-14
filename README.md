# API Destinatarios

_API encargada de parsear y extraer información de empresas de diferentes sitios web públicos y almacenarla en una base de datos. También expone una serie de endpoints para gestionar la base de datos._

# Documentación

_La documentación se puede encontrar en http://preapidoc.anf.es/?url=api_destinatarios_specification.yml_

## Comenzando 🚀

_Clona el proyecto con:_

```
git clone http://git.anf.es/jonad/ApiDestinatarios.git API_Destinatarios
```

Consulte **Despliegue** para conocer como desplegar el proyecto.


## Pre-requisitos 📋

* Apache Maven
* JDK 8+
* MySQL
* Postman (opcional)

## Instalación y Despliegue 🔧📦

_Se debe instalar MySQL y crear una nueva base de datos con el script "exportScript.sql" que se encuentra en la raiz del proyecto clonado. Se puede asignar el nombre deseado a la BD, sólo hay que tenerlo en cuenta y modificar el fichero "conf/conf.json" con la URL y credenciales correctas._

_Existen al menos 4 formas de ejecutar el servicio:_

### Variante 1 (Entorno de desarrollo): 
_Ejecutarlo como un proyecto de Eclipse. Para esto ejecutamos el Main com.anf.legal.crm.main.VertxApplication.java como un Java Application. No es recomendado Debuguear el proyecto ya que debido a la naturaleza asíncrona de Vert.x y ciertas restricciones de este framework dará errores de ejecución._

### Variante 2 (Entorno de desarrollo): 
_Ejecutarlo con:_

```
mvn test exec:java
```
_Este comando compila el proyecto, genera un fat-jar en el directorio "target", corre las pruebas y ejecuta el servicio._

Run `redeploy.sh` (or `redeploy.bat` on Windows) and the project recompiles while editing.

NOTE: the `redeploy` script are also configurable

### Variante 3 (Entorno de desarrollo): 
_Ejecutar `redeploy.sh` (o `redeploy.bat` en Windows) y el proyecto se recompilará mientras se edita._

NOTA: el script `redeploy` también es configurable.

### Variante 4: 
_Esta debe ser la variante usada para desplegar el servicio en un servidor en entorno de prueba o producción. Primeramente compilar con:_

```
mvn clean package install
```
_Esto creará un directorio "service-destinatarios" a la misma altura que el directorio del proyecto. Dentro de este directorio se encuentra un fichero "api-destinatarios.jar" que es el que desplegaremos, junto con otros directorios y ficheros necesarios para la ejecución. A continuación navegar hasta el nuevo directorio. 

```
cd ../ service-destinatarios
```

Abrir el fichero "config.json.example" y editarlo con los datos de conexión a la base de datos que se usará (si no se realizó anteriormente). Guardar una copia en la carpeta "service-destinatarios/conf" quitando la extensión ".example", quedando como "conf.json"  y desplegar con:_

```
java -jar api-destinatarios.jar
```

_Una vez en ejecución se puede probar el servicio abriendo en el navegador  http://localhost:8080/destinatarios/test. Se debe ver:_

```
{
    "Api Version": "0.2.5",
    "ANFInfoWeb Version": "0.2.5"
}
```

## Ejecutando las pruebas ⚙️

_En el directorio del proyecto clonado se encuentra una carpeta "postmanTests" que cuenta con un proyecto de Postman que tiene una batería de pruebas a cada uno de los endpoints de la API. Con este proyecto, desde Postman, se pueden autogenerar ejemplos de código para consumir los diferentes servicios para diferentes lenguajes y tecnologías._


## Construido con 🛠️

* [Vert.x](http://www.dropwizard.io/1.0.2/docs/) - El framework web usado
* [Maven](https://maven.apache.org/) - Manejador de dependencias
* [MySQL](https://dev.mysql.com/) - Base de datos usada para almacenar los Destinatarios
* [Postman](https://www.postman.com/) - Aplicación para realizar pruebas sobre la API


## Autores ✒️

* **Jonad García San Martín** - *Desarrollo, Documentacion y Captura de Requisitos* - [jgarciasm89](jgarciasm89@gmail.com)


## Licencia 📄

Este proyecto está bajo protección de Copyright protección de ANF AC.

---
