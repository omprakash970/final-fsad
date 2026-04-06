# LoanFlow Backend - Quick Start Guide

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

## Setup Instructions

### 1. Database Setup

Create MySQL database:
```sql
CREATE DATABASE loan_management;
```

Update database credentials in `src/main/resources/application.properties` if needed:
```properties
spring.datasource.username=root
spring.datasource.password=Nancy123abc@
spring.datasource.url=jdbc:mysql://localhost:3306/loan_management
```

### 2. Build the Project

```bash
cd loanflow-backend
mvn clean install -DskipTests
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or start the JAR file:
```bash
java -jar target/loanflow-backend-0.0.1-SNAPSHOT.jar
```

The server will start on **http://localhost:8081**

## Verify Installation

### Check API Health
```bash
curl http://localhost:8081/api/auth/register
```

### Login Test
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "rajan@loanflow.com",
    "password": "password123"
  }'
```

## Test Accounts (Pre-initialized)

| Role     | Email                    | Password     |
|----------|--------------------------|--------------|
| Admin    | rajan@loanflow.com       | password123  |
| Admin    | sarah@loanflow.com       | password123  |
| Borrower | arjun@loanflow.com       | password123  |
| Borrower | keiko@loanflow.com       | password123  |
| Lender   | david@capitalfin.com     | password123  |
| Lender   | elena@quicklend.com      | password123  |
| Analyst  | ananya@loanflow.com      | password123  |

## Key API Endpoints

### Authentication
- **POST** `/api/auth/register` - Register new user
- **POST** `/api/auth/login` - Login user

### EMI Schedules (Authenticated)
- **GET** `/api/emi-schedule/{loanId}` - Get EMI schedule for loan
- **GET** `/api/emi-schedule/list/all` - Get all EMI schedules
- **GET** `/api/emi-schedule/view/{id}` - Get specific EMI schedule
- **POST** `/api/emi-schedule/create` - Create EMI schedule
- **PUT** `/api/emi-schedule/{id}/status` - Update EMI status

### User Management (Admin Only)
- **GET** `/api/admin/users/list` - Get paginated users
- **GET** `/api/admin/users/all` - Get all users
- **GET** `/api/admin/users/{id}` - Get user by ID
- **GET** `/api/admin/users/role/{role}` - Get users by role
- **PUT** `/api/admin/users/{id}/status` - Update user status
- **GET** `/api/admin/users/count/total` - Get total user count

## Example Requests

### 1. Register User
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Jane Smith",
    "email": "jane@example.com",
    "password": "securePassword123",
    "role": "BORROWER"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jane@example.com",
    "password": "securePassword123"
  }'
```

Save the returned `token` for authenticated requests.

### 3. Get EMI Schedule (Authenticated)
```bash
TOKEN="your_jwt_token_here"

curl -X GET http://localhost:8081/api/emi-schedule/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 4. Get All Users (Admin Only)
```bash
TOKEN="admin_jwt_token_here"

curl -X GET http://localhost:8081/api/admin/users/all \
  -H "Authorization: Bearer $TOKEN"
```

### 5. Update User Status (Admin Only)
```bash
TOKEN="admin_jwt_token_here"

curl -X PUT http://localhost:8081/api/admin/users/5/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "userId": 5,
    "status": "DISABLED"
  }'
```

## Database Tables

The application automatically creates the following tables:

- `users` - User accounts
- `borrowers` - Borrower profiles
- `lenders` - Lender profiles
- `loans` - Loan accounts
- `emi_schedules` - EMI payment schedules
- `payments` - Payment records
- `risk_reports` - Risk assessments
- `security_logs` - Audit logs

## Configuration Files

### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/loan_management
spring.datasource.username=root
spring.datasource.password=Nancy123abc@

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8081

jwt.secret=your_super_secret_key_which_should_be_long_enough_123456789
jwt.expiration=86400000  # 24 hours
```

### CORS Configuration
- Allowed Origins: `http://localhost:5173`
- Allowed Methods: GET, POST, PUT, DELETE, OPTIONS
- Credentials: Enabled

## Troubleshooting

### Database Connection Error
```
ERROR: Access denied for user 'root'@'localhost'
```
**Solution:** Verify MySQL is running and credentials in `application.properties` are correct.

### Port Already in Use
```
Port 8081 is already in use
```
**Solution:** Change port in `application.properties`:
```properties
server.port=8082
```

### JWT Token Expired
**Solution:** Re-login to get a new token. Tokens expire in 24 hours.

### 403 Forbidden on Admin Endpoints
**Solution:** Ensure you're using an ADMIN user token.

## Development Tips

### Enable SQL Logging
Add to `application.properties`:
```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Useful Maven Commands
```bash
# Compile only
mvn clean compile

# Run tests
mvn test

# Build JAR
mvn clean package

# Skip tests during build
mvn clean install -DskipTests
```

## Architecture

```
loanflow-backend/
├── src/main/java/com/klef/loanflowbackend/
│   ├── controller/       # REST API endpoints
│   ├── service/          # Business logic
│   ├── repository/       # Database access
│   ├── entity/           # JPA entities
│   ├── dto/              # Data transfer objects
│   ├── config/           # Configuration classes
│   └── security/         # JWT & authentication
├── src/main/resources/
│   └── application.properties  # Application configuration
└── pom.xml               # Maven dependencies
```

## Support

For API documentation, see `API_DOCUMENTATION.md` in the project root.

## Next Steps

1. **Frontend Integration**: Connect React frontend to these API endpoints
2. **Additional Features**: Add payment processing, loan approval workflow
3. **Advanced Security**: Add rate limiting, API key management
4. **Monitoring**: Add logging and monitoring solutions
5. **Testing**: Implement comprehensive unit and integration tests

---

**Backend Status**: ✅ Ready for Development

Last Updated: April 5, 2026
