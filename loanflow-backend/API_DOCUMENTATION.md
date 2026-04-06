# LoanFlow Backend - API Documentation

## Base URL
```
http://localhost:8081/api
```

## 1. Authentication Endpoints

### Register User
**POST** `/auth/register`

Register a new user with specified role (ADMIN, LENDER, BORROWER, ANALYST)

**Request Body:**
```json
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123",
  "role": "BORROWER"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "role": "BORROWER",
    "email": "john@example.com",
    "fullName": "John Doe"
  }
}
```

**Roles:**
- `ADMIN` - Platform administration
- `LENDER` - Create and manage loan offers
- `BORROWER` - Apply for and manage loans
- `ANALYST` - View analytics and risk reports

---

### Login User
**POST** `/auth/login`

Authenticate user and receive JWT token

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "role": "BORROWER",
    "email": "john@example.com",
    "fullName": "John Doe"
  }
}
```

---

## 2. EMI Schedule Endpoints

All EMI endpoints require authentication (Bearer token in Authorization header)

### Get EMI Schedule by Loan ID
**GET** `/emi-schedule/{loanId}`

Get complete EMI schedule for a specific loan

**Path Parameters:**
- `loanId` (Long) - ID of the loan

**Response (200 OK):**
```json
{
  "success": true,
  "message": "EMI schedules retrieved successfully",
  "data": [
    {
      "id": 1,
      "loanId": 100,
      "month": 1,
      "emiAmount": 1320.00,
      "principal": 920.00,
      "interest": 400.00,
      "balance": 41580.00,
      "status": "PAID",
      "createdAt": 1712000000000
    },
    {
      "id": 2,
      "loanId": 100,
      "month": 2,
      "emiAmount": 1320.00,
      "principal": 929.00,
      "interest": 391.00,
      "balance": 40651.00,
      "status": "UPCOMING",
      "createdAt": 1712000000000
    }
  ]
}
```

**Status Values:** `PAID`, `UPCOMING`, `PENDING`, `FAILED`, `COMPLETED`

---

### Get All EMI Schedules
**GET** `/emi-schedule/list/all`

Retrieve all EMI schedules in the system

**Response (200 OK):**
```json
{
  "success": true,
  "message": "All EMI schedules retrieved successfully",
  "data": [...]
}
```

---

### Get EMI Schedule by ID
**GET** `/emi-schedule/view/{id}`

Get a specific EMI schedule record

**Path Parameters:**
- `id` (Long) - ID of the EMI schedule

**Response (200 OK):**
```json
{
  "success": true,
  "message": "EMI schedule retrieved successfully",
  "data": {
    "id": 1,
    "loanId": 100,
    "month": 1,
    ...
  }
}
```

---

### Create EMI Schedule
**POST** `/emi-schedule/create`

Create a new EMI schedule record

**Request Body:**
```json
{
  "loanId": 100,
  "month": 1,
  "emiAmount": 1320.00,
  "principal": 920.00,
  "interest": 400.00,
  "balance": 41580.00,
  "status": "PENDING"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "EMI schedule created successfully",
  "data": {...}
}
```

---

### Update EMI Schedule Status
**PUT** `/emi-schedule/{id}/status?status=COMPLETED`

Update the payment status of an EMI

**Path Parameters:**
- `id` (Long) - ID of the EMI schedule

**Query Parameters:**
- `status` (String) - New status (PENDING, COMPLETED, FAILED, CANCELLED, UPCOMING)

**Response (200 OK):**
```json
{
  "success": true,
  "message": "EMI schedule status updated successfully",
  "data": {...}
}
```

---

## 3. User Management Endpoints (Admin Only)

⚠️ **All endpoints in this section require ADMIN role**

### Get All Users
**GET** `/admin/users/list?page=0&size=10`

Get paginated list of all users

**Query Parameters:**
- `page` (int, default: 0) - Page number
- `size` (int, default: 10) - Items per page

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Users retrieved successfully",
  "data": [
    {
      "id": 1,
      "fullName": "Rajan Kapoor",
      "email": "rajan@loanflow.com",
      "role": "ADMIN",
      "status": "ACTIVE",
      "createdAt": 1712000000000,
      "updatedAt": 1712000000000
    },
    {
      "id": 2,
      "fullName": "John Doe",
      "email": "john@loanflow.com",
      "role": "BORROWER",
      "status": "ACTIVE",
      "createdAt": 1712000000000,
      "updatedAt": 1712000000000
    }
  ]
}
```

---

### Get All Users (Without Pagination)
**GET** `/admin/users/all`

Get list of all users without pagination

**Response (200 OK):**
```json
{
  "success": true,
  "message": "All users retrieved successfully",
  "data": [...]
}
```

---

### Get User by ID
**GET** `/admin/users/{id}`

Get details of a specific user

**Path Parameters:**
- `id` (Long) - User ID

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": 1,
    "fullName": "Rajan Kapoor",
    "email": "rajan@loanflow.com",
    "role": "ADMIN",
    "status": "ACTIVE",
    "createdAt": 1712000000000,
    "updatedAt": 1712000000000
  }
}
```

---

### Get Users by Role
**GET** `/admin/users/role/{role}`

Get all users with a specific role

**Path Parameters:**
- `role` (String) - Role type (ADMIN, LENDER, BORROWER, ANALYST)

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Users retrieved successfully by role",
  "data": [...]
}
```

---

### Update User Status
**PUT** `/admin/users/{id}/status`

Enable or disable a user account

**Path Parameters:**
- `id` (Long) - User ID

**Request Body:**
```json
{
  "userId": 5,
  "status": "DISABLED"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User status updated successfully",
  "data": {...}
}
```

**Status Values:** `ACTIVE`, `DISABLED`

---

### Get Total User Count
**GET** `/admin/users/count/total`

Get total number of users in the system

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Total user count retrieved",
  "data": 15
}
```

---

## 4. Authentication Headers

All authenticated endpoints require the JWT token in the Authorization header:

```
Authorization: Bearer {token}
```

**Example:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGxvYW5mbG93LmNvbSIsImlhdCI6MTcxMjAwMDAwMCwiZXhwIjoxNzEyMDg2NDAwfQ...
```

---

## 5. Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "message": "Error",
  "error": "Invalid request format or parameters",
  "timestamp": 1712000000000
}
```

### 401 Unauthorized
```json
{
  "success": false,
  "message": "Login failed",
  "error": "Invalid credentials",
  "timestamp": 1712000000000
}
```

### 403 Forbidden (Admin Only Endpoint)
```json
{
  "success": false,
  "message": "Access Denied",
  "error": "You do not have permission to access this resource",
  "timestamp": 1712000000000
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Error",
  "error": "User not found with ID: 999",
  "timestamp": 1712000000000
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "message": "Error",
  "error": "An unexpected error occurred",
  "timestamp": 1712000000000
}
```

---

## 6. Sample Test Credentials

**Admin User:**
- Email: `rajan@loanflow.com`
- Password: `password123`
- Role: ADMIN

**Borrower User:**
- Email: `arjun@loanflow.com`
- Password: `password123`
- Role: BORROWER

**Lender User:**
- Email: `david@capitalfin.com`
- Password: `password123`
- Role: LENDER

**Analyst User:**
- Email: `ananya@loanflow.com`
- Password: `password123`
- Role: ANALYST

---

## 7. Running the Backend

### Start the server:
```bash
cd loanflow-backend
mvn spring-boot:run
```

The server will start on `http://localhost:8081`

### Database Setup:
- Create MySQL database: `loan_management`
- Username: `root`
- Password: `Nancy123abc@`
- Host: `localhost:3306`

Sample data will be automatically initialized on first startup.

---

## 8. CORS Configuration

The backend is configured to accept requests from:
- `http://localhost:5173` (Frontend development server)

Allowed HTTP methods: GET, POST, PUT, DELETE, OPTIONS

---

## 9. Security Notes

- Passwords are encrypted using BCrypt
- JWT tokens expire in 24 hours (86400000 ms)
- Role-based access control is enforced on admin endpoints
- CSRF protection is disabled for stateless JWT authentication
- All endpoints (except auth) require valid JWT token
