#!/usr/bin/env bash
# Integration test script for Fitness Tracker Microservices

# Configuration
: ${HOST=localhost}
: ${PORT=8080}  # API Gateway port
API_BASE="http://$HOST:$PORT/api"

# Test data IDs
declare -A testIds=(
  [user]=""
  [workout]=""
  [meal]=""
  [dailylog]=""
)

# Helper functions
function assertCurl() {
  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  local result=$(eval $curlCmd)
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpCode" ]
  then
    echo "Test OK (HTTP Code: $httpCode)"
  else
    echo "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode"
    echo "- Failing command: $curlCmd"
    echo "- Response Body: $RESPONSE"
    exit 1
  fi
}

function assertEqual() {
  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual"
    exit 1
  fi
}

function testUrl() {
  url=$@
  if curl $url -ks -o /dev/null
  then
    echo "Ok"
    return 0
  else
    echo -n "not yet"
    return 1
  fi
}

function waitForService() {
  url=$@
  echo -n "Wait for: $url..."
  n=0
  until testUrl $url
  do
    n=$((n + 1))
    if [[ $n == 100 ]]
    then
      echo " Give up"
      exit 1
    else
      sleep 6
      echo -n ", retry #$n "
    fi
  done
}

# Setup test data
function setupTestdata() {
  # Create a test user
  body='{
    "firstName": "Test",
    "lastName": "User",
    "age": 30,
    "heightInCm": 175,
    "weightInKg": 70,
    "goalDescription": "Test goal",
    "dailyCaloricIntake": 2000,
    "workoutDays": ["MONDAY", "WEDNESDAY"]
  }'
  recreateUser "$body"

  # Create a test workout
  body='{
    "workoutName": "Test Workout",
    "workoutType": "CARDIO",
    "durationInMinutes": 45,
    "workoutDate": "2025-05-01"
  }'
  recreateWorkout "$body"

  # Create a test meal
  body='{
    "mealName": "Test Meal",
    "calories": 500,
    "mealDate": "2025-05-01",
    "mealType": "LUNCH"
  }'
  recreateMeal "$body"
}

# Data creation functions
function recreateUser() {
  local aggregate=$1
  echo "Creating user..."
  testIds[user]=$(curl -X POST "$API_BASE/v1/users" \
    -H "Content-Type: application/json" \
    -d "$aggregate" | jq -r '.userId')
  echo "Created user with id: ${testIds[user]}"
}

function recreateWorkout() {
  local aggregate=$1
  echo "Creating workout..."
  testIds[workout]=$(curl -X POST "$API_BASE/v1/workouts" \
    -H "Content-Type: application/json" \
    -d "$aggregate" | jq -r '.workoutId')
  echo "Created workout with id: ${testIds[workout]}"
}

function recreateMeal() {
  local aggregate=$1
  echo "Creating meal..."
  testIds[meal]=$(curl -X POST "$API_BASE/v1/meals" \
    -H "Content-Type: application/json" \
    -d "$aggregate" | jq -r '.mealId')
  echo "Created meal with id: ${testIds[meal]}"
}

# Test execution
echo "Starting tests..."
echo "API Gateway: $HOST:$PORT"

# Verify environment is ready
waitForService curl -X DELETE "$API_BASE/v1/users/00000000-0000-0000-0000-000000000000"

# Setup test data
setupTestdata

# Test low-level microservices through API Gateway
echo -e "\nTEST 1: Verify GET user endpoint"
assertCurl 200 "curl $API_BASE/v1/users/${testIds[user]} -s"
assertEqual "\"Test\"" $(echo $RESPONSE | jq ".firstName")

echo -e "\nTEST 2: Verify GET workout endpoint"
assertCurl 200 "curl $API_BASE/v1/workouts/${testIds[workout]} -s"
WORKOUT_NAME=$(echo $RESPONSE | jq -r '.workoutName')
echo "Extracted workout name: '$WORKOUT_NAME'"
assertEqual "Test Workout" "$WORKOUT_NAME"

echo -e "\nTEST 3: Verify GET meal endpoint"
assertCurl 200 "curl $API_BASE/v1/meals/${testIds[meal]} -s"
MEAL_NAME=$(echo $RESPONSE | jq -r '.mealName')
echo "Extracted meal name: '$MEAL_NAME'"
assertEqual "Test Meal" "$MEAL_NAME"

# Update the API base to include /v1
API_BASE="http://$HOST:$PORT/api/v1"

# Update the daily log creation and retrieval sections:

# Create daily log
echo -e "\nTEST 4: Create daily log"
body=$(cat <<EOF
{
  "workoutIdentifier": "${testIds[workout]}",
  "logDate": "2025-05-05",
  "breakfastIdentifier": "${testIds[meal]}",
  "lunchIdentifier": "${testIds[meal]}",
  "dinnerIdentifier": "${testIds[meal]}",
  "snacksIdentifier": ["${testIds[meal]}"]
}
EOF
)

# Use user-specific endpoint for creation
FULL_RESPONSE=$(curl -s -X POST "$API_BASE/users/${testIds[user]}/dailyLogs" \
  -H "Content-Type: application/json" \
  -d "$body")

# Print the full response for debugging and verification
echo -e "\n Full Response:"
echo "$FULL_RESPONSE" | jq .

# Extract the ID from the response
parsedId=$(echo "$FULL_RESPONSE" | jq -r '.dailyLogIdentifier // .dailyLogId // .id')

# Store and display the result
if [[ "$parsedId" == "null" || -z "$parsedId" ]]; then
    echo "X Failed to extract daily log ID!"
else
    testIds[dailylog]=$parsedId
    echo -e "\n Yay! Created daily log with ID: ${testIds[dailylog]}"
fi


# Verify GET daily log - use user-specific endpoint
echo -e "\nTEST 5: Verify GET daily log endpoint"
assertCurl 200 "curl $API_BASE/users/${testIds[user]}/dailyLogs/${testIds[dailylog]} -s"
USER_LASTNAME=$(echo $RESPONSE | jq -r '.userLastName')
echo "Extracted user last name: '$USER_LASTNAME'"
assertEqual "User" "$USER_LASTNAME"  # Or whatever your expected value is

# Verify GET all daily logs for user
echo -e "\nTEST 6: Verify GET all daily logs for user"
assertCurl 200 "curl $API_BASE/users/${testIds[user]}/dailyLogs -s"
assertEqual 1 $(echo $RESPONSE | jq ". | length")
echo -e "\nTEST 7: Verify aggregate invariant - workout goal status"
# Should be ACHIEVED since we created a workout for a workout day (Monday)
assertEqual "\"ACHIEVED\"" $(echo $RESPONSE | jq ".[0].metWorkoutGoal")

echo -e "\nTEST 8: Verify aggregate invariant - calorie goal status"
# Should be ACHIEVED since total calories (500*3=1500) is within 90-110% of goal (2000)
assertEqual "\"ACHIEVED\"" $(echo $RESPONSE | jq ".[0].metCaloriesGoal")

echo -e "\nTEST 9: Update daily log"
body=$(cat <<EOF
{
  "workoutIdentifier": "None",
  "logDate": "2025-05-05",
  "breakfastIdentifier": "${testIds[meal]}",
  "lunchIdentifier": "${testIds[meal]}",
  "dinnerIdentifier": "${testIds[meal]}",
  "snacksIdentifier": []
}
EOF
)
assertCurl 200 "curl -X PUT $API_BASE/users/${testIds[user]}/dailyLogs/${testIds[dailylog]} \
  -H \"Content-Type: application/json\" \
  -d '$body' -s"

echo -e "\nTEST 10: Verify workout goal status after update"
# Should be MISSED since we set workout to "None" on a workout day
assertCurl 200 "curl $API_BASE/users/${testIds[user]}/dailyLogs/${testIds[dailylog]} -s"
assertEqual "\"MISSED\"" $(echo $RESPONSE | jq ".metWorkoutGoal")

echo -e "\nTEST 11: Delete daily log"
assertCurl 204 "curl -X DELETE $API_BASE/users/${testIds[user]}/dailyLogs/${testIds[dailylog]} -s"

echo -e "\nTEST 12: Verify daily log is deleted"
assertCurl 404 "curl $API_BASE/users/${testIds[user]}/dailyLogs/${testIds[dailylog]} -s"

echo -e "\nAll tests passed successfully!"