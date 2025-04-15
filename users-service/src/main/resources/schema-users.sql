CREATE TABLE IF NOT EXISTS users (
                                     id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                     user_id VARCHAR(36) UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    age INTEGER,
    height_in_cm INTEGER,
    weight_in_kg INTEGER,
    goal_description VARCHAR(50),
    daily_caloric_intake INTEGER
    );

CREATE TABLE IF NOT EXISTS user_workout_days (
                                                 user_id INTEGER,
                                                 workout_day VARCHAR(10)
    );