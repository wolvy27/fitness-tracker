package com.fitnesstracker.users.datamapperlayer;


import com.fitnesstracker.users.dataaccesslayer.User;
import com.fitnesstracker.users.presentationlayer.UserController;
import com.fitnesstracker.users.presentationlayer.UserResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    @AfterMapping()
    default void addLinks(@MappingTarget UserResponseModel userResponseModel, User user) {
        // Self link (GET user by ID)
        userResponseModel.add(
                linkTo(methodOn(UserController.class)
                        .getUser(userResponseModel.getUserId()))
                        .withSelfRel()
        );

        // Link to get all users
        userResponseModel.add(
                linkTo(methodOn(UserController.class)
                        .getUsers())
                        .withRel("allUsers")
        );
    }
}
