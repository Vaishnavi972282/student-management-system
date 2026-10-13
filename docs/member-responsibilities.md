# DevOps-Based Student Management System — Team Roles & Deliverables Guide
**College Mini Project — Complete 10-Member Implementation & Viva Dossier**

---

## 👥 MEMBER 1 — Team Lead / Product Owner

### 1. Responsibilities
- Define and freeze functional & non-functional requirements.
- Maintain the member-wise task allocation tracker and coordinate module integrations.
- Prepare the system architecture, component diagrams, and entity-relationship models.
- Ensure all 10 experiments connect together into ONE working software deliverable.

### 2. Deliverables Produced & Verified
- **Requirements Specification**: [docs/architecture.md](file:///docs/architecture.md#L1-L45)
- **Task Allocation Matrix**: [README.md](file:///README.md#L7-L23)
- **Demonstration Checklist**: [docs/demonstration-checklist.md](file:///docs/demonstration-checklist.md)

### 3. Architecture & Data Flow Diagram
```
                     [ Web Client / Browser ]
                                │
                                ▼ (HTTP / Port 8080)
┌───────────────────────────────────────────────────────────────┐
│              Spring Boot Application Server                   │
│                                                               │
│   ┌───────────────────────────────────────────────────────┐   │
│   │ Spring Security Filter Chain (Member 2)               │   │
│   │  - BCryptPasswordEncoder                              │   │
│   │  - Form Login / Logout Session Management             │   │
│   └───────────┬───────────────────────────────┬───────────┘   │
│               │                               │               │
│               ▼                               ▼               │
│   ┌───────────────────────┐       ┌───────────────────────┐   │
│   │ AuthController (M2/M3)│       │ StudentController (M4)│   │
│   └───────────┬───────────┘       └───────────┬───────────┘   │
│               ▼                               ▼               │
│   ┌───────────────────────┐       ┌───────────────────────┐   │
│   │ UserService (M2/M3)   │       │ StudentService (M4)   │   │
│   └───────────┬───────────┘       └───────────┬───────────┘   │
│               ▼                               ▼               │
│   ┌───────────────────────┐       ┌───────────────────────┐   │
│   │ UserRepository (JPA)  │       │ StudentRepository(JPA)│   │
│   └───────────┬───────────┘       └───────────┬───────────┘   │
│               │                               │               │
└───────────────┼───────────────────────────────┼───────────────┘
                ▼                               ▼
       [ "users" Table ]               [ "students" Table ]
       (Credentials & Roles)          (Student Academic Profile)
```

### 4. Faculty Viva Questions & Answers
- **Q1: Why did you separate the User entity from the Student entity?**  
  *Answer:* To follow the Principle of Single Responsibility and security isolation. The `User` entity manages authentication credentials, password hashes, and authorization roles. The `Student` entity manages academic information (roll number, department, semester). This avoids exposing login credentials with student records.
- **Q2: How does the application scale in a DevOps environment?**  
  *Answer:* The application is packaged as a stateless container image (`student-management:local`) orchestrated by Kubernetes with multiple replicas managed behind a NodePort Service.

---

## 🔐 MEMBER 2 — Login Developer

### 1. Responsibilities
- Implement custom authentication flow, login page, and session handling.
- Protect dashboard and CRUD operations against unauthenticated requests.
- Verify credentials against BCrypt password hashes.
- Handle logout and session cleanup.

### 2. Deliverables Produced & Verified
- **Spring Security Configuration**: [`SecurityConfig.java`](file:///src/main/java/com/devops/studentmanagement/config/SecurityConfig.java)
- **Authentication Service**: [`UserService.java`](file:///src/main/java/com/devops/studentmanagement/service/UserService.java#L44-L54)
- **Login Web Interface**: [`login.html`](file:///src/main/resources/templates/login.html)
- **Automated Tests**: [`StudentManagementApplicationTests.java`](file:///src/test/java/com/devops/studentmanagement/StudentManagementApplicationTests.java#L31-L71) (`TC-SEC-01`, `TC-SEC-02`, `TC-SEC-03`, `TC-SEC-04`)

### 3. Faculty Viva Questions & Answers
- **Q1: How are passwords secured in your application?**  
  *Answer:* Passwords are never stored in plain text. We use Spring Security's `BCryptPasswordEncoder`, which applies cryptographic salt and a computationally intensive one-way hashing function.
- **Q2: What happens if an unauthenticated user tries to open `/dashboard` directly?**  
  *Answer:* The Spring Security Filter interceptor detects that the user does not possess an active authenticated session token and automatically issues an HTTP 302 redirection to `/login`.

---

## 📝 MEMBER 3 — Registration Developer

### 1. Responsibilities
- Create user registration web page with input validation.
- Enforce validation rules on full name, email format, and password length.
- Check and reject duplicate account registrations.
- Encrypt passwords before persisting new users into the database.

### 2. Deliverables Produced & Verified
- **Data Transfer Object**: [`UserRegistrationDto.java`](file:///src/main/java/com/devops/studentmanagement/dto/UserRegistrationDto.java)
- **Registration Controller Endpoint**: [`AuthController.java`](file:///src/main/java/com/devops/studentmanagement/controller/AuthController.java#L34-L55)
- **Registration Web View**: [`register.html`](file:///src/main/resources/templates/register.html)
- **Automated Registration Tests**: [`StudentManagementApplicationTests.java`](file:///src/test/java/com/devops/studentmanagement/StudentManagementApplicationTests.java#L76-L95) (`TC-REG-01`, `TC-REG-02`)

### 3. Faculty Viva Questions & Answers
- **Q1: How do you prevent duplicate registrations with the same email?**  
  *Answer:* In `UserService.registerUser()`, we call `userRepository.existsByEmail(dto.getEmail())`. If `true`, an `IllegalArgumentException` is thrown and rendered back to the user on the registration form without creating a database collision.
- **Q2: What is the purpose of using a DTO instead of the User Entity directly?**  
  *Answer:* Using `UserRegistrationDto` prevents over-posting attacks, isolates form fields from database schema internals, and applies validation rules specific to user registration input.

---

## 🎓 MEMBER 4 — Student Management Developer

### 1. Responsibilities
- Implement full CRUD operations (Create, Read, Update, Delete) for student records.
- Validate student roll numbers, departments, semesters, and contact numbers.
- Prevent duplicate roll numbers and conflicting emails across student profiles.
- Display instant feedback using Thymeleaf flash messages.

### 2. Deliverables Produced & Verified
- **Student Entity**: [`Student.java`](file:///src/main/java/com/devops/studentmanagement/entity/Student.java)
- **Repository Interface**: [`StudentRepository.java`](file:///src/main/java/com/devops/studentmanagement/repository/StudentRepository.java)
- **Business Logic Service**: [`StudentService.java`](file:///src/main/java/com/devops/studentmanagement/service/StudentService.java)
- **Web Controller**: [`StudentController.java`](file:///src/main/java/com/devops/studentmanagement/controller/StudentController.java)
- **User Views**: [`dashboard.html`](file:///src/main/resources/templates/dashboard.html), [`student-form.html`](file:///src/main/resources/templates/student-form.html), [`student-view.html`](file:///src/main/resources/templates/student-view.html)
- **Automated Tests**: [`StudentManagementApplicationTests.java`](file:///src/test/java/com/devops/studentmanagement/StudentManagementApplicationTests.java#L100-L144) (`TC-CRUD-01`, `TC-CRUD-02`, `TC-CRUD-03`)

### 3. Faculty Viva Questions & Answers
- **Q1: How does your update logic avoid duplicate key errors if the user doesn't modify their roll number?**  
  *Answer:* In `StudentService.updateStudent()`, when checking `findByStudentCode()`, we compare `existing.getId().equals(id)`. If the matching record belongs to the same student being updated, it is valid; otherwise, a duplicate collision is prevented.
- **Q2: How are success and error messages passed across redirects?**  
  *Answer:* We use Spring's `RedirectAttributes.addFlashAttribute()`, which temporarily stores the message in the HTTP session and displays it once after redirection before removing it automatically.

---

## 🧪 MEMBER 5 — QA Engineer

### 1. Responsibilities
- Prepare a comprehensive test plan covering unit, integration, and security layers.
- Write automated test cases using JUnit 5 and Spring Boot `MockMvc`.
- Run tests and record genuine execution logs.
- Document the verification matrix in `docs/testing.md`.

### 2. Deliverables Produced & Verified
- **Automated Test Suite**: [`StudentManagementApplicationTests.java`](file:///src/test/java/com/devops/studentmanagement/StudentManagementApplicationTests.java)
- **Test Execution Report**: [`docs/testing.md`](file:///docs/testing.md)
- **Actual Build Execution Evidence**:
  ```
  Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
  Total time: 01:44 min — BUILD SUCCESS
  ```

### 3. Faculty Viva Questions & Answers
- **Q1: What is MockMvc and why did you use it?**  
  *Answer:* `MockMvc` is a Spring test tool that lets us test web controllers without starting a full HTTP server. It simulates HTTP GET and POST requests, verifies status codes, redirect URLs, view names, and security filters.
- **Q2: What is the purpose of `@Transactional` on your test class?**  
  *Answer:* It ensures that any database changes made during individual test executions are automatically rolled back when the test finishes, keeping the test database clean and isolated.

---

## 🌿 MEMBER 6 — Git / GitHub Engineer

### 1. Responsibilities
- Set up the Git repository with standard `.gitignore` rules.
- Design and enforce the **GitFlow** branching strategy (`main`, `develop`, `feature/*`, `release/*`).
- Tag releases and manage pull requests and code review flow.

### 2. Deliverables Produced & Verified
- **Git Configuration**: Local repository initialized on branch `main` (commit `8df19e1`).
- **Release Tag**: `v1.0.0`
- **Active GitFlow Branches**:
  ```
  develop
  feature/login
  feature/registration
  feature/student-crud
  feature/tests
  feature/jenkins
  feature/docker
  feature/kubernetes
  release/v1.0
  * main
  ```

### 3. Faculty Viva Questions & Answers
- **Q1: Explain the difference between `main` and `develop` in GitFlow.**  
  *Answer:* `main` stores only production-ready, tagged releases (e.g. `v1.0.0`). `develop` serves as the integration branch where completed feature branches (`feature/*`) are merged after code review and testing.
- **Q2: How do you push these branches to GitHub?**  
  *Answer:* By running:
  ```powershell
  git remote add origin https://github.com/Vaishnavi972282/student-management-system.git
  git push -u origin main
  git push origin develop --all
  git push --tags
  ```

---

## ⚙️ MEMBER 7 — Jenkins Engineer

### 1. Responsibilities
- Manage the Maven build lifecycle (`compile`, `test`, `package`).
- Create a declarative CI/CD pipeline script (`Jenkinsfile`).
- Ensure the pipeline fails if compilation or tests fail, and archive artifacts upon success.

### 2. Deliverables Produced & Verified
- **Build Descriptor**: [`pom.xml`](file:///pom.xml)
- **Built Artifact**: `target/student-management-1.0.0.jar` (65 MB)
- **Pipeline Script**: [`Jenkinsfile`](file:///Jenkinsfile) with 7 declarative stages:
  1. *Checkout*
  2. *Validate Environment*
  3. *Compile*
  4. *Run Automated Tests* (with JUnit report collection)
  5. *Package Application*
  6. *Archive Artifacts* (`target/*.jar`)
  7. *Build Docker Image*

### 3. Faculty Viva Questions & Answers
- **Q1: What is the purpose of the `archiveArtifacts` step in Jenkins?**  
  *Answer:* It instructs Jenkins to capture the generated `student-management-1.0.0.jar` from the build workspace and store it persistently in the Jenkins build history for deployment.
- **Q2: What happens if any of Member 5's test cases fail during the Jenkins build?**  
  *Answer:* In stage 4 (`Run Automated Tests`), Maven exits with an error status, causing Jenkins to immediately stop the build, mark the job as `FAILURE`, and skip packaging and deployment.

---

## 🐳 MEMBER 8 — Docker Engineer

### 1. Responsibilities
- Create a production-ready, multi-stage [`Dockerfile`](file:///Dockerfile) with a non-root user.
- Build container image `student-management:local`.
- Create [`compose.yaml`](file:///compose.yaml) to orchestrate both the application container and the MySQL 8.4 database.

### 2. Deliverables Produced & Verified
- **Multi-Stage Dockerfile**: [`Dockerfile`](file:///Dockerfile)
- **Docker Ignore File**: [`.dockerignore`](file:///.dockerignore)
- **Docker Compose Spec**: [`compose.yaml`](file:///compose.yaml)

### 3. Faculty Viva Questions & Answers
- **Q1: Why did you use a multi-stage Docker build?**  
  *Answer:* Stage 1 uses a full JDK to compile the project and build the JAR. Stage 2 copies ONLY the built `.jar` into a lightweight Alpine JRE runtime image. This reduces final image size and eliminates build tools and source code from production for security.
- **Q2: Why did you create an `appuser` instead of running as root?**  
  *Answer:* Running containers as root is a major security vulnerability. Running as an unprivileged user prevents container breakouts from gaining root privileges on the host system.

---

## ☸️ MEMBER 9 — Kubernetes Engineer

### 1. Responsibilities
- Author declarative Kubernetes manifests for application deployment and network exposure.
- Configure Pod replica counts, CPU and memory limits.
- Set up HTTP Readiness and Liveness probes pointing to `/actuator/health`.
- Create a NodePort Service to make the application accessible.

### 2. Deliverables Produced & Verified
- **Deployment Manifest**: [`k8s/deployment.yaml`](file:///k8s/deployment.yaml) (Replicas: 2, Resource requests/limits, Health probes)
- **Service Manifest**: [`k8s/service.yaml`](file:///k8s/service.yaml) (Type: `NodePort`, NodePort: `30080`, Port: `8080`)

### 3. Faculty Viva Questions & Answers
- **Q1: What is the difference between a Liveness Probe and a Readiness Probe?**  
  *Answer:* A **Readiness Probe** checks if the container is ready to accept traffic; if it fails, Kubernetes stops routing requests to that Pod. A **Liveness Probe** checks if the application is still running; if it fails (e.g., deadlock), Kubernetes kills and restarts the container.
- **Q2: How do you deploy and access the application in Minikube?**  
  *Answer:*
  ```powershell
  kubectl apply -f k8s/
  kubectl get pods
  minikube service student-management-service
  ```

---

## 📈 MEMBER 10 — DevOps / SRE Engineer

### 1. Responsibilities
- Integrate Spring Boot Actuator for operational health monitoring and metrics.
- Verify live application startup, memory usage, and component statuses.
- Assemble end-to-end integration evidence and faculty viva preparation documentation.

### 2. Deliverables Produced & Verified
- **Monitoring Configuration**: [`application.properties`](file:///src/main/resources/application.properties#L32-L37)
- **Live Health Endpoint Verified**:
  ```json
  {
    "status": "UP",
    "components": {
      "db": { "status": "UP", "details": { "database": "H2", "validationQuery": "isValid()" } },
      "diskSpace": { "status": "UP", "details": { "free": 222450880512 } },
      "ping": { "status": "UP" }
    }
  }
  ```
- **Project Dossier**: [`docs/member-responsibilities.md`](file:///docs/member-responsibilities.md) & [`docs/demonstration-checklist.md`](file:///docs/demonstration-checklist.md)

### 3. Faculty Viva Questions & Answers
- **Q1: How do you verify the application's health without logging into the UI?**  
  *Answer:* By querying the REST endpoint `/actuator/health`. It returns HTTP 200 with JSON payload `{"status":"UP"}` and component breakdown for database, disk space, and network ping.
- **Q2: How do you trace issues if the application crashes?**  
  *Answer:* We check application logs in the terminal/console, container logs using `docker logs <container-id>`, and Kubernetes Pod logs using `kubectl logs <pod-name>`.

---

## 🎯 Summary Table: Who Shows What During Viva

| Member | Module | Live Screen / Command to Show |
| :--- | :--- | :--- |
| **Member 1** | Architecture | Show `docs/architecture.md` and high-level 3-tier diagram |
| **Member 2** | Login | Open `http://localhost:8080/login` & test wrong vs correct password |
| **Member 3** | Registration | Open `http://localhost:8080/register` & register a new user |
| **Member 4** | Student CRUD | Add, Edit, View, and Delete a student on `http://localhost:8080/dashboard` |
| **Member 5** | QA / Testing | Run `mvn test` in terminal and show 10 passing tests |
| **Member 6** | Git / GitHub | Run `git branch -a` and `git log --oneline` in terminal |
| **Member 7** | Maven & Jenkins | Show `pom.xml` and the 7-stage `Jenkinsfile` |
| **Member 8** | Docker | Show `Dockerfile` (multi-stage) and `compose.yaml` |
| **Member 9** | Kubernetes | Show `k8s/deployment.yaml` (probes, replicas) & `k8s/service.yaml` |
| **Member 10**| Monitoring | Open `http://localhost:8080/actuator/health` in browser |
