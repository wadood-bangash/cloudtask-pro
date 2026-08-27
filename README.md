# ☁️ CloudTask Pro

## Enterprise Task Management Platform with Full DevOps Pipeline

[![Java](https://img.shields.io/badge/Java-17-007396?logo=java)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-20.10+-2496ED?logo=docker)](https://www.docker.com/)
[![AWS](https://img.shields.io/badge/AWS-EC2%20%7C%20RDS-232F3E?logo=amazonaws)](https://aws.amazon.com/)
[![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-2088FF?logo=githubactions)](https://github.com/features/actions)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [API Documentation](#api-documentation)
- [Quick Start](#quick-start)
- [Local Development](#local-development)
- [Docker Deployment](#docker-deployment)
- [AWS Deployment](#aws-deployment)
- [CI/CD Pipeline](#cicd-pipeline)
- [Monitoring](#monitoring)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

**CloudTask Pro** is a production-grade task management platform built with **Spring Boot** and deployed using a complete **DevOps pipeline**. This project demonstrates:

- ✅ **Containerization** with Docker & Docker Compose
- ✅ **CI/CD Automation** with GitHub Actions
- ✅ **Cloud Deployment** on AWS EC2 with RDS
- ✅ **Monitoring** with Prometheus & Grafana
- ✅ **Security** with JWT Authentication
- ✅ **Scalability** with Horizontal Scaling

### Key Features

| Feature | Description |
|---------|-------------|
| **User Management** | Registration, Login, JWT Authentication |
| **Task Management** | Create, Read, Update, Delete, Complete tasks |
| **Task Assignment** | Assign tasks to users |
| **Search & Filter** | Search by title, filter by status/priority |
| **Task Statistics** | Real-time task metrics |
| **API Documentation** | Swagger/OpenAPI interactive docs |
| **Health Checks** | Built-in actuator endpoints |
| **Metrics** | Prometheus metrics for monitoring |

---

## Architecture

### High-Level Architecture
┌─────────────────────────────────────────────────────────────────────────────┐
│ EXTERNAL WORLD │
│ Users / API Clients / Browsers │
└─────────────────────────────────┬───────────────────────────────────────────┘
│
▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ NGINX REVERSE PROXY │
│ Load Balancing & Routing │
└─────────────────────────────────┬───────────────────────────────────────────┘
│
▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ SPRING BOOT APPLICATION │
│ REST API with JWT Authentication │
└────────────┬──────────────────────────┬──────────────────────────┬──────────┘
│ │ │
▼ ▼ ▼
┌─────────────────────┐ ┌─────────────────────┐ ┌─────────────────────┐
│ POSTGRESQL (RDS) │ │ REDIS CACHE │ │ PROMETHEUS │
│ Primary Database │ │ Sessions & Cache │ │ Metrics Storage │
└─────────────────────┘ └─────────────────────┘ └─────────────────────┘
│ │ │
└──────────────────────────┼──────────────────────────┘
▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ GRAFANA DASHBOARD │
│ Visualization & Monitoring │
└─────────────────────────────────────────────────────────────────────────────┘
text


### CI/CD Pipeline

GitHub Push → Build → Test → Dockerize → Push → Deploy → Monitor
│ │ │ │ │ │ │
│ │ │ │ │ │ └─ Grafana
│ │ │ │ │ └─────────── AWS EC2
│ │ │ │ └───────────────── Docker Hub
│ │ │ └────────────────────────── Docker Build
│ │ └──────────────────────────────────── JUnit Tests
│ └───────────────────────────────────────────── Gradle Build
└────────────────────────────────────────────────────────── GitHub Actions

---

## Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 3.2.3 | REST API Framework |
| Spring Security | 6.2.x | Authentication & Authorization |
| Spring Data JPA | 3.2.x | Database ORM |
| PostgreSQL | 16.x | Relational Database |
| Redis | 7.x | Caching & Session Management |
| Flyway | 9.x | Database Migrations |
| JWT | 0.11.x | Token-based Authentication |
| Lombok | 1.18.x | Boilerplate Reduction |
| Swagger/OpenAPI | 2.3.x | API Documentation |

### DevOps
| Technology | Version | Purpose |
|------------|---------|---------|
| Docker | 20.10+ | Containerization |
| Docker Compose | 2.0+ | Multi-container Orchestration |
| Nginx | Alpine | Reverse Proxy & Load Balancer |
| GitHub Actions | N/A | CI/CD Automation |
| Docker Hub | N/A | Container Registry |
| AWS EC2 | t2.micro | Compute |
| AWS RDS | PostgreSQL | Managed Database |
| AWS CloudWatch | N/A | Monitoring & Logging |
| Prometheus | Latest | Metrics Collection |
| Grafana | Latest | Visualization |

---

## Features

### User Features

| Feature | Endpoint | Method | Auth |
|---------|----------|--------|------|
| Register | `/api/auth/register` | POST | ❌ |
| Login | `/api/auth/login` | POST | ❌ |
| Get Profile | `/api/users/me` | GET | ✅ |
| Update Profile | `/api/users/me` | PUT | ✅ |

### Task Features

| Feature | Endpoint | Method | Auth |
|---------|----------|--------|------|
| Create Task | `/api/tasks` | POST | ✅ |
| Get All Tasks | `/api/tasks` | GET | ✅ |
| Get Task by ID | `/api/tasks/{id}` | GET | ✅ |
| Update Task | `/api/tasks/{id}` | PUT | ✅ |
| Delete Task | `/api/tasks/{id}` | DELETE | ✅ |
| Complete Task | `/api/tasks/{id}/complete` | PATCH | ✅ |
| Search Tasks | `/api/tasks/search` | GET | ✅ |
| Task Statistics | `/api/tasks/stats` | GET | ✅ |

### Monitoring Features

| Feature | Endpoint | Method | Auth |
|---------|----------|--------|------|
| Health Check | `/actuator/health` | GET | ❌ |
| Metrics | `/actuator/metrics` | GET | ❌ |
| Prometheus | `/actuator/prometheus` | GET | ❌ |
| Swagger UI | `/swagger-ui.html` | GET | ❌ |
| API Docs | `/api-docs` | GET | ❌ |

---

## API Documentation

### Authentication

#### Register User

```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "fullName": "John Doe"
}
Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER",
  "message": "User registered successfully"
}
Login
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePass123"
}
Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER",
  "message": "Login successful"
}
Task Management
Create Task
POST /api/tasks
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "title": "Complete Project Documentation",
  "description": "Write comprehensive README and API docs",
  "priority": "HIGH",
  "dueDate": "2026-12-31T23:59:59",
  "assignedToUserId": 2
}
Response:
{
  "id": 1,
  "title": "Complete Project Documentation",
  "description": "Write comprehensive README and API docs",
  "status": "PENDING",
  "priority": "HIGH",
  "dueDate": "2026-12-31T23:59:59",
  "createdBy": "john_doe",
  "assignedTo": "jane_smith",
  "createdAt": "2026-08-27T10:00:00",
  "updatedAt": "2026-08-27T10:00:00",
  "completedAt": null
}
Get Tasks
GET /api/tasks?status=PENDING&page=0&size=10
Authorization: Bearer <JWT_TOKEN>
Complete Task
PATCH /api/tasks/{id}/complete
Authorization: Bearer <JWT_TOKEN>
Quick Start
Prerequisites

    Docker (20.10+)

    Docker Compose (2.0+)

    Git (2.0+)

    Java 17 (for local development)

    AWS CLI (for AWS deployment)

One-Click Start
# Clone the repository
git clone https://github.com/your-username/cloudtask-manager-pro.git
cd cloudtask-manager-pro

# Copy environment variables
cp .env.example .env

# Edit .env with your values
nano .env

# Start all services
docker-compose up -d

# Check status
docker-compose ps

# Access the application
curl http://localhost:8080/api/tasks
