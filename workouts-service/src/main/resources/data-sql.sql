DELETE FROM workouts;

-- Workouts Table
-- Alice (Monday, Wednesday, Friday)
INSERT INTO workouts (workout_id, workout_name, workout_type, duration_in_minutes, workout_date)
VALUES
    ('38f8492b-4d5a-4326-9981-32bffb132938', 'Cardio', 'CARDIO', 45, '2025-03-10'), -- Monday
    ('b9c5431e-3a8d-45cf-916e-53dfe893a1cd', 'Strength', 'STRENGTH', 40, '2025-03-14'); -- Friday

-- Bob (Tuesday, Thursday, Saturday)
INSERT INTO workouts (workout_id, workout_name, workout_type, duration_in_minutes, workout_date)
VALUES
    ('fd8b9e10-1b3b-4d82-b2c6-4c9ed0fcb647', 'Strength', 'STRENGTH', 50, '2025-03-11'), -- Tuesday
    ('7ea2d214-8d2d-4f06-99e8-4cb3a2d896a3', 'Cardio', 'CARDIO', 60, '2025-03-16'); -- Skipped Thursday, did Saturday

-- Charlie (Monday, Thursday)
INSERT INTO workouts (workout_id, workout_name, workout_type, duration_in_minutes, workout_date)
VALUES
    ('c27f56a7-b2c5-4e4d-93b3-6d47300d4fd7', 'HIIT', 'HIGH_INTENSITY', 30, '2025-03-10'); -- Monday

-- Dave (Wednesday, Friday, Sunday)
INSERT INTO workouts (workout_id, workout_name, workout_type, duration_in_minutes, workout_date)
VALUES
    ('5b47d48a-65fd-4b1d-a0e1-cb948d3731f4', 'Cardio', 'AQUATIC', 45, '2025-03-12'), -- Wednesday (Swimming)
    ('2a5b7d19-4e09-4f60-a5cc-785987bcf3b0', 'Strength', 'STRENGTH', 40, '2025-03-16'); -- Sunday (Skipped Friday)

-- Eve (Tuesday, Saturday)
INSERT INTO workouts (workout_id, workout_name, workout_type, duration_in_minutes, workout_date)
VALUES
    ('abe3e1c8-6094-4fd6-a377-1c888efbc409', 'Yoga', 'FLEXIBILITY', 30, '2025-03-15'); -- Skipped Tuesday, did Saturday

-- Frank (Monday, Wednesday, Saturday)
INSERT INTO workouts (workout_id, workout_name, workout_type, duration_in_minutes, workout_date)
VALUES
    ('1b4fba67-2989-4b0e-91db-28dc8cbd8897', 'Strength', 'STRENGTH', 50, '2025-03-10'), -- Monday
    ('903c482d-3822-487e-a5e0-97e5b58b0a14', 'Cardio', 'CARDIO', 35, '2025-03-12'); -- Wednesday

-- Grace (Tuesday, Thursday)
INSERT INTO workouts (workout_id, workout_name, workout_type, duration_in_minutes, workout_date)
VALUES
    ('e34f45bb-87e5-4667-b6b9-3df3cbf830fa', 'Pilates', 'FLEXIBILITY', 40, '2025-03-11'); -- Tuesday

-- Henry (Monday, Friday)
INSERT INTO workouts (workout_id, workout_name, workout_type, duration_in_minutes, workout_date)
VALUES
    ('f75ad2be-097e-4827-b509-e59d95a83c58', 'Strength', 'STRENGTH', 45, '2025-03-10'), -- Monday
    ('08bcd44e-bbe1-456d-8b90-78f35f6787e7', 'Cardio', 'CARDIO', 25, '2025-03-14'); -- Friday

-- Isabella (Monday, Wednesday, Friday, Sunday)
INSERT INTO workouts (workout_id, workout_name, workout_type, duration_in_minutes, workout_date)
VALUES
    ('f6034af9-0291-46d6-bf47-3eb65efad13c', 'Endurance', 'CARDIO', 60, '2025-03-10'), -- Monday
    ('2c79cb07-b8cb-4066-bc5d-0d2fb88d8c34', 'Strength', 'STRENGTH', 50, '2025-03-14'); -- Friday

-- Jack (Tuesday, Thursday, Saturday)
INSERT INTO workouts (workout_id, workout_name, workout_type, duration_in_minutes, workout_date)
VALUES
    ('d964b991-d7e6-4371-9734-9e63307b65b5', 'Martial Arts', 'HIGH_INTENSITY', 50, '2025-03-11'), -- Tuesday
    ('97d49e44-8baf-4de5-91b6-b963e9f3a7cc', 'Flexibility', 'FLEXIBILITY', 30, '2025-03-15'); -- Skipped Thursday, did Saturday
