package datamapperlayer;

import dataaccesslayer.DailyLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import presentationlayer.DailyLogResponseModel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DailyLogResponseMapper {

    @Mapping(source = "dailyLogId", target = "dailyLogIdentifier")
    @Mapping(source = "userId", target = "userIdentifier")

    @Mapping(source = "workoutId", target = "workoutIdentifier")

    // mealIds -> mealIdentifiers
    // Plural?!?!?!? The bite of 87'?!?!?!?!?!?!?!?!?!?!?!??!?!?!?!?!
    @Mapping(source = "mealIds", target = "mealIdentifiers")

    @Mapping(source = "logDate", target = "logDate")
    @Mapping(source = "breakfast", target = "breakfast")
    @Mapping(source = "lunch", target = "lunch")
    @Mapping(source = "dinner", target = "dinner")
    @Mapping(source = "snacks", target = "snacks")
    @Mapping(source = "metCaloriesGoal", target = "metCaloriesGoal")
    @Mapping(source = "metWorkoutGoal", target = "metWorkoutGoal")
    @Mapping(source = "metDailyGoals", target = "metDailyGoals")
    DailyLogResponseModel entityToResponseModel(DailyLog dailyLog);

    List<DailyLogResponseModel> entityToResponseModelList(List<DailyLog> dailyLogs);
}
