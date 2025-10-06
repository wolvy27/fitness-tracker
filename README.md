
# Fitness Tracker Backend

This project is an individual assignment for the **Web Services and Distributed Computing** class. It is a **microservices-based backend** that allows users to track fitness goals, log meals and workouts, and monitor daily goal completion.

---

## Features
- User management with customizable fitness goals  
- Logging of **Meals** and **Workouts** per user  
- Aggregated **Daily Log** to evaluate whether users met their daily goals  
- Microservices architecture to separate responsibilities and scale easily

---
## How to Run
- Open Docker Desktop
- Navigate fitness-tracker project directory
- Build the docker containaiers: `docker-compose build`
- Run the services: `docker-compose up -d`

---
## Tech Stack
- **Language:** Java
- **Framework:** Spring Boot
- **Databases:** 
  - MySQL (User Service)
  - PostgreSQL (Workout/Meal Services)
  - MongoDB (Daily Log Service)
- **Build Tool:** Gradle
- **Containerization:** Docker & Docker Compose
- **Architecture:** Microservices with API Gateway
- **Unit Tests:** JUnit 5, Mockito  
- **Integration Tests:** Spring Boot Test
- **API Testing:** Postman
- **Test Coverage:** JaCoCo

---

## Project Structure
```
fitness-tracker/
├── api-gateway/        # Exposes endpoints on port 8080
├── design/       # Contains Diagrams
├── user-service/       # Handles user and goal management
├── workout-service/    # Handles workout tracking
├── meal-service/       # Handles meal tracking
├── dailylog-service/  # Aggregates data and calculates goal 
└── README.md
```

---

## API Endpoints

All requests go through the API Gateway on `http://localhost:8080`

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/users` | Get all users |
| `GET` | `/api/v1/users/{userId}` | Get user by ID |
| `POST` | `/api/v1/users` | Create a new user |
| `PUT` | `/api/v1/users/{userId}` | Update user by ID |
| `DELETE` | `/api/v1/users/{userId}` | Delete user by ID |

### Meals
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/meals` | Get all meals |
| `GET` | `/api/v1/meals/{mealId}` | Get meal by ID |
| `POST` | `/api/v1/meals` | Create a new meal |
| `PUT` | `/api/v1/meals/{mealId}` | Update meal by ID |
| `DELETE` | `/api/v1/meals/{mealId}` | Delete meal by ID |

### Workouts
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/workouts` | Get all workouts |
| `GET` | `/api/v1/workouts/{workoutId}` | Get workout by ID |
| `POST` | `/api/v1/workouts` | Create a new workout |
| `PUT` | `/api/v1/workouts/{workoutId}` | Update workout by ID |
| `DELETE` | `/api/v1/workouts/{workoutId}` | Delete workout by ID |

### Daily Logs
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/users/{userId}/dailyLogs` | Get all daily logs for a user |
| `GET` | `/api/v1/users/{userId}/dailyLogs/{dailyLogId}` | Get specific daily log |
| `POST` | `/api/v1/users/{userId}/dailyLogs` | Create a new daily log |
| `PUT` | `/api/v1/users/{userId}/dailyLogs/{dailyLogId}` | Update daily log |
| `DELETE` | `/api/v1/users/{userId}/dailyLogs/{dailyLogId}` | Delete daily log |
