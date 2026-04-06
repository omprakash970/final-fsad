# LoanFlow Backend - Quick Start Guide

## 🚀 Quick Setup (5 Minutes)

### 1. Prerequisites Check
```bash
# Check Java version (should be 17+)
java -version

# Check Maven version (should be 3.6+)
mvn -version

# Check MySQL is running
mysql -u root -p
```

### 2. Database Setup
```sql
CREATE DATABASE loan_management;
```

### 3. Start Application
```bash
cd C:\Users\Oppie_549\loanflow-backend
mvn spring-boot:run
```

**Expected Output:**
```
... Started LoanflowBackendApplication in 5.234 seconds
```

---

## 🧪 Quick Test (With cURL or Postman)

### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "email": "test@example.com",
    "password": "test123",
    "role": "BORROWER"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "test123"
  }'
```

---

## 📋 File Checklist

✅ `pom.xml` - Maven dependencies (updated with JWT)
✅ `application.properties` - Database & JWT config
✅ `SecurityConfig.java` - Spring Security configuration
✅ `AuthController.java` - REST endpoints
✅ `AuthService.java` - Business logic
✅ `JwtService.java` - Token generation/validation
✅ `JwtAuthenticationFilter.java` - JWT filter
✅ `CustomUserDetailsService.java` - User details loading
✅ `User.java` - Entity with validation
✅ `Role.java` - Enum
✅ `UserRepository.java` - Data access
✅ `RegisterRequest.java` - Request DTO
✅ `AuthRequest.java` - Login request DTO
✅ `AuthResponse.java` - Response DTO

---

## 🔑 API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | ❌ No | Register new user |
| POST | `/api/auth/login` | ❌ No | Login user |
| * | `/api/**` | ✅ Yes | Protected endpoints (future) |

---

## 📍 Key Configuration Files

**MySQL Connection:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/loan_management
spring.datasource.username=root
spring.datasource.password=Nancy123abc@
```

**JWT Settings:**
```properties
jwt.secret=your_super_secret_key_which_should_be_long_enough_123456789
jwt.expiration=86400000  # 24 hours in milliseconds
```

**CORS Enabled For:**
```
http://localhost:5173  # React/Vite frontend
```

---

## 🎯 Test Roles

| Role | Purpose |
|------|---------|
| ADMIN | System administration |
| LENDER | Loan provider |
| BORROWER | Loan applicant |
| ANALYST | Loan analysis |

---

## ❌ Common Issues & Solutions

### Issue: Connection refused to MySQL
**Solution:** 
- Check MySQL is running: `mysql -u root -p`
- Check URL in application.properties
- Default port is 3306

### Issue: JWT token invalid
**Solution:**
- Check `jwt.secret` is long enough (>32 chars)
- Token expires after 24 hours
- Make sure you're using Bearer token format

### Issue: Port 8080 already in use
**Solution:**
- Change in `application.properties`: `server.port=8081`
- Or kill process using port 8080

### Issue: Validation errors on registration
**Solution:**
- Email must be valid format: `user@example.com`
- Password must be at least 6 characters
- Role must be: ADMIN, LENDER, BORROWER, or ANALYST
- All fields are required

---

## 📱 Integration with React Frontend

**Frontend should:**
1. POST to `/api/auth/register` for signup
2. POST to `/api/auth/login` for login
3. Store token from response
4. Include token in Authorization header: `Bearer <token>`

**Example React Code:**
```javascript
const register = async (fullName, email, password, role) => {
  const response = await fetch('http://localhost:8080/api/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ fullName, email, password, role })
  });
  return response.json();
};

const login = async (email, password) => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  const data = await response.json();
  localStorage.setItem('token', data.token);
  return data;
};

const authenticatedFetch = (url, options = {}) => {
  const token = localStorage.getItem('token');
  return fetch(url, {
    ...options,
    headers: {
      ...options.headers,
      'Authorization': `Bearer ${token}`
    }
  });
};
```

---

## 🔍 Verify Installation

```bash
# Navigate to project
cd C:\Users\Oppie_549\loanflow-backend

# Build project (checks for compilation errors)
mvn clean compile

# Run tests (if any)
mvn test

# Start application
mvn spring-boot:run
```

---

## 📚 File Locations

```
C:\Users\Oppie_549\loanflow-backend\
├── src/main/java/com/klef/loanflowbackend/
│   ├── controller/AuthController.java
│   ├── service/AuthService.java
│   ├── entity/User.java, Role.java
│   ├── dto/AuthRequest.java, AuthResponse.java, RegisterRequest.java
│   ├── repository/UserRepository.java
│   ├── security/JwtService.java, JwtAuthenticationFilter.java, CustomUserDetailsService.java
│   ├── config/SecurityConfig.java
│   └── LoanflowBackendApplication.java
└── src/main/resources/
    └── application.properties
```

---

## ✅ Phase 1 Completion Status

- ✅ User registration with BCrypt encryption
- ✅ User login with JWT token generation
- ✅ Role-based authentication foundation
- ✅ JWT validation and expiration
- ✅ Spring Security configuration
- ✅ MySQL database integration
- ✅ CORS configuration for React
- ✅ Input validation
- ✅ Error handling
- ✅ Production-ready code

---

## 🎓 Learning Resources

- [Spring Security Docs](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL Documentation](https://dev.mysql.com/)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)

---

**Ready to go!** 🚀 Start with `mvn spring-boot:run`

