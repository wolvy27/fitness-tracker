package datamapperlayer;

import dataaccesslayer.DailyLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import presentationlayer.DailyLogRequestModel;

@Mapper(componentModel = "spring")
public interface DailyLogRequestMapper {

    @Mapping(source = "workoutIdentifier", target = "workoutId")
    @Mapping(source = "mealIdentifiers", target = "mealIds")
    @Mapping(source = "logDate", target = "logDate")
    @Mapping(source = "breakfast", target = "breakfast")
    @Mapping(source = "lunch", target = "lunch")
    @Mapping(source = "dinner", target = "dinner")
    @Mapping(source = "snacks", target = "snacks")
    @Mapping(source = "metCaloriesGoal", target = "metCaloriesGoal")
    @Mapping(source = "metWorkoutGoal", target = "metWorkoutGoal")
    @Mapping(source = "metDailyGoals", target = "metDailyGoals")
    DailyLog requestModelToEntity(DailyLogRequestModel dailyLogRequestModel);

}
