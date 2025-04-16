DROP TABLE IF EXISTS meals;

CREATE TABLE IF NOT EXISTS meals (
                                     id SERIAL,
                                     meal_id VARCHAR(36) UNIQUE,
    meal_name VARCHAR(50),
    calories INTEGER,
    meal_date DATE,
    meal_type VARCHAR(50),
    PRIMARY KEY (id)
    );
