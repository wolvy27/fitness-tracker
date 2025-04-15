package com.fitnesstracker.workouts.datamapperlayer;


import com.fitnesstracker.workouts.dataaccesslayer.Workout;
import com.fitnesstracker.workouts.presentationlayer.WorkoutController;
import com.fitnesstracker.workouts.presentationlayer.WorkoutResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface WorkoutResponseMapper {
    @Mapping(source = "workoutIdentifier.workoutId", target = "workoutId")
    @Mapping(source = "userIdentifier.userId", target = "userId")
    @Mapping(source = "workoutName", target = "workoutName")
    @Mapping(source = "workoutType", target = "workoutType")
    @Mapping(source = "durationInMinutes", target = "durationInMinutes")
    @Mapping(source = "workoutDate", target = "workoutDate")
    WorkoutResponseModel entityToResponseModel(Workout workout);

    List<WorkoutResponseModel> entityToResponseModelList(List<Workout> workouts);

    @AfterMapping()
    default void addLinks(@MappingTarget WorkoutResponseModel workoutResponseModel, Workout workout) {
        Link selfLink = linkTo(methodOn(WorkoutController.class)
                .getWorkout(workoutResponseModel.getWorkoutId(), workoutResponseModel.getUserId()))
                .withSelfRel();
        workoutResponseModel.add(selfLink);

        Link allWorkoutsLink = linkTo(methodOn(WorkoutController.class)
                .getWorkouts(workoutResponseModel.getUserId()))
                .withRel("allWorkoutsForUser");
        workoutResponseModel.add(allWorkoutsLink);
    }
}
