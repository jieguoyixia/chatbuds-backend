# ChatBuds Backend

**ChatBuds** is a real-time chat application backend built with **Spring Boot**, **MongoDB**, and **WebSocket/STOMP**. It supports user authentication with JWT, refresh token handling, and persistent chat history. The backend is fully Dockerized and can be deployed using Docker Compose or Jenkins CI/CD.

---

## Table of Contents

- [Features](#features)  
- [Tech Stack](#tech-stack)  
- [Getting Started](#getting-started)  
  - [Prerequisites](#prerequisites)  
  - [Run Locally](#run-locally)  

---

## Features

- User registration and login with JWT authentication  
- Access token + refresh token handling  
- Real-time chat using WebSocket/STOMP  
- Persistent chat history stored in MongoDB  
- REST endpoints for user authentication and chat history  
- Dockerized for easy deployment  

---

## Tech Stack

- **Backend:** Java 21, Spring Boot  
- **Database:** MongoDB  
- **Messaging:** WebSocket/STOMP  
- **Build Tool:** Maven  
- **Containerization:** Docker, Docker Compose

---

## Getting Started

### Prerequisites

- Java 21 (or OpenJDK 21)  
- Maven  
- Docker & Docker Compose  
- Git  

---

### Run Locally

1. Clone the repository:

```bash
git clone https://github.com/yourusername/chatbuds-backend.git
cd chatbuds-backend
./mvnw clean package
java -jar target/chatbuds-backend-0.0.1-SNAPSHOT.jar
