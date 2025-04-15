package com.fitnesstracker.users.datamapperlayer;



import com.fitnesstracker.users.dataaccesslayer.User;
import com.fitnesstracker.users.presentationlayer.UserRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "heightInCm", target = "heightInCm")
    @Mapping(source = "weightInKg", target = "weightInKg")
    @Mapping(source = "goalDescription", target = "goal.goalDescription")
    @Mapping(source = "dailyCaloricIntake", target = "goal.dailyCaloricIntake")
    @Mapping(source = "workoutDays", target = "goal.workoutDays")
    User requestModelToEntity(UserRequestModel userRequestModel);

}
