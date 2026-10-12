# Faculty Demonstration Checklist & Viva Guide
**DevOps-Based Student Management Web Application**

Use this checklist during your college project presentation to demonstrate every module step-by-step.

---

## 1. Demonstration Sequence for Faculty

### Step 1: Start Application
Run:
```powershell
cd D:\devops\student-management
java -jar target\student-management-1.0.0.jar
```
Show startup logs in terminal indicating:
- `Tomcat started on port 8080 (http)`
- `Started StudentManagementApplication in X.XXX seconds`

### Step 2: Member 2 (Login Module Demo)
1. Open browser at `http://localhost:8080/dashboard`.
2. Notice automatic redirection to `http://localhost:8080/login` (Protected Route Demonstration).
3. Try typing invalid credentials (e.g. `test@wrong.com` / `wrongpass`) -> Shows red alert banner.
4. Login with `admin@college.edu` / `admin123` -> Successfully lands on Dashboard.

### Step 3: Member 3 (Registration Module Demo)
1. Click **Logout** from top-right.
2. Click **Create New Account**.
3. Register a new user:
   - Full Name: `Faculty Evaluator`
   - Email: `evaluator@college.edu`
   - Password: `password123`
4. Show successful registration message, then login using the newly created account.

### Step 4: Member 4 (Student CRUD Operations Demo)
1. On Dashboard, click **Add Student**.
2. Enter student details:
   - Roll No: `CS2026-099`
   - Name: `Aditya Sharma`
   - Email: `aditya@college.edu`
   - Department: `Computer Engineering`
   - Year: `Final Year (Sem 7-8)`
   - Contact: `9876543210`
3. Click **Save** -> Card updates with green success alert.
4. Click **View (Eye icon)** -> Shows formatted Student Profile card.
5. Click **Edit (Pencil icon)** -> Change semester or name and save -> Verifies update.
6. Click **Delete (Trash icon)** -> Confirms and removes student.

### Step 5: Member 5 (QA Test Suite Demo)
Run:
```powershell
mvn test
```
Show terminal output: `Tests run: 10, Failures: 0, Errors: 0, Skipped: 0` -> `BUILD SUCCESS`.

### Step 6: Member 7 (Maven & Jenkins Demo)
Show `pom.xml` and `Jenkinsfile` in IDE. Explain the 7 pipeline stages:
1. Checkout
2. Environment Validation
3. Compilation
4. Test Execution
5. Packaging
6. Artifact Archival (`target/*.jar`)
7. Docker Image Creation

### Step 7: Member 8 (Docker Containerization Demo)
Show `Dockerfile` (multi-stage non-root build) and run:
```powershell
docker build -t student-management:local .
docker compose up -d
docker ps
```

### Step 8: Member 9 (Kubernetes Manifests Demo)
Show `k8s/deployment.yaml` and `k8s/service.yaml`.
Explain:
- `replicas: 2` (High availability)
- `readinessProbe` and `livenessProbe` pointing to `/actuator/health`
- `NodePort: 30080` Service

### Step 9: Member 10 (Health Monitoring Demo)
In browser, open:
`http://localhost:8080/actuator/health`  
Show live JSON response:
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "diskSpace": { "status": "UP" },
    "ping": { "status": "UP" }
  }
}
```
