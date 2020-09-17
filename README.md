# ssv-api (Starter Single Verticle API)

Template REST API project ready to deploy. REST API based in vert.x framework with lots of plumbing code, examples, use cases and documentation to quickly develope an API with almost no knowledge of vert.x and without any waste of time. 

## It is intended to: 
- Developers who want to quickly and efficiently develop an API without microservices architecture (for now) concentrating only on the business.
- Developers and students who want to learn to work with the vert.x framework.
- Developers who need to develop an API with vert.x and have little or no knowledge of vert.x

## It has the following functionalities:
- HTTP BASIC AUTHENTICATION
- Access to configuration file
- Enabling and disabling endpoints
- Endpoints with GET, POST, PUT y DELETE
- Cross-Origin Resource Sharing (CORS) support
- Execution of blocking code and non-blocking code
- Asynchronous HTTP responses
- System logs
- Builtin SwaggerUI documentation
- Administration endpoints
- Handling HTTP query parameters
- Handling of POST body parameters
- Handling of Multipart files
- Scheduling periodic tasks
- Unit tests for each endpoint
- Postman Collection for testing purposes
- Fatjar generation
- Simple jar generation
- Execution of ant tasks to generate a directory with everything necessary for the deployment

# Documentation

## _Prerequisites_ 📋

* Apache Maven
* JDK 8+
* Postman (optional)

## _Getting started_ 🚀

Since you will likely want to use (Git) version control as well, the fastest route is create a new repository from ssv-api template. This will create a new Github repository using the same structure that ssv-api but with the personalized information that you has to provide. To do this you has to press **Use this template** green button placed at the right top corner of **<> Code** section of this repository.

Or do it by hand by clone the repository, delete its .git/ folder and then create a new Git repository:

```
git clone https://github.com/jgarciasm/ssv-api.git PROJECT_NAME
cd PROJECT_NAME
rm -rf .git
git init
```
_Replace PROJECT_NAME with the name of your project._

Open the pom.xml file and customize the **groupId**, **artifactId**, **version**, **output.dir**, and **jar.name**. You can also change the **main.verticle** property to use your own package name and verticle class.


See ** Deployment ** to know how to deploy the project.

## _Structure of the project_ 🏬

The project contains:

- a pom.xml file

- a main verticle file (src/main/java/com.bugsyteam.verticles/MainVerticle.java)

- a traditional main class (src/main/java/com.bugsyteam.main/VertxApplication.java)

- unit test cases (src/main/test/com.bugsyteam.verticles/MainVerticleTest.java)

- collection of utility classes (src/main/java/com.bugsyteam.utils)

- collection of endpoint call handlers (src/main/java/com.bugsyteam.endpoints)

- a Swagger specification to document the API (postmanTests/ssv-api.postman_collection.json)

- a Postman collection to test the API (src/main/resources/webroot/node_modules/swagger-ui-dist/ssv_api_specification.yml)

_All the classes and methods are well documented for study purposes._


## _Compile_ 📦

Maven is used to compile. For those who are not familiar, it can be compiled using any of the following commands:

```
mvn package
mvn clean package
mvn clean package install
```
- package - Compile to **/target** directory
- clean - clears all content from **/target** before compiling
- install - generate the deployment directory **../PROJECT_NAME-export**

## _Configuration and Deployment_ ⚙️

The file **conf/conf.json** must be modified with the desired parameters.

```
{
"http.port" : 8080,
"api.username" : "user",
"api.password" : "passw",
"cors.enable" : true,
"endpoint.admin.enable" : true,
"endpoint.swagger.enable" : true
}
```
- **http.port** - port on which the service will listen.

- **api.username** y **api.password** - username and password to access the different endpoints using HTTP BASIC AUTHENTICATION.

- **cors.enable** - Enable CORS support.

- **endpoint.admin.enable** - Enable management endpoints.

- **endpoint.swagger.enable** - Enable SwaggerUI documentation in **/doc/** path.


### There are at least 3 ways to run the service:

### Variant 1 (Development environment): 
Run it as an Eclipse project. For this we run the Main com.bugsyteam.main.VertxApplication.java as a Java Application. It is not recommended to debug the project since due to the asynchronous nature of Vert.x and certain restrictions of this framework it will give execution errors.

### Variant 2 (Development environment): 
Run it with:

```
mvn package exec:java
```
This command compiles the project, generates a fat-jar in **/target** directory, runs the tests and executes the service.

### Variant 3: 
This should be the variant used to deploy the service on a server in test or production environment. First compile with:

```
mvn clean package install
```
This will create a directory **PROJECT_NAME-export** at the same height as the project directory. Inside this directory there is a file **PROJECT_NAME-VERSION.jar** that is the one that we will display, along with other directories and files necessary for the execution. Then navigate to the new directory. 

_VERSION will be replaced with the version property of your pom.xml._

```
cd ../PROJECT_NAME-export
```

Open the **conf/config.json** file and edit it with the necessary configurations (if not previously done). Copy the directory to the final location and deploy with:

```
java -jar PROJECT_NAME-VERSION.jar
```

Once running, the service can be tested by opening http://localhost:8080/test in the browser. It should respond **HTTP 200**

## _Testing_ 🔎

In the directory of the project there is a "postmanTests" folder that has a Postman collection that has a battery of tests for the API endpoints. With this project, from Postman, code examples can be self-generated to consume the different services for different languages and technologies.

It also has a battery of unit tests that are run every time the project is compiled. This behavior can be disabled by setting the _**skipTests**_ property of the _**pom.xml**_ to _**true**_. It is highly recommended to leave it in _**false**_ since if any change in the code adds an error to the system, it will be detected quickly.


## _Build with_ 🛠️

* [Vert.x](https://vertx.io/) - Framework for building reactive applications on the JVM
* [Maven](https://maven.apache.org/) - Build automation
* [Postman](https://www.postman.com/) - API Testing
* [Eclipse](https://www.eclipse.org/eclipseide/) - Development IDE


## _Authors_ ✒️

* **Jonad García San Martín** - *Development and Documentation* - [jgarciasm89](jgarciasm89@gmail.com)


## _License_ 📄

jgarciasm/ssv-api is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.

---
