# Library Management System(Backend)

This is a Spring Boot application for managing a library, using a PostgreSQL database for data storage.

## Features

- Book management (add, edit, delete, search)
- User management (add, edit, delete, search)
- Transaction management (borrow, return, track overdue books)

## Technologies Used

- Java 17
- Spring Boot 3.3.2
- PostgreSQL 16
- Spring Data JPA
- Gradle 8.8
- Liquibase (for database versioning)
- Swagger UI (for API documentation)

## Authentication and Authorization

This application implements a robust role-based authorization system to ensure secure access to various features:

- **User Roles**:

    - Admin: Can manage books, members, and transactions.
    - Member: Can view books and manage their own transactions.

- **Access Control**:

    - Certain routes and actions are restricted based on user roles.

- **JWT Authentication**:

    - Uses JSON Web Tokens for secure authentication.
    - Tokens are stored in HTTP-only secure cookies, enhancing security against XSS attacks.
    - Token expiration time: **10 Minutes**
    - Automatic token refresh mechanism to maintain user sessions.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 17 or later
- Gradle 8.8 or later
- PostgreSQL 16

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/hariananthaa/library_mgmt_backend
```
2. Navigate to the project directory:
```bash
cd library_management_backend
```

3. Configure the database connection in `src/main/resources/application.yml`:
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/library_mgmt
spring.datasource.username=your_username
spring.datasource.password=your_password 
```
4. Build the project:
```bash
./gradlew build
```
5. Run the application:
```bash
./gradlew bootRun 
```
The application will be available at `http://localhost:8081`

## Database Versioning

This project uses Liquibase for database versioning. The changelog files are located in `src/main/resources/db/changelog/`.

To apply database changes:
```bash
./gradlew update
```

## API Documentation

- API documentation is available via Swagger UI at http://localhost:8081/swagger-ui when the application is running.
- You can also access the API specification in JSON format at http://localhost:8081/v3/api-docs.
- By default, all the endpoints are restricted except http://localhost:8081/api/v1/auth/authenticate.
- By using above endpoint, you can get the JWT token to access the other APIs.

## Credentials
Admin Credential:
- email: admin@zit.com
- password: test@123
- You can add members via this admin credential.

## Database Structure
[https://dbdiagram.io/d/library_mgmt_system-66b9968c8b4bb5230ed5d0c0](https://dbdiagram.io/d/library_mgmt_system-66b9968c8b4bb5230ed5d0c0)

## Deployment

This application is hosted on Render, a cloud platform for static sites and Serverless Functions.

### Important Note on Inactivity

Due to the nature of Render's free tier hosting:

- The application may become inactive after periods of no usage.
- The first request after an inactive period may take longer to load **(usually 5-10 minutes)** as the server spins up.
- Subsequent requests will be faster once the server is active.

If you experience a delay when first accessing the application, please be patient. This is normal behavior for applications hosted on Render's free tier that haven't been accessed recently.

### Live Demo

You can access the live version of this application at: https://library-mgmt-backend-latest.onrender.com/swagger-ui

Please note that if you encounter any issues or the application seems unresponsive, you can try refreshing the page after a short wait. If problems persist, please reach out for support.

- Email: khariharan561@gmail.com

## Sample Endpoints
1. Authenticate
```bash
POST /api/v1/auth/authenticate 
```
- Request Body:
```bash
{
  "email": "your_email",
  "password": "your_password"
}
```
- Response:
```bash
{
  "token": "your_jwt_token"
}
```

## Usage
Once the application is running, you can interact with it through the Swagger UI or via API clients like Postman using the JWT token for authentication.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments
- Thank you to the open-source community for providing various libraries and tools that made this project possible.


