# GastroHub API

Backend API for managing users, user types, restaurants, and menu items.

This project was developed as part of the **FIAP Post Tech -- Phase 2
Tech Challenge**, using **Spring Boot**, **Clean Architecture**,
automated tests, Docker, and Swagger/OpenAPI.

------------------------------------------------------------------------

## Features

-   User management
-   User type management
-   Restaurant management
-   Menu item management
-   Swagger / OpenAPI documentation
-   Postman collection
-   H2 database for development and tests
-   MySQL 8.4 with Docker Compose
-   Unit and integration tests
-   Global exception handling

------------------------------------------------------------------------

## Tech Stack

-   Java 21
-   Spring Boot 3.5.15
-   Spring Web
-   Spring Data JPA
-   Spring Validation
-   H2 Database
-   MySQL 8.4
-   Springdoc OpenAPI
-   JUnit 5
-   Mockito
-   MockMvc
-   Maven
-   Docker
-   Docker Compose

------------------------------------------------------------------------

## Architecture

``` text
src/main/java/br/com/fiap/gastrohubapi
├── domain
├── application
├── infrastructure
└── presentation
```

  -----------------------------------------------------------------------
Layer                 Responsibility
  --------------------- -------------------------------------------------
Domain                Business entities, enums and exceptions

Application           Use cases and gateway contracts

Infrastructure        JPA entities, repositories and gateway
implementations

Presentation          Controllers, DTOs, mappers and exception handlers
-----------------------------------------------------------------------

Dependency flow:

``` text
Presentation → Application → Domain
Infrastructure → Application
```

------------------------------------------------------------------------

## Main Modules

-   User
-   UserType
-   Restaurant
-   MenuItem

------------------------------------------------------------------------

## API Documentation

Swagger UI:

``` text
http://localhost:8080/swagger-ui.html
```

OpenAPI:

``` text
http://localhost:8080/v3/api-docs
```

------------------------------------------------------------------------

## Running the Project

### Requirements

-   Java 21
-   Docker + Docker Compose (optional)

### Run locally (H2)

Windows

``` powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS

``` bash
./mvnw spring-boot:run
```

Application:

``` text
http://localhost:8080
```

Swagger:

``` text
http://localhost:8080/swagger-ui.html
```

H2 Console:

``` text
http://localhost:8080/h2-console
```

### Run with Docker

``` bash
docker compose up --build
```

This starts:

-   MySQL 8.4
-   GastroHub API

Stop containers:

``` bash
docker compose down
```

Reset database:

``` bash
docker compose down -v
```

------------------------------------------------------------------------

## Database

The project supports:

-   H2 (development and tests)
-   MySQL 8.4 (Docker / production profile)

Configuration files:

``` text
src/main/resources/application.properties
src/main/resources/application-prod.properties
src/test/resources/application-test.properties
```

------------------------------------------------------------------------

## Running Tests

Run all tests:

Windows

``` powershell
.\mvnw.cmd test
```

Linux/macOS

``` bash
./mvnw test
```

Run a specific test:

``` bash
./mvnw -Dtest=UserTypeControllerIntegrationTest test
```

Tests include:

-   Domain
-   Use Cases
-   Persistence
-   Controllers
-   Mappers
-   Spring Context

------------------------------------------------------------------------

## Docker

The project includes:

``` text
Dockerfile
docker-compose.yml
```

Docker Compose provisions:

-   MySQL 8.4
-   GastroHub API

SQL initialization scripts:

``` text
sql/01-schema.sql
sql/02-seed.sql
```

------------------------------------------------------------------------

## Postman

``` text
postman/GastroHub_API.postman_collection.json
```

------------------------------------------------------------------------

## Documentation

Technical report:

``` text
docs/RelatorioTecnicoGastroHubFase2.html
docs/RelatorioTecnicoGastroHubFase2.pdf
```
