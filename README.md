# GastroHub API

Backend API for the FIAP PostTech Tech Challenge — Phase 2.

The project provides restaurant management features using Java, Spring Boot, Clean Architecture, automated tests, API documentation, and Docker.

## Project Status

Project setup in progress.

Current focus:

* Base Spring Boot project setup
* Clean Architecture package structure
* User Type CRUD
* User CRUD
* Restaurant CRUD
* Menu Item CRUD
* Swagger/OpenAPI documentation
* Automated tests
* Docker environment

## Tech Stack

* Java 21
* Spring Boot
* Maven
* Spring Web
* Spring Data JPA
* Bean Validation
* PostgreSQL
* Docker
* JUnit 5
* Swagger/OpenAPI

## Architecture

The project follows Clean Architecture principles, separating business rules from technical details.

```text
br.com.fiap.gastrohubapi
├── domain
│   ├── entity
│   └── exception
├── application
│   ├── gateway
│   └── usecase
├── infrastructure
│   ├── config
│   └── persistence
│       ├── entity
│       ├── gateway
│       └── repository
└── presentation
    ├── controller
    ├── dto
    │   ├── request
    │   └── response
    └── mapper
```

## Main Modules

### User Type

Responsible for managing user classifications.

Examples:

* Client
* Restaurant Owner

### User

Responsible for managing application users.

### Restaurant

Responsible for managing restaurant data.

### Menu Item

Responsible for managing restaurant menu items.

## Requirements

Before running the project, make sure you have installed:

* Java 21
* Maven
* Docker
* Git

## How to Run Locally

Clone the repository:

```bash
git clone https://github.com/GastroHubDev/gastrohub-api.git
```

Enter the project folder:

```bash
cd gastrohub-api
```

Run the application:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

## Running Tests

```bash
./mvnw test
```

On Windows:

```bash
mvnw.cmd test
```

## API Documentation

Swagger/OpenAPI documentation will be available after running the application.

Default URL:

```text
http://localhost:8080/swagger-ui.html
```

## Docker

Docker configuration will be added to run the API and database together.

Expected command:

```bash
docker compose up
```

