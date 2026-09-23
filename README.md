# Spring Boot Controls Demo

A basic web app using **Java 21** and **Spring Boot 4.1.1**, with plain HTML, CSS, and JavaScript.

## Requirements

- JDK 21 (set `JAVA_HOME` to your JDK 21 installation)
- Maven 3.6.3 or newer

## Run

```sh
mvn spring-boot:run
```

Open http://localhost:8080. Stop the app with Ctrl+C.

On this Windows machine, you can select the installed Java 21 JDK for the current PowerShell session:

```powershell
$env:JAVA_HOME = 'P:\Java\jdk-21.0.2'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn spring-boot:run
```

## Try it

The page includes a text box, dropdown (combo box), slider with a live percentage,
date picker, radio buttons, checkbox, text area, submit button, and reset button.
Submit sends JSON to `POST /api/demo`; Spring Boot validates and echoes the values
for display alongside the form. Theme and notification selections are demonstration
values only. Nothing is stored, and no database or frontend build tooling is needed.

## Test and package

```sh
mvn clean verify
java -jar target/controls-demo-0.0.1-SNAPSHOT.jar
```

Integration tests start a real HTTP server and check the home page, form submission,
and rejection of invalid input. To use another port, pass `--server.port=8081` to the jar.

## Layout

- `src/main/java/com/example/controlsdemo`: application entry point and JSON controller
- `src/main/resources/static`: web page, stylesheet, and browser JavaScript
- `src/test/java/com/example/controlsdemo`: HTTP integration tests
