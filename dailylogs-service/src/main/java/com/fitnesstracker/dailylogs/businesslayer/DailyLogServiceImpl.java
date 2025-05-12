package com.fitnesstracker.dailylogs.businesslayer;


import com.fitnesstracker.dailylogs.dataaccesslayer.DailyLog;
import com.fitnesstracker.dailylogs.dataaccesslayer.DailyLogIdentifier;
import com.fitnesstracker.dailylogs.dataaccesslayer.DailyLogRepository;
import com.fitnesstracker.dailylogs.dataaccesslayer.GoalStatus;
import com.fitnesstracker.dailylogs.datamapperlayer.DailyLogRequestMapper;
import com.fitnesstracker.dailylogs.datamapperlayer.DailyLogResponseMapper;
import com.fitnesstracker.dailylogs.domainclientlayer.meals.MealModel;
import com.fitnesstracker.dailylogs.domainclientlayer.meals.MealsServiceClient;
import com.fitnesstracker.dailylogs.domainclientlayer.users.UserModel;
import com.fitnesstracker.dailylogs.domainclientlayer.users.UsersServiceClient;
import com.fitnesstracker.dailylogs.domainclientlayer.workouts.WorkoutModel;
import com.fitnesstracker.dailylogs.domainclientlayer.workouts.WorkoutsServiceClient;
import com.fitnesstracker.dailylogs.utils.exceptions.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.fitnesstracker.dailylogs.presentationlayer.DailyLogRequestModel;
import com.fitnesstracker.dailylogs.presentationlayer.DailyLogResponseModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DailyLogServiceImpl implements DailyLogService {

    private final DailyLogRepository dailyLogRepository;

    private final UsersServiceClient usersServiceClient;

    private final WorkoutsServiceClient workoutsServiceClient;

    private final MealsServiceClient mealsServiceClient;

    private final DailyLogRequestMapper dailyLogRequestMapper;

    private final DailyLogResponseMapper dailyLogResponseMapper;

    public DailyLogServiceImpl(DailyLogRepository dailyLogRepository, UsersServiceClient usersServiceClient, WorkoutsServiceClient workoutsServiceClient,
                               MealsServiceClient mealsServiceClient, DailyLogRequestMapper dailyLogRequestMapper, DailyLogResponseMapper dailyLogResponseMapper) {
        this.dailyLogRepository = dailyLogRepository;
        this.usersServiceClient = usersServiceClient;
        this.workoutsServiceClient = workoutsServiceClient;
        this.mealsServiceClient = mealsServiceClient;
        this.dailyLogRequestMapper = dailyLogRequestMapper;
        this.dailyLogResponseMapper = dailyLogResponseMapper;
    }

    private WorkoutModel createEmptyWorkout() {
        return WorkoutModel.builder()
                .workoutId("None")
                .workoutName("")
                .workoutDurationInMinutes(0)
                .build();
    }

    private MealModel createEmptyMeal() {
        return MealModel.builder()
                .mealId("None")
                .mealName("")
                .mealCalorie(0)
                .build();
    }



    @Override
    public List<DailyLogResponseModel> getDailyLogs(String userId) {
        UserModel foundUser = usersServiceClient.getUserByUserId(userId);
        if (foundUser == null) {
            throw new InvalidInputException("User not found");
        }

        List<DailyLog> dailyLogs = dailyLogRepository.findAllByUserModel_userId(userId);
        List<DailyLogResponseModel> dailyLogResponseModels = new ArrayList<>();
        int userCaloriesGoal = usersServiceClient.getDailyCalorieIntake(userId);

        for (DailyLog dailyLog : dailyLogs) {
            int totalCaloriesConsumed = 0;
            DailyLogResponseModel responseModel = dailyLogResponseMapper.entityToResponseModel(dailyLog);

            // Set user names
            responseModel.setUserFirstName(foundUser.getFirstName());
            responseModel.setUserLastName(foundUser.getLastName());

            // Handle workout
            WorkoutModel workout;
            if (dailyLog.getWorkoutModel() != null) {
                workout = dailyLog.getWorkoutModel();
            } else {
                workout = createEmptyWorkout();
            }

            responseModel.setWorkoutName(workout.getWorkoutName());
            responseModel.setWorkoutDurationInMinutes(workout.getWorkoutDurationInMinutes());

            // Handle workout goal status
            List<String> userWorkoutDays = usersServiceClient.getWorkoutDays(userId);
            String dayOfTheWeek = dailyLog.getLogDate().getDayOfWeek().toString();

            if (dailyLog.getLogDate().equals(LocalDate.now())) {
                responseModel.setMetWorkoutGoal(GoalStatus.IN_PROGRESS);
            }
            else if (userWorkoutDays.contains(dayOfTheWeek)) {
                if (workout.getWorkoutId().equals("None")) {
                    responseModel.setMetWorkoutGoal(GoalStatus.MISSED);
                } else {
                    responseModel.setMetWorkoutGoal(GoalStatus.ACHIEVED);
                }
            } else {
                responseModel.setMetWorkoutGoal(GoalStatus.NONE);
            }

            // Handle meals
            MealModel breakfast;
            if (dailyLog.getBreakfast() != null) {
                breakfast = dailyLog.getBreakfast();
            } else {
                breakfast = createEmptyMeal();
            }

            MealModel lunch;
            if (dailyLog.getLunch() != null) {
                lunch = dailyLog.getLunch();
            } else {
                lunch = createEmptyMeal();
            }

            MealModel dinner;
            if (dailyLog.getDinner() != null) {
                dinner = dailyLog.getDinner();
            } else {
                dinner = createEmptyMeal();
            }

            List<MealModel> snacks;
            if (dailyLog.getSnacks() != null) {
                snacks = dailyLog.getSnacks();
            } else {
                snacks = Collections.emptyList();
            }

            // Calculate calories with null checks
            totalCaloriesConsumed = (breakfast.getMealCalorie() != null ? breakfast.getMealCalorie() : 0)
                    + (lunch.getMealCalorie() != null ? lunch.getMealCalorie() : 0)
                    + (dinner.getMealCalorie() != null ? dinner.getMealCalorie() : 0);

            for (MealModel snack : snacks) {
                totalCaloriesConsumed += (snack.getMealCalorie() != null ? snack.getMealCalorie() : 0);
            }

            // Set calorie goal status
            int lowerBound = (int) Math.floor(userCaloriesGoal * 0.9);
            int upperBound = (int) Math.ceil(userCaloriesGoal * 1.1);

            if (dailyLog.getLogDate().equals(LocalDate.now())) {
                responseModel.setMetCaloriesGoal(GoalStatus.IN_PROGRESS);
            } else if (totalCaloriesConsumed >= lowerBound && totalCaloriesConsumed <= upperBound) {
                responseModel.setMetCaloriesGoal(GoalStatus.ACHIEVED);
            } else {
                responseModel.setMetCaloriesGoal(GoalStatus.MISSED);
            }


            // Ensure meal calorie fields are never null in response
            responseModel.setBreakfastCalories(breakfast.getMealCalorie() != null ? breakfast.getMealCalorie() : 0);
            responseModel.setLunchCalories(lunch.getMealCalorie() != null ? lunch.getMealCalorie() : 0);
            responseModel.setDinnerCalories(dinner.getMealCalorie() != null ? dinner.getMealCalorie() : 0);

            dailyLogResponseModels.add(responseModel);
        }
        return dailyLogResponseModels;
    }

    @Override
    public DailyLogResponseModel getDailyLogByDailyLogId(String dailyLogId, String userId) {
        UserModel foundUser = usersServiceClient.getUserByUserId(userId);
        if (foundUser == null) {
            throw new InvalidInputException("User not found");
        }

        DailyLog dailyLog = dailyLogRepository.findDailyLogByDailyLogIdentifier_DailyLogIdAndUserModel_userId(dailyLogId, userId);
        if (dailyLog == null) {
            throw new InvalidInputException("DailyLog with id " + dailyLogId + " not found for user " + userId);
        }
        int userCaloriesGoal = usersServiceClient.getDailyCalorieIntake(userId);

        // Map DailyLog to Response Model
        DailyLogResponseModel responseModel = dailyLogResponseMapper.entityToResponseModel(dailyLog);

        // Set user names from the found user
        responseModel.setUserFirstName(foundUser.getFirstName());
        responseModel.setUserLastName(foundUser.getLastName());

        // Handle workout
        WorkoutModel workout;
        if (dailyLog.getWorkoutModel() != null) {
            workout = dailyLog.getWorkoutModel();
            responseModel.setWorkoutName(workout.getWorkoutName());
            responseModel.setWorkoutDurationInMinutes(workout.getWorkoutDurationInMinutes());
        } else {
            workout = createEmptyWorkout();
            responseModel.setWorkoutName(workout.getWorkoutName());
            responseModel.setWorkoutDurationInMinutes(workout.getWorkoutDurationInMinutes());
        }

        // Handle workout goal status
        List<String> userWorkoutDays = usersServiceClient.getWorkoutDays(userId);
        String dayOfTheWeek = dailyLog.getLogDate().getDayOfWeek().toString();

        if (dailyLog.getLogDate().equals(LocalDate.now())) {
            responseModel.setMetWorkoutGoal(GoalStatus.IN_PROGRESS);
        } else if (userWorkoutDays.contains(dayOfTheWeek)) {
            if (workout.getWorkoutId().equals("None")) {
                responseModel.setMetWorkoutGoal(GoalStatus.MISSED);
            } else {
                responseModel.setMetWorkoutGoal(GoalStatus.ACHIEVED);
            }
        } else {
            responseModel.setMetWorkoutGoal(GoalStatus.NONE);
        }

        // Handle meals and calculate calories with null checks
        int totalCaloriesConsumed = 0;

        MealModel breakfast;
        if (dailyLog.getBreakfast() != null) {
            breakfast = dailyLog.getBreakfast();
        } else {
            breakfast = createEmptyMeal();
        }
        totalCaloriesConsumed += (breakfast.getMealCalorie() != null ? breakfast.getMealCalorie() : 0);

        MealModel lunch;
        if (dailyLog.getLunch() != null) {
            lunch = dailyLog.getLunch();
        } else {
            lunch = createEmptyMeal();
        }
        totalCaloriesConsumed += (lunch.getMealCalorie() != null ? lunch.getMealCalorie() : 0);

        MealModel dinner;
        if (dailyLog.getDinner() != null) {
            dinner = dailyLog.getDinner();
        } else {
            dinner = createEmptyMeal();
        }
        totalCaloriesConsumed += (dinner.getMealCalorie() != null ? dinner.getMealCalorie() : 0);

        List<MealModel> snacks;
        if (dailyLog.getSnacks() != null) {
            snacks = dailyLog.getSnacks();
        } else {
            snacks = Collections.emptyList();
        }

        for (MealModel snack : snacks) {
            totalCaloriesConsumed += (snack.getMealCalorie() != null ? snack.getMealCalorie() : 0);
        }

        // Set calorie goal status
        int lowerBound = (int) Math.floor(userCaloriesGoal * 0.9);
        int upperBound = (int) Math.ceil(userCaloriesGoal * 1.1);

        if (dailyLog.getLogDate().equals(LocalDate.now())) {
            responseModel.setMetCaloriesGoal(GoalStatus.IN_PROGRESS);
        } else if (totalCaloriesConsumed >= lowerBound && totalCaloriesConsumed <= upperBound) {
            responseModel.setMetCaloriesGoal(GoalStatus.ACHIEVED);
        } else {
            responseModel.setMetCaloriesGoal(GoalStatus.MISSED);
        }

        // Ensure meal calorie fields are never null in response
        responseModel.setBreakfastCalories(breakfast.getMealCalorie() != null ? breakfast.getMealCalorie() : 0);
        responseModel.setLunchCalories(lunch.getMealCalorie() != null ? lunch.getMealCalorie() : 0);
        responseModel.setDinnerCalories(dinner.getMealCalorie() != null ? dinner.getMealCalorie() : 0);

        return responseModel;
    }

    @Override
    public DailyLogResponseModel addDailyLog(DailyLogRequestModel newDailyLogData, String userId) {
        // Validate user exists
        UserModel foundUser = usersServiceClient.getUserByUserId(userId);
        if (foundUser == null) {
            throw new InvalidInputException("User with id " + userId + " not found");
        }

        // Handle workout
        WorkoutModel workout;
        if (newDailyLogData.getWorkoutIdentifier() != null && !newDailyLogData.getWorkoutIdentifier().equals("None")) {
            workout = workoutsServiceClient.getWorkoutByWorkoutId(newDailyLogData.getWorkoutIdentifier());
            if (workout == null) {
                throw new InvalidInputException("Workout with id " + newDailyLogData.getWorkoutIdentifier() + " not found");
            }
            if (workout.getWorkoutDurationInMinutes() == null) {
                workout.setWorkoutDurationInMinutes(workoutsServiceClient.getWorkoutDuration(newDailyLogData.getWorkoutIdentifier()));
            }
        } else {
            workout = createEmptyWorkout();
        }

        // Handle meals - get actual calorie values from meal service
        MealModel breakfast;
        if (newDailyLogData.getBreakfastIdentifier() != null && !newDailyLogData.getBreakfastIdentifier().isEmpty()) {
            breakfast = mealsServiceClient.getMealByMealId(newDailyLogData.getBreakfastIdentifier());
            if (breakfast == null) {
                throw new InvalidInputException("Breakfast meal with id " + newDailyLogData.getBreakfastIdentifier() + " not found");
            }
            if (breakfast.getMealCalorie() == null) {
                breakfast.setMealCalorie(mealsServiceClient.getCalories(newDailyLogData.getBreakfastIdentifier()));
            }

        } else {
            breakfast = createEmptyMeal();
        }

        MealModel lunch;
        if (newDailyLogData.getLunchIdentifier() != null && !newDailyLogData.getLunchIdentifier().isEmpty()) {
            lunch = mealsServiceClient.getMealByMealId(newDailyLogData.getLunchIdentifier());
            if (lunch == null) {
                throw new InvalidInputException("Lunch meal with id " + newDailyLogData.getLunchIdentifier() + " not found");
            }
            if (lunch.getMealCalorie() == null) {
                lunch.setMealCalorie(mealsServiceClient.getCalories(newDailyLogData.getLunchIdentifier()));
            }
        } else {
            lunch = createEmptyMeal();
        }

        MealModel dinner;
        if (newDailyLogData.getDinnerIdentifier() != null && !newDailyLogData.getDinnerIdentifier().isEmpty() &&
                !newDailyLogData.getDinnerIdentifier().equals("None")) {
            dinner = mealsServiceClient.getMealByMealId(newDailyLogData.getDinnerIdentifier());
            if (dinner == null) {
                throw new InvalidInputException("Dinner meal with id " + newDailyLogData.getDinnerIdentifier() + " not found");
            }
            if (dinner.getMealCalorie() == null) {
                dinner.setMealCalorie(mealsServiceClient.getCalories(newDailyLogData.getDinnerIdentifier()));
            }
        } else {
            dinner = createEmptyMeal();
        }

        // Handle snacks
        List<MealModel> snacks = new ArrayList<>();
        if (newDailyLogData.getSnacksIdentifier() != null) {
            for (String snackId : newDailyLogData.getSnacksIdentifier()) {
                if (snackId != null && !snackId.isEmpty()) {
                    MealModel snack = mealsServiceClient.getMealByMealId(snackId);
                    if (snack == null) {
                        throw new InvalidInputException("Snack meal with id " + snackId + " not found");
                    }
                    if (snack.getMealCalorie() == null) {
                        snack.setMealCalorie(mealsServiceClient.getCalories(snackId));
                    }
                    snacks.add(snack);
                }
            }
        }

        // Check for existing log
        DailyLog existingDailyLog = dailyLogRepository.findDailyLogByLogDateAndUserModel_userId(newDailyLogData.getLogDate(), userId);
        if (existingDailyLog != null) {
            throw new InvalidInputException("Daily log already exists for date " + newDailyLogData.getLogDate());
        }

        // Create and save daily log
        DailyLog dailyLog = dailyLogRequestMapper.requestModelToEntity(newDailyLogData);
        dailyLog.setUserModel(foundUser);
        dailyLog.setWorkoutModel(workout);
        dailyLog.setBreakfast(breakfast);
        dailyLog.setLunch(lunch);
        dailyLog.setDinner(dinner);
        dailyLog.setSnacks(snacks.isEmpty() ? Collections.emptyList() : snacks);

        // Calculate workout goal status
        List<String> userWorkoutDays = usersServiceClient.getWorkoutDays(userId);
        String dayOfTheWeek = dailyLog.getLogDate().getDayOfWeek().toString();

        if (dailyLog.getLogDate().equals(LocalDate.now())) {
            dailyLog.setMetWorkoutGoal(GoalStatus.IN_PROGRESS);
        } else if (userWorkoutDays.contains(dayOfTheWeek)) {
            dailyLog.setMetWorkoutGoal(workout.getWorkoutId().equals("None") ?
                    GoalStatus.MISSED : GoalStatus.ACHIEVED);
        } else {
            dailyLog.setMetWorkoutGoal(GoalStatus.NONE);
        }

        // Calculate calories goal status
        int totalCaloriesConsumed = (breakfast.getMealCalorie() != null ? breakfast.getMealCalorie() : 0) +
                (lunch.getMealCalorie() != null ? lunch.getMealCalorie() : 0) +
                (dinner.getMealCalorie() != null ? dinner.getMealCalorie() : 0);

        for (MealModel snack : snacks) {
            totalCaloriesConsumed += (snack.getMealCalorie() != null ? snack.getMealCalorie() : 0);
        }

        int userCaloriesGoal = usersServiceClient.getDailyCalorieIntake(userId);
        if (dailyLog.getLogDate().equals(LocalDate.now())) {
            dailyLog.setMetCaloriesGoal(GoalStatus.IN_PROGRESS);
        } else {
            int lowerBound = (int) Math.floor(userCaloriesGoal * 0.9);
            int upperBound = (int) Math.ceil(userCaloriesGoal * 1.1);
            dailyLog.setMetCaloriesGoal((totalCaloriesConsumed >= lowerBound && totalCaloriesConsumed <= upperBound) ?
                    GoalStatus.ACHIEVED : GoalStatus.MISSED);
        }


        // Calculate daily goals status
        if (dailyLog.getMetCaloriesGoal() == GoalStatus.ACHIEVED &&
                dailyLog.getMetWorkoutGoal() == GoalStatus.ACHIEVED) {
            dailyLog.setMetDailyGoals(GoalStatus.ACHIEVED);
        } else if (dailyLog.getMetCaloriesGoal() == GoalStatus.MISSED ||
                dailyLog.getMetWorkoutGoal() == GoalStatus.MISSED) {
            dailyLog.setMetDailyGoals(GoalStatus.MISSED);
        } else {
            dailyLog.setMetDailyGoals(GoalStatus.NONE);
        }

        DailyLogIdentifier dailyLogIdentifier = new DailyLogIdentifier();
        dailyLog.setDailyLogIdentifier(dailyLogIdentifier);
        dailyLog = dailyLogRepository.save(dailyLog);

        // Map to response model
        DailyLogResponseModel response = dailyLogResponseMapper.entityToResponseModel(dailyLog);
        response.setDailyLogIdentifier(dailyLogIdentifier.getDailyLogId());

        // Set user names
        response.setUserFirstName(foundUser.getFirstName());
        response.setUserLastName(foundUser.getLastName());

        // Set workout details
        response.setWorkoutName(workout.getWorkoutName());
        response.setWorkoutDurationInMinutes(workout.getWorkoutDurationInMinutes());

        // Set meal details with actual calorie values
        response.setBreakfastName(breakfast.getMealName());
        response.setBreakfastCalories(breakfast.getMealCalorie() != null ? breakfast.getMealCalorie() : 0);
        response.setLunchName(lunch.getMealName());
        response.setLunchCalories(lunch.getMealCalorie() != null ? lunch.getMealCalorie() : 0);
        response.setDinnerName(dinner.getMealName());
        response.setDinnerCalories(dinner.getMealCalorie() != null ? dinner.getMealCalorie() : 0);

        // Set empty arrays for snacks if null
        if (response.getSnacksIdentifier() == null) {
            response.setSnacksIdentifier(Collections.emptyList());
        }
        if (response.getSnacksName() == null) {
            response.setSnacksName(Collections.emptyList());
        }
        if (response.getSnacksCalories() == null) {
            response.setSnacksCalories(Collections.emptyList());
        }

        return response;
    }

    @Override
    public DailyLogResponseModel updateDailyLog(DailyLogRequestModel newDailyLogData, String dailyLogId, String userId) {
        // Validate user exists
        UserModel foundUser = usersServiceClient.getUserByUserId(userId);
        if (foundUser == null) {
            throw new InvalidInputException("User with id " + userId + " not found");
        }

        // Get existing daily log
        DailyLog existingDailyLog = dailyLogRepository.findDailyLogByDailyLogIdentifier_DailyLogIdAndUserModel_userId(dailyLogId, userId);
        if (existingDailyLog == null) {
            throw new InvalidInputException("Daily log with id " + dailyLogId + " not found for user " + userId);
        }

        // Handle workout
        WorkoutModel workout;
        if (newDailyLogData.getWorkoutIdentifier() != null && !newDailyLogData.getWorkoutIdentifier().equals("None")) {
            workout = workoutsServiceClient.getWorkoutByWorkoutId(newDailyLogData.getWorkoutIdentifier());
            if (workout == null) {
                throw new InvalidInputException("Workout with id " + newDailyLogData.getWorkoutIdentifier() + " not found");
            }
            if (workout.getWorkoutDurationInMinutes() == null) {
                workout.setWorkoutDurationInMinutes(workoutsServiceClient.getWorkoutDuration(newDailyLogData.getWorkoutIdentifier()));
            }

        } else {
            workout = createEmptyWorkout();
        }

        // Handle meals - get actual calorie values from meal service
        MealModel breakfast;
        if (newDailyLogData.getBreakfastIdentifier() != null && !newDailyLogData.getBreakfastIdentifier().isEmpty()) {
            breakfast = mealsServiceClient.getMealByMealId(newDailyLogData.getBreakfastIdentifier());
            if (breakfast == null) {
                throw new InvalidInputException("Breakfast meal with id " + newDailyLogData.getBreakfastIdentifier() + " not found");
            }
            if (breakfast.getMealCalorie() == null) {
                breakfast.setMealCalorie(mealsServiceClient.getCalories(newDailyLogData.getBreakfastIdentifier()));
            }

        } else {
            breakfast = createEmptyMeal();
        }

        MealModel lunch;
        if (newDailyLogData.getLunchIdentifier() != null && !newDailyLogData.getLunchIdentifier().isEmpty()) {
            lunch = mealsServiceClient.getMealByMealId(newDailyLogData.getLunchIdentifier());
            if (lunch == null) {
                throw new InvalidInputException("Lunch meal with id " + newDailyLogData.getLunchIdentifier() + " not found");
            }
            if (lunch.getMealCalorie() == null) {
                lunch.setMealCalorie(mealsServiceClient.getCalories(newDailyLogData.getLunchIdentifier()));
            }
        } else {
            lunch = createEmptyMeal();
        }

        MealModel dinner;
        if (newDailyLogData.getDinnerIdentifier() != null && !newDailyLogData.getDinnerIdentifier().isEmpty() &&
                !newDailyLogData.getDinnerIdentifier().equals("None")) {
            dinner = mealsServiceClient.getMealByMealId(newDailyLogData.getDinnerIdentifier());
            if (dinner == null) {
                throw new InvalidInputException("Dinner meal with id " + newDailyLogData.getDinnerIdentifier() + " not found");
            }
            if (dinner.getMealCalorie() == null) {
                dinner.setMealCalorie(mealsServiceClient.getCalories(newDailyLogData.getDinnerIdentifier()));
            }
        } else {
            dinner = createEmptyMeal();
        }

        // Handle snacks
        List<MealModel> snacks = new ArrayList<>();
        if (newDailyLogData.getSnacksIdentifier() != null) {
            for (String snackId : newDailyLogData.getSnacksIdentifier()) {
                if (snackId != null && !snackId.isEmpty()) {
                    MealModel snack = mealsServiceClient.getMealByMealId(snackId);
                    if (snack == null) {
                        throw new InvalidInputException("Snack meal with id " + snackId + " not found");
                    }
                    if (snack.getMealCalorie() == null) {
                        snack.setMealCalorie(mealsServiceClient.getCalories(snackId));
                    }
                    snacks.add(snack);
                }
            }
        }

        // Update the existing log
        existingDailyLog.setLogDate(newDailyLogData.getLogDate());
        existingDailyLog.setWorkoutModel(workout);
        existingDailyLog.setBreakfast(breakfast);
        existingDailyLog.setLunch(lunch);
        existingDailyLog.setDinner(dinner);
        existingDailyLog.setSnacks(snacks.isEmpty() ? Collections.emptyList() : snacks);

        // Calculate workout goal status
        List<String> userWorkoutDays = usersServiceClient.getWorkoutDays(userId);
        String dayOfTheWeek = existingDailyLog.getLogDate().getDayOfWeek().toString();

        if (existingDailyLog.getLogDate().equals(LocalDate.now())) {
            existingDailyLog.setMetWorkoutGoal(GoalStatus.IN_PROGRESS);
        } else if (userWorkoutDays.contains(dayOfTheWeek)) {
            existingDailyLog.setMetWorkoutGoal(workout.getWorkoutId().equals("None") ?
                    GoalStatus.MISSED : GoalStatus.ACHIEVED);
        } else {
            existingDailyLog.setMetWorkoutGoal(GoalStatus.NONE);
        }

        // Calculate calories goal status
        int totalCaloriesConsumed = (breakfast.getMealCalorie() != null ? breakfast.getMealCalorie() : 0) +
                (lunch.getMealCalorie() != null ? lunch.getMealCalorie() : 0) +
                (dinner.getMealCalorie() != null ? dinner.getMealCalorie() : 0);

        for (MealModel snack : snacks) {
            totalCaloriesConsumed += (snack.getMealCalorie() != null ? snack.getMealCalorie() : 0);
        }

        int userCaloriesGoal = usersServiceClient.getDailyCalorieIntake(userId);
        if (existingDailyLog.getLogDate().equals(LocalDate.now())) {
            existingDailyLog.setMetCaloriesGoal(GoalStatus.IN_PROGRESS);
        } else {
            int lowerBound = (int) Math.floor(userCaloriesGoal * 0.9);
            int upperBound = (int) Math.ceil(userCaloriesGoal * 1.1);
            existingDailyLog.setMetCaloriesGoal((totalCaloriesConsumed >= lowerBound && totalCaloriesConsumed <= upperBound) ?
                    GoalStatus.ACHIEVED : GoalStatus.MISSED);
        }


        // Calculate daily goals status
        if (existingDailyLog.getMetCaloriesGoal() == GoalStatus.ACHIEVED &&
                existingDailyLog.getMetWorkoutGoal() == GoalStatus.ACHIEVED) {
            existingDailyLog.setMetDailyGoals(GoalStatus.ACHIEVED);
        } else if (existingDailyLog.getMetCaloriesGoal() == GoalStatus.MISSED ||
                existingDailyLog.getMetWorkoutGoal() == GoalStatus.MISSED) {
            existingDailyLog.setMetDailyGoals(GoalStatus.MISSED);
        } else {
            existingDailyLog.setMetDailyGoals(GoalStatus.NONE);
        }

        DailyLog updatedDailyLog = dailyLogRepository.save(existingDailyLog);

        // Map to response model
        DailyLogResponseModel response = dailyLogResponseMapper.entityToResponseModel(updatedDailyLog);

        // Set user names
        response.setUserFirstName(foundUser.getFirstName());
        response.setUserLastName(foundUser.getLastName());

        // Set workout details
        response.setWorkoutName(workout.getWorkoutName());
        response.setWorkoutDurationInMinutes(workout.getWorkoutDurationInMinutes());

        // Set meal details with actual calorie values
        response.setBreakfastName(breakfast.getMealName());
        response.setBreakfastCalories(breakfast.getMealCalorie() != null ? breakfast.getMealCalorie() : 10);
        response.setLunchName(lunch.getMealName());
        response.setLunchCalories(lunch.getMealCalorie() != null ? lunch.getMealCalorie() : 10);
        response.setDinnerName(dinner.getMealName());
        response.setDinnerCalories(dinner.getMealCalorie() != null ? dinner.getMealCalorie() : 0);

        // Set empty arrays for snacks if null
        if (response.getSnacksIdentifier() == null) {
            response.setSnacksIdentifier(Collections.emptyList());
        }
        if (response.getSnacksName() == null) {
            response.setSnacksName(Collections.emptyList());
        }
        if (response.getSnacksCalories() == null) {
            response.setSnacksCalories(Collections.emptyList());
        }

        // Ensure the identifier is set in the response
        response.setDailyLogIdentifier(dailyLogId);

        return response;
    }

    @Override
    public void deleteDailyLog(String dailyLogId, String userId) {
        UserModel foundUser = usersServiceClient.getUserByUserId(userId);
        if (foundUser == null) {
            throw new InvalidInputException("User with id " + userId + " not found");
        }

        DailyLog existingDailyLog = dailyLogRepository.findDailyLogByDailyLogIdentifier_DailyLogIdAndUserModel_userId(dailyLogId, userId);
        if (existingDailyLog == null) {
            throw new InvalidInputException("Daily log with id " + dailyLogId + " not found for user " + userId);
        }

        dailyLogRepository.delete(existingDailyLog);

        log.info("Deleted daily log with id: {} for user: {}", dailyLogId, userId);
    }
}
