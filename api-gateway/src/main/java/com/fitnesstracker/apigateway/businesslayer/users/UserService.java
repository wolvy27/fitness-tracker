package com.fitnesstracker.apigateway.businesslayer.users;

import com.fitnesstracker.apigateway.domainclientlayer.users.User;
import com.fitnesstracker.apigateway.presentationlayer.users.UserResponseModel;

import java.util.List;

public interface UserService {
    List<UserResponseModel> getUsers();
    UserResponseModel getUserByUserId(String userId);
}
