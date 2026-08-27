# CloudTask Pro - Architecture Documentation

## 📋 Table of Contents
1. [System Overview](#system-overview)
2. [Architecture Diagram](#architecture-diagram)
3. [Technology Stack](#technology-stack)
4. [Component Details](#component-details)
5. [Data Flow](#data-flow)
6. [Security Architecture](#security-architecture)
7. [Monitoring & Observability](#monitoring--observability)
8. [Scalability & Performance](#scalability--performance)
9. [Deployment Architecture](#deployment-architecture)
10. [Decision Records](#decision-records)

---

## System Overview

CloudTask Pro is a **production-grade task management platform** built using **microservices architecture** with containerization, CI/CD automation, and cloud-native monitoring.

### Key Characteristics

| Characteristic | Description |
|----------------|-------------|
| **Architecture Style** | Microservices with REST APIs |
| **Deployment Model** | Containerized (Docker) + Cloud (AWS) |
| **State Management** | Stateless application + Stateful databases |
| **Scalability** | Horizontal scaling via load balancer |
| **Resilience** | Health checks, auto-restart, monitoring |
| **Security** | JWT authentication, role-based access |

---

## Architecture Diagram

### High-Level Architecture
┌─────────────────────────────────────────────────────────────────────────────┐
│ EXTERNAL WORLD │
│ Users / API Clients / Browsers │
└─────────────────────────────────┬───────────────────────────────────────────┘
│
│ HTTPS/HTTP (Port 80/443)
▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ NGINX REVERSE PROXY │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ • Load Balancing (Round Robin) │ │
│ │ • SSL Termination │ │
│ │ • Static File Serving │ │
│ │ • GZIP Compression │ │
│ │ • Rate Limiting │ │
│ │ • Request Routing │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────┬───────────────────────────────────────────┘
│
│ Internal Network
▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ SPRING BOOT APPLICATION │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ PRESENTATION LAYER │ │
│ │ ┌──────────────────────────────────────────────────────────────┐ │ │
│ │ │ REST Controllers │ │ │
│ │ │ • AuthController • TaskController • UserController │ │ │
│ │ └──────────────────────────────────────────────────────────────┘ │ │
│ │ ┌──────────────────────────────────────────────────────────────┐ │ │
│ │ │ Swagger/OpenAPI Documentation │ │ │
│ │ └──────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ BUSINESS LOGIC LAYER │ │
│ │ ┌──────────────────────────────────────────────────────────────┐ │ │
│ │ │ Services │ │ │
│ │ │ • AuthService • TaskService • UserService │ │ │
│ │ │ • JwtService • CacheService • AuditService │ │ │
│ │ └──────────────────────────────────────────────────────────────┘ │ │
│ │ ┌──────────────────────────────────────────────────────────────┐ │ │
│ │ │ Security │ │ │
│ │ │ • JWT Authentication • Role-Based Access (RBAC) │ │ │
│ │ │ • Password Encryption (BCrypt) • CORS Configuration │ │ │
│ │ └──────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ DATA ACCESS LAYER │ │
│ │ ┌──────────────────────────────────────────────────────────────┐ │ │
│ │ │ Repositories │ │ │
│ │ │ • UserRepository • TaskRepository │ │ │
│ │ │ • Spring Data JPA • Hibernate ORM │ │ │
│ │ └──────────────────────────────────────────────────────────────┘ │ │
│ │ ┌──────────────────────────────────────────────────────────────┐ │ │
│ │ │ Database Migrations (Flyway) │ │ │
│ │ │ • V1__create_users.sql • V2__create_tasks.sql │ │ │
│ │ └──────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└────────────┬──────────────────────────┬──────────────────────────┬──────────┘
│ │ │
▼ ▼ ▼
┌─────────────────────┐ ┌─────────────────────┐ ┌─────────────────────┐
│ POSTGRESQL (RDS) │ │ REDIS CACHE │ │ PROMETHEUS │
│ Port 5432 │ │ Port 6379 │ │ Port 9090 │
│ │ │ │ │ │
│ ┌───────────────┐ │ │ ┌───────────────┐ │ │ ┌───────────────┐ │
│ │ User Data │ │ │ │ Sessions │ │ │ │ Metrics │ │
│ │ Task Data │ │ │ │ Cache │ │ │ │ Storage │ │
│ │ ACID │ │ │ │ Rate Limiting│ │ │ │ Query Engine │ │
│ │ Compliant │ │ │ │ Pub/Sub │ │ │ │ 30 Day │ │
│ └───────────────┘ │ │ └───────────────┘ │ │ │ Retention │ │
└─────────────────────┘ └─────────────────────┘ └─────────────────────┘
│ │ │
└──────────────────────────┼──────────────────────────┘
│
▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ MONITORING DASHBOARD │
│ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ GRAFANA (Port 3000) │ │
│ │ ┌──────────────────────────────────────────────────────────────┐ │ │
│ │ │ • System Health Dashboard │ │ │
│ │ │ • Application Performance Dashboard │ │ │
│ │ │ • JVM Metrics Dashboard │ │ │
│ │ │ • Business Metrics Dashboard │ │ │
│ │ │ • Alert Management │ │ │
│ │ └──────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘

### CI/CD Pipeline Architecture
┌─────────────────────────────────────────────────────────────────────────────┐
│ CI/CD PIPELINE │
│ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ 1. CODE PUSH │ │
│ │ Developer pushes to main branch │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│ │ │
│ ▼ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ 2. BUILD & TEST (GitHub Actions) │ │
│ │ ┌────────────────────────────────────────────────────────────┐ │ │
│ │ │ • Checkout code │ │ │
│ │ │ • Setup JDK 17 │ │ │
│ │ │ • Build with Gradle │ │ │
│ │ │ • Run Unit Tests (JUnit) │ │ │
│ │ │ • Run Integration Tests │ │ │
│ │ │ • Generate Test Reports │ │ │
│ │ └────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│ │ │
│ ▼ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ 3. SECURITY SCANNING │ │
│ │ ┌────────────────────────────────────────────────────────────┐ │ │
│ │ │ • Trivy Vulnerability Scan │ │ │
│ │ │ • Dependency Check │ │ │
│ │ │ • SAST (Static Application Security Testing) │ │ │
│ │ └────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│ │ │
│ ▼ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ 4. DOCKER BUILD & PUSH │ │
│ │ ┌────────────────────────────────────────────────────────────┐ │ │
│ │ │ • Build Docker Image │ │ │
│ │ │ • Tag with commit SHA │ │ │
│ │ │ • Push to Docker Hub / ECR │ │ │
│ │ └────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│ │ │
│ ▼ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ 5. DEPLOY TO EC2 │ │
│ │ ┌────────────────────────────────────────────────────────────┐ │ │
│ │ │ • SSH into EC2 │ │ │
│ │ │ • Pull latest image │ │ │
│ │ │ • Stop old container │ │ │
│ │ │ • Run new container │ │ │
│ │ │ • Verify health check │ │ │
│ │ └────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│ │ │
│ ▼ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ 6. POST-DEPLOYMENT │ │
│ │ ┌────────────────────────────────────────────────────────────┐ │ │
│ │ │ • API Tests (Newman) │ │ │
│ │ │ • Verify monitoring │ │ │
│ │ │ • Slack Notification │ │ │
│ │ └────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘

---

## Technology Stack

### Backend Technologies

| Technology | Version | Purpose | Why Chosen |
|------------|---------|---------|------------|
| **Spring Boot** | 3.2.x | REST API Framework | Industry standard, mature ecosystem |
| **Spring Security** | 6.2.x | Authentication & Authorization | Enterprise-grade security |
| **Spring Data JPA** | 3.2.x | Database ORM | Simplifies data access |
| **Hibernate** | 6.4.x | ORM Implementation | Feature-rich, stable |
| **JWT** | 0.11.x | Token-based Authentication | Stateless, scalable |
| **PostgreSQL** | 16.x | Relational Database | ACID compliant, production-ready |
| **Redis** | 7.x | Caching & Session Management | High performance, in-memory |
| **Flyway** | 9.x | Database Migrations | Version control for database |
| **Lombok** | 1.18.x | Boilerplate Reduction | Cleaner code |
| **Swagger/OpenAPI** | 2.3.x | API Documentation | Interactive API docs |

### DevOps & Infrastructure

| Technology | Version | Purpose | Why Chosen |
|------------|---------|---------|------------|
| **Docker** | 20.10+ | Containerization | Consistency across environments |
| **Docker Compose** | 2.0+ | Multi-container Orchestration | Local development |
| **Nginx** | Alpine | Reverse Proxy & Load Balancer | Performance, stability |
| **GitHub Actions** | N/A | CI/CD Automation | Integrated with GitHub |
| **Docker Hub** | N/A | Container Registry | Simple, free tier |
| **AWS EC2** | N/A | Compute (t2.micro) | Free tier, flexible |
| **AWS RDS** | N/A | Managed Database | Automated backups, scaling |
| **AWS CloudWatch** | N/A | Monitoring & Logging | Native AWS integration |
| **Prometheus** | Latest | Metrics Collection | Industry standard |
| **Grafana** | Latest | Visualization | Beautiful dashboards |

### Application Layers
┌─────────────────────────────────────────────────────────────────────────────┐
│ APPLICATION LAYERS │
│ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ PRESENTATION LAYER │ │
│ │ • REST Controllers (Auth, Tasks, Users) │ │
│ │ • Swagger/OpenAPI Documentation │ │
│ │ • Request/Response DTOs │ │
│ │ • Validation (Jakarta Validation) │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│ │ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ BUSINESS LAYER │ │
│ │ • Services (Auth, Tasks, Users, JWT) │ │
│ │ • Business Logic Implementation │ │
│ │ • Transaction Management │ │
│ │ • Exception Handling │ │
│ │ • Caching Strategy │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│ │ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ DATA LAYER │ │
│ │ • Repositories (User, Task) │ │
│ │ • Entity Models │ │
│ │ • Database Migrations (Flyway) │ │
│ │ • Query Methods │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│ │ │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │ SECURITY LAYER │ │
│ │ • JWT Authentication Filter │ │
│ │ • Security Configuration │ │
│ │ • Role-Based Access Control (RBAC) │ │
│ │ • Password Encoding (BCrypt) │ │
│ │ • CORS Configuration │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘

---

## Component Details

### 1. Spring Boot Application

#### Key Components

| Component | Class | Responsibility |
|-----------|-------|----------------|
| **Main Application** | `CloudTaskApplication.java` | Application entry point |
| **Auth Controller** | `AuthController.java` | Registration, Login, Refresh |
| **Task Controller** | `TaskController.java` | CRUD operations for tasks |
| **User Controller** | `UserController.java` | User profile management |
| **JWT Service** | `JwtService.java` | Token generation & validation |
| **Task Service** | `TaskService.java` | Business logic for tasks |
| **User Service** | `UserService.java` | Business logic for users |
| **Security Config** | `SecurityConfig.java` | Security configuration |
| **JWT Filter** | `JwtAuthenticationFilter.java` | Request authentication |

#### Application Properties

```yaml
spring:
  application:
    name: cloudtask-pro
  
  datasource:
    url: jdbc:postgresql://postgres:5432/cloudtaskdb
    username: cloudtask_user
    password: ${DB_PASSWORD}
  
  jpa:
    hibernate:
      ddl-auto: validate
    
  flyway:
    enabled: true
    baseline-on-migrate: true
  
  data:
    redis:
      host: redis
      port: 6379

server:
  port: 8080

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus

