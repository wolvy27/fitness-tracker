package com.fitnesstracker.workouts.datamapperlayer;



import com.fitnesstracker.workouts.dataaccesslayer.Workout;
import com.fitnesstracker.workouts.presentationlayer.WorkoutRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkoutRequestMapper {
    @Mapping(source = "userId", target = "userIdentifier.userId")
    @Mapping(source = "workoutName", target = "workoutName")
    @Mapping(source = "workoutType", target = "workoutType")
    @Mapping(source = "durationInMinutes", target = "durationInMinutes")
    @Mapping(source = "workoutDate", target = "workoutDate")
    Workout requestModelToEntity(WorkoutRequestModel workoutRequestModel);
}
