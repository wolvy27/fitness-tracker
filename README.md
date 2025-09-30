
# Fitness Tracker Backend

This project is an individual assignment for the **Web Services and Distributed Computing** class. It is a **microservices-based backend** that allows users to track fitness goals, log meals and workouts, and monitor daily goal completion.

---

## Features
- User management with customizable fitness goals  
- Logging of **Meals** and **Workouts** per user  
- Aggregated **Daily Log** to evaluate whether users met their daily goals  
- Microservices architecture to separate responsibilities and scale easily  

---

## Project Structure
```
fitness-tracker/
├── api-gateway/        # Exposes endpoints on port 8080
├── user-service/       # Contains Diagrams
├── user-service/       # Handles user and goal management
├── workout-service/    # Handles workout tracking
├── meal-service/       # Handles meal tracking
├── dailylog-service/  # Aggregates data and calculates goal 
└── README.md
```
