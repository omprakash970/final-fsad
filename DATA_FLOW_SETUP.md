# Data Flow Setup - Admin & Analyst Dashboard

## Overview
This document outlines the data flow for the LoanFlow application, specifically how loans and analytics data are fetched and displayed across different user roles (Admin and Analyst).

## Architecture

### Backend API Endpoints
The following endpoints are available and implemented in the Spring Boot backend:

#### Loan Management
- **GET `/api/loans/all`** - Fetch all loans (Admin/Analyst access)
  - Returns: List of all loans with status, borrower, lender, amount
  - Used by: Admin Loans Overview, Analyst Dashboard

- **GET `/api/loans/pending`** - Fetch pending loans for review (Admin only)
  - Returns: List of loans awaiting approval
  - Used by: Admin Dashboard

- **GET `/api/loans/{id}`** - Get specific loan details
  - Returns: Individual loan with full details
  - Used by: Loan detail pages

- **PATCH `/api/loans/{id}/approve`** - Approve a pending loan (Admin only)
  - Action: Updates loan status to APPROVED
  - Used by: Admin Dashboard

- **PATCH `/api/loans/{id}/reject`** - Reject a pending loan (Admin only)
  - Action: Updates loan status to REJECTED
  - Used by: Admin Dashboard

#### Analytics
- **GET `/api/analytics/risk-distribution`** - Risk score distribution by bands (Analyst/Admin)
  - Returns: Array of risk bands with loan counts
  - Used by: Analyst Dashboard, Analyst Analytics

- **GET `/api/analytics/portfolio-exposure`** - Portfolio exposure breakdown (Analyst/Admin)
  - Returns: Array of exposure data by status
  - Used by: Analyst Dashboard, Analyst Analytics

- **GET `/api/analytics/quarterly-trends`** - Quarterly loan trends (Analyst/Admin)
  - Returns: Array of quarterly data with loan counts and volumes
  - Used by: Analyst Trends page

- **GET `/api/analytics/summary`** - Complete analytics summary (Analyst/Admin)
  - Returns: Aggregated analytics data
  - Used by: Export functionality

---

## Frontend Data Flow

### 1. Admin Dashboard
**File:** `src/features/admin/AdminDashboard.jsx`

**Data Fetched:**
- Pending loans via `/api/loans/pending`

**Display Elements:**
- Stats cards showing pending approvals count
- List of pending loans for approval/rejection
- Action buttons to approve or reject loans

**Actions Supported:**
- Approve loan: `PATCH /api/loans/{id}/approve`
- Reject loan: `PATCH /api/loans/{id}/reject`

---

### 2. Admin Loans Overview
**File:** `src/features/admin/LoansOverview.jsx`

**Data Fetched:**
- All loans via `/api/loans/all`

**Display Elements:**
- Summary statistics (Total Loans, Active, Overdue, Total Volume)
- Searchable table of all loans with:
  - Loan ID
  - Borrower Name
  - Lender Name
  - Amount
  - Status (with color-coded badges)

**Features:**
- Full-text search across ID, borrower, and lender
- Dynamic count of filtered results
- Color-coded status indicators

---

### 3. Analyst Dashboard
**File:** `src/features/analyst/AnalystDashboard.jsx`

**Data Fetched:**
- All loans via `/api/loans/all`
- Risk distribution via `/api/analytics/risk-distribution`
- Portfolio exposure via `/api/analytics/portfolio-exposure`

**Display Elements:**
- 6 stat cards showing:
  - Default Probability (calculated from overdue loans)
  - Portfolio Exposure (total volume in millions)
  - Active Loans count
  - Avg Risk Score
  - Pending Review count
  - Total Loans count

- Active Alerts panel (derived from pending/overdue loans)
- Portfolio Risk segments panel (from risk distribution data)

**Data Processing:**
```javascript
// Loan status filtering
const activeLoans = loans.filter(l => l.status === "ACTIVE").length;
const pendingLoans = loans.filter(l => l.status === "PENDING").length;
const overdueLoans = loans.filter(l => l.status === "OVERDUE").length;

// Stat calculations
const totalVolume = loans.reduce((sum, l) => sum + l.amount, 0);
const defaultProbability = (overdueLoans / loans.length) * 100;

// Risk segments mapping
const riskSegmentsData = riskData.map((item, idx) => ({
  label: item.band,
  pct: (item.count / loans.length) * 100,
  count: item.count,
  color: colorMapping[idx]
}));
```

---

### 4. Analyst Analytics
**File:** `src/features/analyst/Analytics.jsx`

**Data Fetched:**
- Risk distribution via `/api/analytics/risk-distribution`
- Portfolio exposure via `/api/analytics/portfolio-exposure`

**Display Elements:**
- Risk Score Distribution bar chart
  - X-axis: Risk bands
  - Y-axis: Loan count
  - Colors: Dynamic based on CHART_COLORS constant

- Portfolio Exposure pie/donut chart
  - Segments: By status (ACTIVE, PENDING, OVERDUE, etc.)
  - Values: Loan amounts in dollars
  - Legend: Status labels

**Features:**
- Export report to JSON file
- Loading and error states
- Responsive tooltips

---

### 5. Analyst Trends
**File:** `src/features/analyst/Trends.jsx`

**Data Fetched:**
- Quarterly trends via `/api/analytics/quarterly-trends`

**Display Elements:**
- 4 KPI cards:
  - Total Loans (Latest Quarter)
  - Total Disbursed (Cumulative)
  - Avg Loans Per Quarter
  - Growth Rate (First → Latest)

- Quarterly Loan Count line chart
  - X-axis: Quarters (Q1 2024 - Q4 2025)
  - Y-axis: Loan count
  - Line: Trend across quarters

- Disbursement Volume line chart
  - X-axis: Quarters
  - Y-axis: Volume in dollars
  - Line: Volume trend over time

**KPI Calculations:**
```javascript
const lastQuarter = data[data.length - 1];
const totalDisbursed = data.reduce((sum, q) => sum + q.volume, 0);
const avgLoans = totalLoans / data.length;
const growthRate = ((lastQuarter.loans - firstQuarter.loans) / firstQuarter.loans) * 100;
```

---

### 6. Analyst Risk Reports
**File:** `src/features/analyst/RiskReports.jsx`

**Data Fetched:**
- All risk reports via `/api/risk-reports/all`

**Display Elements:**
- Summary statistics:
  - Low risk count
  - Medium risk count
  - High risk count
  - Average risk score
  - Average default probability

- Filterable table with:
  - Loan ID
  - Risk Score (with visual bar)
  - Default Probability
  - Status

**Features:**
- Filter by risk level (Low, Medium, High)
- Sort by score or probability
- Color-coded risk indicators

---

## Data Models

### Loan DTO
```javascript
{
  id: number,
  loanId: string,
  borrowerName: string,
  lenderName: string,
  amount: number,
  status: "ACTIVE" | "PENDING" | "OVERDUE" | "REJECTED" | "CLOSED",
  createdAt: timestamp,
  // ... additional fields
}
```

### Risk Band
```javascript
{
  band: string (e.g., "0-20", "21-40"),
  count: number (loans in this band)
}
```

### Portfolio Exposure
```javascript
{
  name: string (status or category),
  value: number (total amount)
}
```

### Quarterly Trend
```javascript
{
  quarter: string (e.g., "Q1 2024"),
  loans: number (count),
  volume: number (total amount)
}
```

---

## API Client Setup

**File:** `src/utils/apiClient.js`

The API client provides:
- Automatic JWT token injection from AuthContext
- Centralized error handling
- Request/response interceptors

### Usage
```javascript
import { apiGet, apiPost, apiPatch } from "../../utils/apiClient";

// Fetch data
const response = await apiGet("/loans/all");
const loans = response?.data || [];

// Update data
await apiPatch(`/loans/${id}/approve`, {});
```

---

## Authentication & Authorization

### Role-Based Access Control
- **Admin**: Can view all loans, approve/reject pending loans, see admin dashboard
- **Analyst**: Can view all loans, access analytics dashboards, generate reports
- **Borrower**: Can only see their own loans
- **Lender**: Can only see loans assigned to them

### Endpoints Protection
All analytics and admin endpoints are protected with `@PreAuthorize` annotations:
- `hasRole('ADMIN')` - Admin-only endpoints
- `hasRole('ANALYST')` - Analyst-only endpoints
- `hasRole('ADMIN') or hasRole('ANALYST')` - Both roles

---

## State Management

All pages use React Hooks for state management:
- `useState` for component state (data, loading, error)
- `useEffect` for fetching data on mount
- `useMemo` for expensive calculations and filtering

### Typical Pattern
```javascript
const [data, setData] = useState([]);
const [loading, setLoading] = useState(true);
const [error, setError] = useState(null);

useEffect(() => {
  const fetch = async () => {
    try {
      setLoading(true);
      const res = await apiGet("/endpoint");
      setData(res?.data || []);
    } catch (err) {
      setError("Error message");
    } finally {
      setLoading(false);
    }
  };
  fetch();
}, []); // Empty dependency array = fetch once on mount
```

---

## UI/UX Considerations

### Loading States
All pages display "Loading..." messages while fetching data

### Error States
All pages display error messages in red if fetching fails

### Empty States
When no data is available, pages show "No data available" messages

### Color Coding
- **Green (#34d399)**: Positive metrics, low risk
- **Blue (#2dd4bf, #818cf8)**: Neutral metrics
- **Orange (#fb923c, #f59e0b)**: Warning metrics, medium risk
- **Red (#f87171, #e11d48)**: Critical metrics, high risk

---

## Testing the Data Flow

### Prerequisites
1. Backend running on port 8082 (or configured in VITE_API_BASE_URL)
2. Database with sample loan data
3. User authenticated with appropriate role

### Testing Steps

1. **Admin View All Loans:**
   - Navigate to Admin > Loans Overview
   - Should display table with all loans
   - Check search functionality

2. **Admin Approve Loan:**
   - Go to Admin Dashboard
   - Find pending loans
   - Click Approve/Reject
   - Check status updates

3. **Analyst View Dashboard:**
   - Navigate to Analyst Dashboard
   - Should show 6 stat cards with calculated metrics
   - Should display active alerts and portfolio risk

4. **Analyst View Analytics:**
   - Navigate to Analyst > Analytics
   - Should display risk distribution bar chart
   - Should display portfolio exposure pie chart

5. **Analyst View Trends:**
   - Navigate to Analyst > Trends
   - Should display KPI cards
   - Should display quarterly trend line charts

---

## Future Enhancements

1. **Real-time Updates**: Implement WebSocket for live dashboard updates
2. **Advanced Filtering**: Add date range, amount range filters
3. **Export Options**: PDF, CSV export in addition to JSON
4. **Caching**: Implement client-side caching to reduce API calls
5. **Pagination**: Large loan lists should be paginated
6. **Sorting**: Advanced sorting options for all tables

---

## Troubleshooting

### No data showing
- Check backend is running
- Verify API endpoint URLs are correct
- Check browser DevTools Network tab for API responses
- Verify user has appropriate role

### API 401 errors
- Check authentication token is valid
- Verify token is being sent in Authorization header
- Re-login if token expired

### API 403 errors
- Verify user has appropriate role
- Check @PreAuthorize annotations on backend endpoints

### Charts not rendering
- Verify data format matches Recharts expectations
- Check data has required properties (dataKey fields)
- Verify CHART_COLORS constant is defined

