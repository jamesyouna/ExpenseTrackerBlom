# Expense Tracker API

A RESTful backend application for managing personal expenses, built with **Java and Spring Boot**.

The application allows users to create an account, authenticate securely, and manage their own expenses. Each user's expense data is isolated so authenticated users can only access and modify their own records.

> **Project Status:** Under active development.

## Features

### Authentication & Users
- User registration
- User login
- JWT-based authentication
- BCrypt password hashing
- User profile management
- Stateless Spring Security configuration

### Expense Management
- Create expenses
- View expenses
- Update expenses
- Delete expenses
- User-specific expense ownership
- Pagination
- Filter expenses by category
- Search expenses by name
- Filter expenses by date range

### Backend
- RESTful API architecture
- Spring Data JPA repositories
- MySQL database integration
- Input validation
- Global exception handling
- Layered application architecture

## Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Web MVC**
- **Spring Data JPA**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Hibernate**
- **MySQL**
- **Maven**
- **Postman**
- **Git & GitHub**

## Architecture

The project follows a layered backend architecture:

```text
Client / Postman
       |
       v
   Controller
       |
       v
     Service
       |
       v
   Repository
       |
       v
     MySQL
```

Authentication requests additionally pass through the Spring Security and JWT authentication components.

## Authentication Flow

```text
Login Request
     |
     v
Verify Email & Password
     |
     v
Generate JWT
     |
     v
Return Token
     |
     v
Authorization: Bearer <token>
     |
     v
JWT Filter
     |
     v
Protected API Endpoint
```

The API uses stateless authentication. After login, the generated JWT must be included with requests to protected endpoints.

## Project Structure

```text
src/main/java/com/BLOM/expensetrackerapi/
|
|-- config/          # Spring Security configuration
|-- controller/      # REST API controllers
|-- entity/          # JPA entities and request/response models
|-- exceptions/      # Custom and global exception handling
|-- impls/           # Service implementations
|-- repository/      # Spring Data JPA repositories
|-- security/        # JWT authentication components
|-- service/         # Service interfaces
|-- util/            # JWT utilities
|
`-- ExpensetrackerapiApplication.java
```

## Configuration

For security reasons, the real `application.properties` file is excluded from version control.

An example configuration is provided:

```text
src/main/resources/application-example.properties
```

Create your own `application.properties` based on the example and provide your local database credentials and JWT secret.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expensetracker
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update

jwt.secret=YOUR_JWT_SECRET
```

Never commit real passwords or JWT secrets to the repository.

## Running the Application

### Requirements

- Java 21
- MySQL
- Maven or the included Maven Wrapper

Create the MySQL database:

```sql
CREATE DATABASE expensetracker;
```

Configure `application.properties`, then run the application using:

### Windows

```bash
mvnw.cmd spring-boot:run
```

### macOS / Linux

```bash
./mvnw spring-boot:run
```

The API runs locally at:

```text
http://localhost:8080/api/v1
```

## API Security

The following endpoints are publicly accessible:

```text
POST /register
POST /login
```

Other application endpoints require authentication using:

```text
Authorization: Bearer <JWT_TOKEN>
```

## Development

The project is being developed incrementally using Git for version control.

Upcoming work will continue to expand the application's functionality, testing, documentation, and overall user experience.

## Author

**James Youna**  
Computer and Communications Engineering Student  
American University of Science and Technology (AUST)

- **Email:** jamesyouna@gmail.com
- **GitHub:** [jamesyouna](https://github.com/jamesyouna)



