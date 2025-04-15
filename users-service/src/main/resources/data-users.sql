-- Users Table
INSERT INTO users (user_id, first_name, last_name, age, height_in_cm, weight_in_kg, goal_description, daily_caloric_intake)
VALUES
    ('2dbdcb96-6479-430b-860f-bf99fd854940', 'Alice', 'Johnson', 25, 165, 60, 'Lose weight', 1800),
    ('1a0d6533-f13b-42fb-afd6-7dfd3eb95510', 'Bob', 'Smith', 30, 175, 75, 'Build muscle', 2500),
    ('7554db22-2104-4d9b-b80a-93c96ecd784a', 'Charlie', 'Davis', 22, 180, 70, 'Stay fit', 2200),
    ('941187dc-bf1f-4752-84bc-0026dc24bcd2', 'Dave', 'Miller', 28, 170, 68, 'Increase endurance', 2100),
    ('97aa92f3-7e4d-4f64-b67f-2762b6d19e0c', 'Eve', 'Wilson', 35, 160, 55, 'Maintain weight', 2000),
    ('6fc89a50-3d10-48b7-819c-dc5e40fca6e9', 'Frank', 'Anderson', 32, 178, 82, 'Reduce body fat', 2200),
    ('4d75e21a-8f9b-4063-bbb0-c2437a2c14b5', 'Grace', 'Taylor', 27, 163, 57, 'Improve flexibility', 1900),
    ('82a71d95-57c6-4d39-9cbe-f6a6b4c0b73a', 'Henry', 'Clark', 40, 183, 90, 'Lower cholesterol', 2100),
    ('3b9ef1d2-c6a8-47e5-85b0-df63b3951497', 'Isabella', 'Moore', 29, 168, 62, 'Train for marathon', 2300),
    ('5ae0f247-3d9b-4e58-ba3c-2f8d72ad32c1', 'Jack', 'Walker', 35, 176, 78, 'General fitness', 2400);

-- User Workout Days Table
INSERT INTO user_workout_days (user_id, workout_day)
VALUES
    (1, 'MONDAY'),
    (1, 'WEDNESDAY'),
    (1, 'FRIDAY'),
    (2, 'TUESDAY'),
    (2, 'THURSDAY'),
    (2, 'SATURDAY'),
    (3, 'MONDAY'),
    (3, 'THURSDAY'),
    (4, 'WEDNESDAY'),
    (4, 'FRIDAY'),
    (4, 'SUNDAY'),
    (5, 'TUESDAY'),
    (5, 'SATURDAY'),
    (6, 'MONDAY'),
    (6, 'WEDNESDAY'),
    (6, 'SATURDAY'),
    (7, 'TUESDAY'),
    (7, 'THURSDAY'),
    (8, 'MONDAY'),
    (8, 'FRIDAY'),
    (9, 'MONDAY'),
    (9, 'WEDNESDAY'),
    (9, 'FRIDAY'),
    (9, 'SUNDAY'),
    (10, 'TUESDAY'),
    (10, 'THURSDAY'),
    (10, 'SATURDAY');