# User and Group Management API

This project provides a RESTful API for managing users and user groups, built using Spring Boot.

## Features
- Manage users and groups
- Associate users with groups
- H2 in-memory database for persistence

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/esa-kian/surveypal
   cd surveypal

2. Ensure you have JDK 17+ and Maven installed.

3. Build and run the application using Maven:
    ```bash
    mvn spring-boot:run
    ```

3. Access the API at `http://localhost:8080`

## API Endpoints

### Users

- `GET /api/users`: Fetch all users
- `GET /api/users/{id}`: Get user by ID
- `POST /api/users`: Create new user
- `DELETE /api/users/{id}`: Delete user

### Groups

- `POST /api/groups`: Create new group
- `POST /api/groups/{groupId}/users/{userId}`: Add user to group
- `DELETE /api/groups/{groupId}/users/{userId}`: Remove user from group

## Swagger UI

Swagger API documentation is available at `http://localhost:8080/swagger-ui.html`


## Instructions for Docker Usage
1. **Build the Docker Image:**
```
docker build -t user-management-rest .
# for ARM architecture (like M1/M2 Macs): 
docker build --platform linux/amd64 -t user-management-rest .
```

2. **Run the Container:**
```
docker run -p 8000:8080 user-management-rest
```
3. **Stop the Container:**
```
docker stop user-management-rest
```

## Running Tests
To run unit tests for the application, run:

```bash
mvn test
```

## License

This project is open-source and available under the MIT License.

