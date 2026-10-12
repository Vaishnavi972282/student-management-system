# Architecture & System Design Document
**Project:** DevOps-Based Student Management Web Application  
**Lead:** Member 1 (Team Lead / Product Owner)

---

## 1. High-Level Architecture

The Student Management Application follows a modern 3-Tier Layered Architecture containerized and orchestrated through cloud-native tools:

```
[ User Browser / Client ]
           │
           ▼ (HTTP Port 8080 / 30080)
┌────────────────────────────────────────────────────────┐
│ Kubernetes Cluster (or Docker Engine)                  │
│                                                        │
│  [ Kubernetes Service: student-management-service ]   │
│                          │                             │
│                          ▼ (Round Robin)               │
│  ┌─────────────────────────┐ ┌──────────────────────┐  │
│  │ Pod 1: student-app      │ │ Pod 2: student-app   │  │
│  │  - Spring Boot 3        │ │  - Spring Boot 3     │  │
│  │  - Spring Security      │ │  - Spring Security   │  │
│  │  - Thymeleaf View Engine│ │  - Thymeleaf         │  │
│  │  - Actuator Health Probe│ │  - Actuator          │  │
│  └───────────┬─────────────┘ └──────────┬───────────┘  │
│              │                          │              │
└──────────────┼──────────────────────────┼──────────────┘
               ▼                          ▼
      [ Database Tier: H2 in-memory / MySQL 8.4 Persistent ]
```

---

## 2. Component Structure
- **Presentation Layer (`controller`, `templates`)**:
  - `AuthController`: Manages `/login`, `/register`, logout redirects.
  - `StudentController`: Manages `/dashboard`, `/students/new`, `/students/edit/{id}`, `/students/view/{id}`, `/students/delete/{id}`.
- **Security Layer (`config`)**:
  - `SecurityConfig`: Configures URL authorization, form login, BCrypt password hashing, session lifecycle.
- **Service Layer (`service`)**:
  - `UserService`: User account creation and Spring Security `UserDetailsService` authentication logic.
  - `StudentService`: Student CRUD business logic and duplicate key checking.
- **Data Access Layer (`repository`, `entity`)**:
  - `UserRepository` & `User`: Stores credentials with hashed passwords.
  - `StudentRepository` & `Student`: Stores student profile records.
- **DevOps Layer (`Dockerfile`, `Jenkinsfile`, `k8s/`)**:
  - Jenkins pipeline for continuous automated builds and test verification.
  - Multi-stage Docker packaging.
  - Kubernetes declarative deployments and services.
