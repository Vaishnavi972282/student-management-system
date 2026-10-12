# Quality Assurance & Test Execution Report
**QA Engineer:** Member 5  
**Execution Date:** Verified Live on Maven 3.9 & Java 24

---

## 1. Automated Test Results (Executed via `mvn test`)

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.devops.studentmanagement.StudentManagementApplicationTests
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 11.20 s
[INFO] BUILD SUCCESS
```

---

## 2. Test Cases Specification & Verification Matrix

| Test Case ID | Module | Precondition | Input / Action | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-SEC-01** | Security | User unauthenticated | `GET /dashboard` | HTTP 302 redirect to `/login` | Redirected to `**/login` | **PASSED** |
| **TC-SEC-02** | Security | Application running | `GET /login` | HTTP 200 OK, view name `login` | HTTP 200 OK | **PASSED** |
| **TC-SEC-03** | Security | Admin account seeded | `POST /login` with `admin@college.edu`/`admin123` | HTTP 302 redirect to `/dashboard` | Redirected to `/dashboard` | **PASSED** |
| **TC-SEC-04** | Security | User registered | `POST /login` with invalid password | HTTP 302 redirect to `/login?error=true` | Redirected to `/login?error=true` | **PASSED** |
| **TC-REG-01** | Registration | New user data | `registerUser(candidate)` | User saved, password BCrypt encoded | Saved with encrypted hash | **PASSED** |
| **TC-REG-02** | Registration | Email already exists | `registerUser(duplicateEmail)` | Throws `IllegalArgumentException` | `IllegalArgumentException` thrown | **PASSED** |
| **TC-CRUD-01**| Student CRUD | Authenticated user | `GET /dashboard` | HTTP 200 OK, student list model present | Students loaded in model | **PASSED** |
| **TC-CRUD-02**| Student CRUD | Authenticated user | `POST /students` with new student | Student persisted, redirect with flash message | Redirected with success flash | **PASSED** |
| **TC-CRUD-03**| Student CRUD | Saved student record | Update name & subsequent delete | Student details updated, record removed | Updated & verified deleted | **PASSED** |
| **TC-MON-01** | Monitoring | Application running | `GET /actuator/health` | HTTP 200 OK, `{"status": "UP"}` | Status UP verified | **PASSED** |
