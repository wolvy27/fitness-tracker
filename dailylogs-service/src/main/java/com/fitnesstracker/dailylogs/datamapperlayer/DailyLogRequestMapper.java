package com.fitnesstracker.dailylogs.datamapperlayer;

import com.fitnesstracker.dailylogs.dataaccesslayer.DailyLog;
import com.fitnesstracker.dailylogs.domainclientlayer.meals.MealModel;
import com.fitnesstracker.dailylogs.presentationlayer.DailyLogRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DailyLogRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userModel", ignore = true)
    @Mapping(target = "dailyLogIdentifier", ignore = true)
    @Mapping(target = "breakfast", ignore = true)
    @Mapping(target = "lunch", ignore = true)
    @Mapping(target = "dinner", ignore = true)
    @Mapping(target = "snacks", ignore = true)
    @Mapping(source = "workoutIdentifier", target = "workoutModel.workoutId")
    @Mapping(source = "logDate", target = "logDate")
    @Mapping(target = "metCaloriesGoal", ignore = true)
    @Mapping(target = "metWorkoutGoal", ignore = true)
    @Mapping(target = "metDailyGoals", ignore = true)
    DailyLog requestModelToEntity(DailyLogRequestModel dailyLogRequestModel);


}
