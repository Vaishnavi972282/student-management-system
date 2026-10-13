# DevOps-Based Student Management Web Application

A full-stack, enterprise-grade Java web application built using **Spring Boot 3**, **Thymeleaf**, **Spring Security**, and **Spring Data JPA**, designed and deployed through a complete **DevOps Lifecycle (GitFlow, Jenkins CI/CD, Docker containerization, Kubernetes orchestration, and Actuator monitoring)**.

---

## 👥 10-Member Team Responsibility Matrix

| Member | Role | Key Responsibility | Deliverable / Artifact |
| :--- | :--- | :--- | :--- |
| **Member 1** | Team Lead / Product Owner | Architecture, requirements, integration tracking | `docs/architecture.md`, `README.md` |
| **Member 2** | Security & Login Engineer | Authentication, session handling, logout, BCrypt | `SecurityConfig.java`, `login.html` |
| **Member 3** | Registration Engineer | Student registration, input validation, duplicate check | `UserService.java`, `register.html` |
| **Member 4** | Student CRUD Engineer | Student records management (Create, Read, Update, Delete) | `StudentController.java`, `StudentService.java` |
| **Member 5** | QA Engineer | Automated test cases (Unit, Integration, Security) | `StudentManagementApplicationTests.java`, `docs/testing.md` |
| **Member 6** | Git / GitHub Engineer | Version control, GitFlow branching, PR management | Git history, branches (`develop`, `feature/*`), PRs |
| **Member 7** | Maven & Jenkins Engineer | Build lifecycle and declarative CI pipeline | `pom.xml`, `Jenkinsfile` |
| **Member 8** | Docker Engineer | Multi-stage Docker image, container deployment | `Dockerfile`, `compose.yaml` |
| **Member 9** | Kubernetes Engineer | Pods, ReplicaSet, Deployment, Service, Probes | `k8s/deployment.yaml`, `k8s/service.yaml` |
| **Member 10** | Monitoring & DevOps Integrator | Actuator health monitoring, log review, viva report | `/actuator/health`, `docs/demonstration-checklist.md` |

---

## 🛠️ Technology Stack
- **Language**: Java 21 / 24
- **Framework**: Spring Boot 3.4.3 (Spring MVC, Spring Data JPA, Spring Security)
- **Frontend**: Thymeleaf, Bootstrap 5, Bootstrap Icons
- **Database**: H2 (In-Memory for development and testing) / MySQL 8.4+ (Production)
- **CI/CD**: Apache Maven 3.9, Jenkins Pipeline
- **Containers**: Docker & Docker Compose
- **Orchestration**: Kubernetes (`kubectl`, Minikube)
- **Monitoring**: Spring Boot Actuator

---

## 🚀 Quick Start (Running Locally)

### 1. Build and Run via Maven
```powershell
cd student-management-system
mvn clean package
java -jar target/student-management-1.0.0.jar
```
Or run directly in development mode:
```powershell
mvn spring-boot:run
```

Access the application in your browser:
- **Application URL**: [http://localhost:8080](http://localhost:8080)
- **Default Admin Account**: `admin@college.edu`
- **Default Password**: `admin123`
- **Actuator Health Endpoint**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **H2 Web Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:studentdb`)

---

## 🐳 Docker Deployment
To build and run the multi-stage Docker container:
```powershell
# Build image
docker build -t student-management:local .

# Run with Docker Compose (includes MySQL 8.4)
docker compose up -d

# Verify logs
docker logs -f student-management-app
```

---

## ☸️ Kubernetes Deployment
Deploy into Minikube or any Kubernetes cluster:
```powershell
kubectl apply -f k8s/
kubectl get pods
kubectl get services
```
Access via Minikube tunnel or node port:
```powershell
minikube service student-management-service
```
