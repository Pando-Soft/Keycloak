# KeyCloak Application

This project integrates Spring Boot with Keycloak for user management and authentication. It enables the creation and
management of users and admins within Keycloak directly from the Spring Boot application. The project also incorporates
PostgreSQL for database management, Swagger for API documentation, Spring Security for securing endpoints, and
localization to handle exceptions and messages in different languages.

## Table of Contents

- [Overview](#overview)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Database Setup](#database-setup)
- [Keycloak Configuration](#keycloak-configuration)
- [FeignClient Usage](#feignclient-usage)
- [Localization](#localization)
- [Contributing](#contributing)
- [License](#license)

## Overview

This project is a **Spring Boot** application that integrates **Keycloak** for user authentication and authorization,
uses **PostgreSQL** as its database, and implements **FeignClient** for microservice communication. It also supports
localization for internationalization purposes.

## Technologies Used

- **Spring Boot**: Version 3.3.3
- **Keycloak**: Version 25.0.5
- **PostgreSQL**: Version 13 (via Docker)
- **FeignClient**
- **IntelliJ IDEA**: Version 2024.1.4 (Community Edition)
- **JDK 17**

## Prerequisites

Before running this project, ensure you have the following installed:

- Java 17
- Docker
- PostgreSQL 13
- Keycloak 25.0.5
- Maven/Gradle

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/Pando-Soft/Keycloak.git
    cd Keycloak
    ```

2. Set up the environment variables required by the application:
    - **Database URL**
    - **Keycloak server URL**
    - **FeignClient endpoints**

3. Run the following commands to build and start the application:
    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

## Usage

Once the application is running:

- Access the keyclaok admin-cli at `http://localhost:8080`.
- Authenticate via Keycloak for secure access.

### Endpoints

- `/api/auth/login/otp`: to create users and admins and generate otp for users.
- `/api/auth/login/verify-otp`: to verify login and generate access token.
- `/api/user` and `/api/admin`: to check access token work well.

## Database ad Keyclaok Setup

To run PostgreSQL and Keycloak via Docker:

```bash
docker-compose -f src/main/docker/docker-compose.yml up
