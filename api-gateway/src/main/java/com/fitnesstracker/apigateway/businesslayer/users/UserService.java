package com.fitnesstracker.apigateway.businesslayer.users;

import com.fitnesstracker.apigateway.presentationlayer.users.UserRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.users.UserResponseModel;

import java.util.List;

public interface UserService {
    List<UserResponseModel> getUsers();
    UserResponseModel getUserByUserId(String userId);
    UserResponseModel addUser(UserRequestModel userRequestModel);
    UserResponseModel updateUser(UserRequestModel userRequestModel, String userId);
    void deleteUser(String userId);
}
