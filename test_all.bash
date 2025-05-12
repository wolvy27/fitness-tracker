#!/bin/bash

# API Gateway base URL
API_GATEWAY_URL="http://localhost:8084"

# Test data
USER_ID="2dbdcb96-6479-43db-860f-bf99fd854940"  # Alice's user ID
DAILY_LOG_ID="dialbici-diel-41fi-gih1-iljik11im1n1"
MEAL_ID="alb2c3d4-e5f6-47g8-h91d-j1k213m4n5o6"  # Alice's breakfast
WORKOUT_ID="38f8492b-4d5a-4326-9981-32bffb132938"  # Alice's cardio workout

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# Function to print test results
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}SUCCESS${NC}: $2"
    else
        echo -e "${RED}FAILED${NC}: $2"
    fi
}

# Test 1: Get all users
echo "Testing GET /api/v1/users..."
response=$(curl -s -o /dev/null -w "%{http_code}" "${API_GATEWAY_URL}/api/v1/users")
if [ "$response" -eq 200 ]; then
    print_result 0 "Get all users"
else
    print_result 1 "Get all users (Status: $response)"
fi

# Test 2: Get specific user
echo "Testing GET /api/v1/users/${USER_ID}..."
response=$(curl -s -o /dev/null -w "%{http_code}" "${API_GATEWAY_URL}/api/v1/users/${USER_ID}")
if [ "$response" -eq 200 ]; then
    print_result 0 "Get user by ID"
else
    print_result 1 "Get user by ID (Status: $response)"
fi

# Test 3: Get daily logs for user
echo "Testing GET /api/v1/${USER_ID}/dailylogs..."
response=$(curl -s -o /dev/null -w "%{http_code}" "${API_GATEWAY_URL}/api/v1/${USER_ID}/dailylogs")
if [ "$response" -eq 200 ]; then
    print_result 0 "Get daily logs for user"
else
    print_result 1 "Get daily logs for user (Status: $response)"
fi

# Test 4: Get specific daily log
echo "Testing GET /api/v1/${USER_ID}/dailylogs/${DAILY_LOG_ID}..."
response=$(curl -s -o /dev/null -w "%{http_code}" "${API_GATEWAY_URL}/api/v1/${USER_ID}/dailylogs/${DAILY_LOG_ID}")
if [ "$response" -eq 200 ]; then
    print_result 0 "Get specific daily log"
else
    print_result 1 "Get specific daily log (Status: $response)"
fi

# Test 5: Check calories goal status (similar to your test example)
echo "Testing calories goal status..."
# First get the user's daily calorie intake goal
calorie_goal=$(curl -s "${API_GATEWAY_URL}/api/v1/users/${USER_ID}/dailycalorieintake" | jq -r '.')
if [ -z "$calorie_goal" ]; then
    calorie_goal=1800  # Default value if not found
fi

# Get the daily log and calculate total calories
daily_log=$(curl -s "${API_GATEWAY_URL}/api/v1/${USER_ID}/dailylogs/${DAILY_LOG_ID}")
breakfast_calories=$(echo "$daily_log" | jq -r '.breakFastCalories')
lunch_calories=$(echo "$daily_log" | jq -r '.lunchCalories')
dinner_calories=$(echo "$daily_log" | jq -r '.dinnerCalories')
total_calories=$((breakfast_calories + lunch_calories + dinner_calories))

# Check if within 10% of goal
lower_bound=$((calorie_goal * 90 / 100))
upper_bound=$((calorie_goal * 110 / 100))

if [ "$total_calories" -ge "$lower_bound" ] && [ "$total_calories" -le "$upper_bound" ]; then
    status="ACHIEVED"
else
    status="MISSED"
fi

# Verify the status in the response
response_status=$(echo "$daily_log" | jq -r '.metCaloriesGoal')
if [ "$response_status" == "$status" ]; then
    print_result 0 "Calories goal status check (Expected: $status, Actual: $response_status)"
else
    print_result 1 "Calories goal status check (Expected: $status, Actual: $response_status)"
fi

# Test 6: Get all meals
echo "Testing GET /api/v1/meals..."
response=$(curl -s -o /dev/null -w "%{http_code}" "${API_GATEWAY_URL}/api/v1/meals")
if [ "$response" -eq 200 ]; then
    print_result 0 "Get all meals"
else
    print_result 1 "Get all meals (Status: $response)"
fi

# Test 7: Get specific meal
echo "Testing GET /api/v1/meals/${MEAL_ID}..."
response=$(curl -s -o /dev/null -w "%{http_code}" "${API_GATEWAY_URL}/api/v1/meals/${MEAL_ID}")
if [ "$response" -eq 200 ]; then
    print_result 0 "Get meal by ID"
else
    print_result 1 "Get meal by ID (Status: $response)"
fi

# Test 8: Get all workouts
echo "Testing GET /api/v1/workouts..."
response=$(curl -s -o /dev/null -w "%{http_code}" "${API_GATEWAY_URL}/api/v1/workouts")
if [ "$response" -eq 200 ]; then
    print_result 0 "Get all workouts"
else
    print_result 1 "Get all workouts (Status: $response)"
fi

# Test 9: Get specific workout
echo "Testing GET /api/v1/workouts/${WORKOUT_ID}..."
response=$(curl -s -o /dev/null -w "%{http_code}" "${API_GATEWAY_URL}/api/v1/workouts/${WORKOUT_ID}")
if [ "$response" -eq 200 ]; then
    print_result 0 "Get workout by ID"
else
    print_result 1 "Get workout by ID (Status: $response)"
fi

# Test 10: Create a new user
echo "Testing POST /api/v1/users..."
new_user_data='{
    "firstName": "Test",
    "lastName": "User",
    "age": 30,
    "heightInCm": 175,
    "weightInKg": 70,
    "goalDescription": "Test goal",
    "dailyCaloricIntake": 2000,
    "workoutDays": ["MONDAY", "WEDNESDAY"]
}'

response=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "$new_user_data" "${API_GATEWAY_URL}/api/v1/users")
if [ "$response" -eq 201 ]; then
    print_result 0 "Create new user"
else
    print_result 1 "Create new user (Status: $response)"
fi