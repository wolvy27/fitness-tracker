package com.fitnesstracker.dailylogs.datamapperlayer;

import com.fitnesstracker.dailylogs.dataaccesslayer.DailyLog;
import com.fitnesstracker.dailylogs.domainclientlayer.meals.MealModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.fitnesstracker.dailylogs.presentationlayer.DailyLogResponseModel;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DailyLogResponseMapper {

    @Mapping(source = "dailyLogIdentifier.dailyLogId", target = "dailyLogIdentifier")
    @Mapping(source = "userModel.userId", target = "userIdentifier")
    @Mapping(source = "workoutModel.workoutId", target = "workoutIdentifier")
    @Mapping(source = "breakfast.mealId", target = "breakfastIdentifier")
    @Mapping(source = "breakfast.mealName", target = "breakfastName")
    @Mapping(source = "breakfast.mealCalorie", target = "breakfastCalories")
    @Mapping(source = "lunch.mealId", target = "lunchIdentifier")
    @Mapping(source = "lunch.mealName", target = "lunchName")
    @Mapping(source = "lunch.mealCalorie", target = "lunchCalories")
    @Mapping(source = "dinner.mealId", target = "dinnerIdentifier")
    @Mapping(source = "dinner.mealName", target = "dinnerName")
    @Mapping(source = "dinner.mealCalorie", target = "dinnerCalories")
    @Mapping(source = "snacks", target = "snacksIdentifier", qualifiedByName = "mapSnackIds")
    @Mapping(source = "snacks", target = "snacksName", qualifiedByName = "mapSnackNames")
    @Mapping(source = "snacks", target = "snacksCalories", qualifiedByName = "mapSnackCalories")
    @Mapping(source = "logDate", target = "logDate")
    @Mapping(source = "metCaloriesGoal", target = "metCaloriesGoal")
    @Mapping(source = "metWorkoutGoal", target = "metWorkoutGoal")
    @Mapping(source = "metDailyGoals", target = "metDailyGoals")
    DailyLogResponseModel entityToResponseModel(DailyLog dailyLog);

    @Named("mapSnackIds")
    default List<String> mapSnackIds(List<MealModel> snacks) {
        if (snacks == null) {
            return Collections.emptyList();
        }
        List<String> ids = new ArrayList<>();
        for (MealModel snack : snacks) {
            if (snack != null) {
                ids.add(snack.getMealId());
            }
        }
        return ids;
    }

    @Named("mapSnackNames")
    default List<String> mapSnackNames(List<MealModel> snacks) {
        if (snacks == null) {
            return Collections.emptyList();
        }
        List<String> names = new ArrayList<>();
        for (MealModel snack : snacks) {
            if (snack != null) {
                names.add(snack.getMealName());
            }
        }
        return names;
    }

    @Named("mapSnackCalories")
    default List<Integer> mapSnackCalories(List<MealModel> snacks) {
        if (snacks == null) {
            return Collections.emptyList();
        }
        List<Integer> calories = new ArrayList<>();
        for (MealModel snack : snacks) {
            if (snack != null) {
                calories.add(snack.getMealCalorie());
            }
        }
        return calories;
    }
}