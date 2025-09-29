# Quiz Application

A comprehensive Spring Boot REST API for managing quizzes with JWT authentication and role-based access control.

## Features

- **User Authentication**: JWT-based authentication system
- **Role-based Access**: Admin and User roles with different permissions
- **Quiz Management**: Create, update, delete quizzes (Admin only)
- **Question Management**: Add questions with multiple choice options
- **Quiz Taking**: Users can take quizzes with attempt limits
- **Score Calculation**: Automatic scoring based on correct answers
- **Analytics**: Admin dashboard with statistics
- **Database**: PostgreSQL with Liquibase migrations

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Security** with JWT
- **Spring Data JPA**
- **PostgreSQL**
- **Liquibase** for database migrations
- **OpenAPI 3** documentation
- **Lombok** for reducing boilerplate code

## API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - User login
- `POST /auth/register-admin` - Register new admin user (Admin only)

### Public Quiz Access
- `GET /quizzes` - List active quizzes
- `GET /quizzes/{id}` - Get quiz details

### User Quiz Taking (Authenticated)
- `GET /quizzes/{id}/attempts` - Get user attempts count
- `POST /quizzes/{id}/start` - Start quiz attempt
- `POST /quizzes/{id}/submit` - Submit quiz answers
- `GET /users/{userId}/results` - Get user results
- `GET /takes/{takeId}` - Get specific attempt details

### Admin Quiz Management
- `GET /admin/quizzes` - List all quizzes
- `POST /admin/quizzes` - Create new quiz
- `PUT /admin/quizzes/{id}` - Update quiz
- `DELETE /admin/quizzes/{id}` - Delete quiz

### Admin Question Management
- `GET /admin/quizzes/{id}/questions` - List quiz questions
- `POST /admin/quizzes/{id}/questions` - Add question
- `PUT /admin/questions/{id}` - Update question
- `DELETE /admin/questions/{id}` - Delete question

### Admin Option Management
- `POST /admin/questions/{id}/options` - Add options to question
- `PUT /admin/options/{id}` - Update option

### Admin User Management
- `GET /admin/users` - List all users
- `GET /admin/users/{id}` - Get user details
- `DELETE /admin/users/{id}` - Delete user

### Admin Analytics
- `GET /admin/stats` - Get application statistics

## Database Schema

The application uses the following main entities:

1. **Users** - System users with roles (ADMIN/USER)
2. **Quizzes** - Quiz definitions with settings
3. **Questions** - Questions belonging to quizzes
4. **Options** - Multiple choice options for questions
5. **Takes** - User quiz attempts with scores
6. **Responses** - User answers for each question

## Setup and Installation

### Prerequisites
- Java 17 or higher
- PostgreSQL database
- Maven or Gradle

### Database Setup
1. Create a PostgreSQL database named `development`
2. Update database credentials in `application-development.yml`
3. The application will automatically create tables using Liquibase migrations

### Running the Application
```bash
./gradlew bootRun
```

### Default Admin User
- Username: `admin`
- Password: `secret`

### Adding More Admin Users

#### Method 1: Via Database Migration
Add a new changeset to `src/main/resources/db/changelog/changelog-1.0.yml`:

```yaml
  - changeSet:
      id: add-new-admin-user
      author: admin
      changes:
        - insert:
            tableName: users
            columns:
              - column:
                  name: username
                  value: "newadmin"
              - column:
                  name: email
                  value: "newadmin@example.com"
              - column:
                  name: password
                  value: "$2a$10$your_bcrypt_hashed_password_here"
              - column:
                  name: role
                  value: "ADMIN"
```

#### Method 2: Via Admin Registration API
Use the special admin registration endpoint:

```bash
curl -X POST http://localhost:8080/auth/register-admin \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "username": "newadmin",
    "email": "newadmin@example.com",
    "password": "securepassword"
  }'
```

## API Documentation

Once the application is running, you can access:
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## Configuration

### JWT Configuration
- Token expiration: 24 hours (configurable in `application.yml`)
- Secret key: Automatically generated using HS256

### Database Configuration
Located in `application-development.yml`:
- URL: `jdbc:postgresql://localhost:5432/development`
- Username: `user`
- Password: `yesh@6289`

## Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-based Authorization**: Different access levels for admin and users
- **Password Encryption**: BCrypt password hashing
- **CORS Support**: Configurable cross-origin resource sharing

## Testing

The application includes comprehensive error handling and validation:
- Input validation using Bean Validation
- Global exception handling
- Proper HTTP status codes
- Detailed error messages

## Future Enhancements

- Quiz categories and tags
- Question types (multiple choice, true/false, essay)
- Quiz time limits
- Result analytics and reporting
- Email notifications
- Bulk question import/export