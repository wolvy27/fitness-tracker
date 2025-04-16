CREATE TABLE IF NOT EXISTS workouts (
                                        id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                        workout_id VARCHAR(36) UNIQUE,
    workout_name VARCHAR(50),
    workout_type VARCHAR(50),
    duration_in_minutes INTEGER,
    workout_date DATE
    );