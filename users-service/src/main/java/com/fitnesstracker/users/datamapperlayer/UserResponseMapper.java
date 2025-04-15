package com.fitnesstracker.users.datamapperlayer;


import com.fitnesstracker.users.dataaccesslayer.User;
import com.fitnesstracker.users.presentationlayer.UserResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    @Mapping(source = "userIdentifier.userId", target = "userId")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "heightInCm", target = "heightInCm")
    @Mapping(source = "weightInKg", target = "weightInKg")
    @Mapping(source = "goal.goalDescription", target = "goalDescription")
    @Mapping(source = "goal.dailyCaloricIntake", target = "dailyCaloricIntake")
    @Mapping(source = "goal.workoutDays", target = "workoutDays")
    UserResponseModel entityToResponseModel(User user);

    List<UserResponseModel> entityListToResponseModelList(List<User> users);
}
