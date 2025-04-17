CREATE TABLE IF NOT EXISTS users (
                                     id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                     user_id VARCHAR(36) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    age INTEGER NOT NULL,
    height_in_cm INTEGER NOT NULL,
    weight_in_kg INTEGER NOT NULL,
    goal_description VARCHAR(50) NOT NULL,
    daily_caloric_intake INTEGER NOT NULL
    );

CREATE TABLE IF NOT EXISTS user_workout_days (
                                                 user_id INTEGER NOT NULL,
                                                 workout_day VARCHAR(10) NOT NULL
    );