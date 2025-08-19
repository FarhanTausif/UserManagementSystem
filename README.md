# User Management System

## Project Overview

The User Management System is a Spring Boot application designed to manage users and roles through a RESTful API. It follows Clean Architecture principles, separating the application into layers (domain, application, infrastructure, and config). The system allows creating users and roles, assigning roles to users, and retrieving user details.

### Features
- Create a user with a name and email.
- Create a role with a role name.
- Assign a role to a user.
- Retrieve user details, including assigned roles.
- In-memory H2 database for persistence during runtime. 
- Unit and integration tests for the application.

## Project Structure

```
/UserManagementSystem
├── src/
│   ├── main/
│   │   ├── java/org/example/usermanagement/
│   │   │   ├── application/
│   │   │   │   ├── interfaces/
│   │   │   │   │   ├── RoleRepository.java             # Interface for role data access
│   │   │   │   │   ├── UserRepository.java             # Interface for user data access
│   │   │   │   ├── RoleService.java                    # Business logic for role operations
│   │   │   │   └── UserService.java                    # Business logic for user operations
│   │   │   ├── config/
│   │   │   │   ├── BeanConfig.java                     # Spring bean definitions
│   │   │   │   └── OpenApiConfig.java                  # Swagger/OpenAPI config
│   │   │   ├── domain/
│   │   │   │   ├── Role.java                           # Domain model for Role
│   │   │   │   └── User.java                           # Domain model for User
│   │   │   ├── infrastructure/
│   │   │   │   ├── controller/
│   │   │   │   │   ├── GlobalExceptionHandler.java     # Global error handling
│   │   │   │   │   ├── RoleController.java             # REST controller for roles
│   │   │   │   │   ├── UserController.java             # REST controller for users
│   │   │   │   │   └── dto/                            # DTOs for requests/responses
│   │   │   │   └── persistence/
│   │   │   │       ├── RoleJpaEntity.java              # JPA entity for Role
│   │   │   │       ├── RoleJpaRepository.java          # Custom JPA impl for Role
│   │   │   │       ├── RoleSpringDataRepository.java   # Spring Data repository for Role
│   │   │   │       ├── UserJpaEntity.java              # JPA entity for User
│   │   │   │       ├── UserJpaRepository.java          # Custom JPA impl for User
│   │   │   │       └── UserSpringDataRepository.java   # Spring Data repository for User
│   │   │   └── UserManagementApplication.java          # Spring Boot main application
│   │   └── resources/
│   │       └── application.properties                  # Spring Boot configuration
│   └── test/java/org/example/usermanagement/
│       ├── application/
│       │   ├── RoleServiceTest.java                    # Unit test for RoleService
│       │   └── UserServiceTest.java                    # Unit test for UserService
│       ├── integration/
│       │   └── UserManagementIntegrationTest.java      # Integration test for endpoints
│       └── UserManagementApplicationTests.java         # Application context test
├── target/                                             # Maven build output (ignored)
├── README.md                                           # Project documentation
├── api-tests.http                                      # Test REST API endpoints
├── mvnw                                                # Maven wrapper (Unix)
├── mvnw.cmd                                            # Maven wrapper (Windows)
└── pom.xml                                             # Maven project descriptor
```

## Prerequisites

- **Java**: JDK 17 (or newer).
- **Maven**: 3.6.3 or higher.
- **IntelliJ IDEA**: Recommended for development (or any IDE supporting Spring Boot).
- **Postman**: For testing the REST API (download from https://www.postman.com/downloads/).

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/FarhanTausif/UserManagementSystem.git
   cd UserManagementSystem
   ```

2. **Import the Project into IntelliJ IDEA**:
    - Open IntelliJ IDEA.
    - Select `File > Open` and choose the `UserManagementSystem` directory.
    - IntelliJ will automatically detect the `pom.xml` file and set up the project.

3. **Verify Dependencies**:
    - Ensure the `pom.xml` includes necessary dependencies:
      ```xml
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-data-jpa</artifactId>
      </dependency>
      <dependency>
          <groupId>com.h2database</groupId>
          <artifactId>h2</artifactId>
          <scope>runtime</scope>
      </dependency>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-validation</artifactId>
      </dependency>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-test</artifactId>
          <scope>test</scope>
      </dependency>
      ```

4. **Run the Application**:
    - Right-click `UserManagementApplication.java` (in `src/main/java/org/example/usermanagement`) and select `Run 'UserManagementApplication'`.
    - Or run from terminal:
      ```bash
      ./mvnw spring-boot:run
      ```
    - The application will start on `http://localhost:8080`.

5. **Access the H2 Console** (Optional):
    - Open `http://localhost:8080/h2-console`.
    - Use the following settings:
        - **JDBC URL**: `jdbc:h2:mem:usermanagement`
        - **Username**: `tausif`
        - **Password**: (leave blank)
    - Click `Connect` to view the database.

## API Endpoints

The application exposes the following REST API endpoints:

| Method | Endpoint                                 | Description                                 | Request Body Example                                                                                   | Response Example                                                                                                        |
|--------|------------------------------------------|---------------------------------------------|--------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| POST   | /users                                   | Create a new user                           | `{ "name": "Alice", "email": "alice@example.com" }`                                                   | `UUID` (e.g., `"b8fdacb7-5f95-4d7e-8e23-3cd54e6e1d45"`)                                                               |
| GET    | /users                                   | Get all users (paginated)                   | _N/A_                                                                                                 | `{ "content": [ { "id": "...", "name": "...", "email": "...", "roles": [...], "createdDate": "...", "updatedDate": "..." }, ... ], ... }` |
| GET    | /users/{id}                              | Get user details by user ID                 | _N/A_                                                                                                 | `{ "id": "...", "name": "...", "email": "...", "roles": [ { "id": "...", "roleName": "...", "createdDate": "...", "updatedDate": "..." } ], "createdDate": "...", "updatedDate": "..." }` |
| POST   | /users/{userId}/assign-role/{roleId}     | Assign a role to a user                     | _N/A_                                                                                                 | `"Role assigned successfully"`                                                                                          |
| DELETE | /users/{userId}/remove-role/{roleId}     | Remove a role from a user                   | _N/A_                                                                                                 | `"Role removed successfully"`                                                                                           |
| POST   | /roles                                   | Create a new role                           | `{ "roleName": "ADMIN" }`                                                                             | `UUID` (e.g., `"b5eedf1a-8a75-49a5-8c43-2f7a2c6a5b1b"`)                                                                |

**Notes:**
- All request and response bodies are in JSON format.
- For paginated `GET /users`, see [Spring Data pagination](https://docs.spring.io/spring-data/commons/docs/current/reference/html/#repositories.paging-and-sorting) for details on request parameters and the structure of the response.
- UUIDs are universally unique identifiers.

**Example DTOs:**

- **CreateUserRequestDTO**
    ```json
    {
      "name": "Alice",
      "email": "alice@example.com"
    }
    ```
- **CreateRoleRequestDTO**
    ```json
    {
      "roleName": "ADMIN"
    }
    ```
- **UserResponseDTO**
    ```json
    {
      "id": "b8fdacb7-5f95-4d7e-8e23-3cd54e6e1d45",
      "name": "Alice",
      "email": "alice@example.com",
      "roles": [
        {
          "id": "b5eedf1a-8a75-49a5-8c43-2f7a2c6a5b1b",
          "roleName": "ADMIN",
          "createdDate": "2025-08-19T12:34:56",
          "updatedDate": "2025-08-19T12:34:56"
        }
      ],
      "createdDate": "2025-08-19T12:34:56",
      "updatedDate": "2025-08-19T12:34:56"
    }
    ```
- **RoleResponseDTO**
    ```json
    {
      "id": "b5eedf1a-8a75-49a5-8c43-2f7a2c6a5b1b",
      "roleName": "ADMIN",
      "createdDate": "2025-08-19T12:34:56",
      "updatedDate": "2025-08-19T12:34:56"
    }
    ```

## Example API Usage with Postman

1. **Download and Install Postman**:
    - Download Postman from https://www.postman.com/downloads/ and install it.

2. **Create a Role**:
    - Open Postman.
    - Set the HTTP method to `POST`.
    - Enter the URL: `http://localhost:8080/roles`.
    - Go to the `Body` tab, select `raw`, and choose `JSON` from the dropdown.
    - Enter the following JSON:
      ```json
      {"roleName":"ADMIN"}
      ```
    - Click `Send`.
    - Copy the returned `roleId` (e.g., `"550e8400-e29b-41d4-a716-446655440000"`).

3. **Create a User**:
    - Set the HTTP method to `POST`.
    - Enter the URL: `http://localhost:8080/users`.
    - In the `Body` tab, select `raw` and `JSON`.
    - Enter the following JSON:
      ```json
      {"name":"John Doe","email":"john@example.com"}
      ```
    - Click `Send`.
    - Copy the returned `userId` (e.g., `"550e8400-e29b-41d4-a716-446655440001"`).

4. **Assign Role to User**:
    - Set the HTTP method to `POST`.
    - Enter the URL with the `userId` and `roleId` from the previous steps: `http://localhost:8080/users/<userId>/assign-role/<roleId>` (e.g., `http://localhost:8080/users/550e8400-e29b-41d4-a716-446655440001/assign-role/550e8400-e29b-41d4-a716-446655440000`).
    - No request body is needed.
    - Click `Send`.
    - Verify the response: `"Role assigned successfully"`.

5. **Get User Details**:
    - Set the HTTP method to `GET`.
    - Enter the URL with the `userId`: `http://localhost:8080/users/<userId>` (e.g., `http://localhost:8080/users/550e8400-e29b-41d4-a716-446655440001`).
    - Click `Send`.
    - Verify the response, e.g.:
      ```json
      {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "name": "John Doe",
          "email": "john@example.com",
          "roles": [
              {
                  "id": "550e8400-e29b-41d4-a716-446655440000",
                  "roleName": "ADMIN"
              }
          ]
      }
      ```

## Running Tests

- **Unit and Integration Tests**:
    - Right-click the `application` and `integration` packages under `src/test/java/org/example/usermanagement` and select `Run Tests`.
    - Tests include `UserServiceTest`, `RoleServiceTest`, and `UserManagementIntegrationTest`.
