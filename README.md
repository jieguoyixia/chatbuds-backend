# Maze Solver (Java + Spring Boot)

A backend service that generates and solves mazes using classic pathfinding algorithms.  

---

## Features
- Generate random mazes 
- Solve using **DFS, BFS, Dijkstra, A\***  
- Step-by-step exploration output  
- REST API endpoints (`/generate`, `/solve`)  
- **Dockerized** for deployment

---

## Quick Start

### Run Locally
mvn spring-boot:run
### Run with Docker
docker build -t maze-solver .
docker run -p 8080:8080 maze-solver
