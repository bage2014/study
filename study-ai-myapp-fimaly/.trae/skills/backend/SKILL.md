---
name: "backend"
description: "Provides guidance for backend development using Spring Boot, including API design, database integration, and security implementation. Invoke when working on backend-related tasks or when needing backend architecture advice."
---

# Backend Development

This skill provides comprehensive guidance for developing the backend of the Family Tree App using Spring Boot.

## Core Technologies

- **Spring Boot 2.5+**
- **MySQL 8.0+**
- **Spring Security**
- **JWT Authentication**
- **Spring Data JPA**

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/familytree/
│   │   │   ├── controller/      # API controllers
│   │   │   ├── service/         # Business logic
│   │   │   ├── repository/      # Data access
│   │   │   ├── model/           # Data models
│   │   │   ├── dto/             # Data transfer objects
│   │   │   ├── config/          # Configuration
│   │   │   ├── security/        # Security configuration
│   │   │   └── utils/           # Utility classes
│   │   └── resources/           # Application resources
│   └── test/                    # Test code
├── pom.xml                      # Maven configuration
└── application.properties       # Application properties
```

## Key Features

### User Authentication
- JWT token generation and validation
- Password encryption
- Role-based access control

### Family Management
- Create and manage families
- Invite members to families
- Family information management

### Member Management
- Add, edit, and delete family members
- Store member details (name, gender, birth date, etc.)
- Manage member relationships

### Family Tree
- Generate family tree data
- Relationship analysis
- Tree structure optimization

### History Records
- Create and manage family events
- Timeline-based event display
- Event categorization

### Media Management
- Upload and store family photos
- Media organization and retrieval
- Access control for media files

## API Design

### RESTful API Principles
- Use HTTP methods appropriately (GET, POST, PUT, DELETE)
- Consistent URL structure
- Standardized response format

### Example Endpoints
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/families` - Get user's families
- `POST /api/families` - Create new family
- `GET /api/families/{id}/members` - Get family members
- `POST /api/families/{id}/members` - Add family member
- `GET /api/families/{id}/tree` - Get family tree data

## Database Design

### Core Tables
- **users** - User information
- **families** - Family details
- **members** - Family members
- **relationships** - Member relationships
- **events** - Family events
- **media** - Media files
- **permissions** - User permissions

## Security Best Practices
- Use HTTPS for all API requests
- Implement rate limiting
- Validate all user input
- Use parameterized queries to prevent SQL injection
- Encrypt sensitive data
- Implement proper error handling

## Testing Strategy
- Unit tests for service layer
- Integration tests for API endpoints
- Database tests
- Security tests

## Deployment
- Containerization with Docker
- CI/CD pipeline with GitHub Actions
- Environment-specific configurations
