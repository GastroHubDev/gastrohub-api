# GastroHub API

Backend API for managing users, user types, restaurants, and restaurant menu items.

This project was developed as part of the FIAP Post Tech Phase 2 Tech Challenge. The goal is to build a restaurant management system using Spring Boot, Clean Architecture principles, automated tests, and API documentation.

## Features

* User management
* User type management
* Restaurant management
* Menu item management
* API documentation with Swagger
* H2 database for local and test execution
* Automated unit and integration tests

## Tech Stack

* Java 21
* Spring Boot 3.5.15
* Spring Web
* Spring Data JPA
* Spring Validation
* H2 Database
* MySQL Driver
* Springdoc OpenAPI / Swagger
* Lombok
* JUnit 5
* Mockito
* MockMvc
* Maven

## Architecture

The project follows a layered structure based on Clean Architecture ideas.

```text
src/main/java/br/com/fiap/gastrohubapi
├── domain
├── application
├── infrastructure
└── presentation
```

### Layers

| Layer            | Responsibility                                          |
| ---------------- | ------------------------------------------------------- |
| `domain`         | Business entities, enums, and domain exceptions         |
| `application`    | Use cases and gateway contracts                         |
| `infrastructure` | JPA entities, repositories, and gateway implementations |
| `presentation`   | Controllers, DTOs, mappers, and exception handlers      |

### Dependency Flow

```text
Presentation → Application → Domain
Infrastructure → Application
```

The application layer depends on gateway interfaces, not directly on database implementations. The infrastructure layer implements those gateway contracts using JPA.

## Main Modules

* `User`
* `UserType`
* `Restaurant`
* `MenuItem`

## API Documentation

After running the application, access Swagger UI at:

```text
http://localhost:8080/swagger-ui.html
```

Swagger contains the available endpoints, request bodies, response models, and HTTP status codes.

## Main Endpoints

### User Types

| Method   | Endpoint           | Description            |
| -------- | ------------------ | ---------------------- |
| `POST`   | `/user-types`      | Create a user type     |
| `GET`    | `/user-types`      | List all user types    |
| `GET`    | `/user-types/{id}` | Find a user type by ID |
| `PUT`    | `/user-types/{id}` | Update a user type     |
| `DELETE` | `/user-types/{id}` | Delete a user type     |

Example request:

```json
{
  "name": "Client",
  "baseCategory": "CLIENT"
}
```

Available base categories:

```text
CLIENT
OWNER
```

### Users

| Method | Endpoint             | Description         |
| ------ | -------------------- | ------------------- |
| `POST` | `/users`             | Create a user       |
| `GET`  | `/users/{id}`        | Find a user by ID   |
| `GET`  | `/users/name/{name}` | Find a user by name |

### Restaurants

| Method   | Endpoint              | Description             |
| -------- | --------------------- | ----------------------- |
| `POST`   | `/restaurants`        | Create a restaurant     |
| `GET`    | `/restaurants`        | List all restaurants    |
| `GET`    | `/restaurants/{id}`   | Find a restaurant by ID |
| `GET`    | `/restaurants/search` | Search restaurants      |
| `PUT`    | `/restaurants/{id}`   | Update a restaurant     |
| `DELETE` | `/restaurants/{id}`   | Delete a restaurant     |

### Menu Items

| Method   | Endpoint                                | Description                   |
| -------- | --------------------------------------- | ----------------------------- |
| `POST`   | `/menu-items`                           | Create a menu item            |
| `GET`    | `/menu-items`                           | List all menu items           |
| `GET`    | `/menu-items/{id}`                      | Find a menu item by ID        |
| `GET`    | `/menu-items/restaurant/{restaurantId}` | List menu items by restaurant |
| `PUT`    | `/menu-items/{id}`                      | Update a menu item            |
| `DELETE` | `/menu-items/{id}`                      | Delete a menu item            |

## Database Configuration

The project currently uses H2 for local and test execution.

### Local H2 Database

Configured in:

```text
src/main/resources/application.properties
```

Main local database URL:

```properties
spring.datasource.url=jdbc:h2:mem:gastrohub
```

H2 Console:

```text
http://localhost:8080/h2-console
```

### Test H2 Database

Configured in:

```text
src/test/resources/application-test.properties
```

Main test database URL:

```properties
spring.datasource.url=jdbc:h2:mem:gastrohub_test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL
```

The test profile uses `create-drop` so the database schema is created and removed during test execution.

## How to Run

### Requirements

* Java 21
* Maven or Maven Wrapper

### Run with Maven Wrapper

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

The application will start at:

```text
http://localhost:8080
```

## How to Test

### Run all tests

Windows:

```powershell
.\mvnw.cmd test
```

Linux/macOS:

```bash
./mvnw test
```

### Run a specific test class

Windows:

```powershell
.\mvnw.cmd -Dtest=UserTypeControllerIntegrationTest test
```

Linux/macOS:

```bash
./mvnw -Dtest=UserTypeControllerIntegrationTest test
```

## Test Strategy

The project includes unit and integration tests.

### Unit Tests

Unit tests cover isolated parts of the system without starting the full Spring context.

Current examples:

```text
domain/entity/UserTypeTest
application/usecase/usertype/*
application/usecase/menuitem/*
presentation/mapper/*
```

They validate:

* Domain rules
* Use case behavior
* Gateway interaction through mocks
* DTO mapping

### Integration Tests

Integration tests start the Spring context and validate the request flow through the API.

Current example:

```text
presentation/controller/UserTypeControllerIntegrationTest
```

This test covers the UserType endpoints using:

* `@SpringBootTest`
* `@AutoConfigureMockMvc`
* `@ActiveProfiles("test")`
* H2 test database

The flow tested is:

```text
Controller → UseCase → Gateway → JPA → H2
```

## UserType Module Coverage

The UserType module includes:

* Domain entity tests
* Use case unit tests
* Mapper tests
* Controller integration tests
* UserType-specific exception handling

Covered endpoints:

```text
POST   /user-types
GET    /user-types
GET    /user-types/{id}
PUT    /user-types/{id}
DELETE /user-types/{id}
```

Covered error scenarios include:

* Invalid request
* User type not found
* Duplicate user type name
* User type in use

## Docker

A `docker-compose.yml` file was not found in the current project version.

Docker Compose setup can be added later to run the application together with a database service.

## Project Status

Current implemented modules:

* User
* UserType
* Restaurant
* MenuItem

Current quality support:

* Swagger API documentation
* Automated tests
* Clean layered package structure
* H2 local/test configuration
