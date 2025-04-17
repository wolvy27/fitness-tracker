package com.fitnesstracker.apigateway.businesslayer.users;


import com.fitnesstracker.apigateway.domainclientlayer.users.UserServiceClient;
import com.fitnesstracker.apigateway.presentationlayer.users.UserRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.users.UserResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserServiceClient userServiceClient;

    public UserServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public List<UserResponseModel> getUsers() {
        return userServiceClient.getUsers();
    }

    @Override
    public UserResponseModel getUserByUserId(String userId) {
        return userServiceClient.getUserByUserId(userId);
    }

    @Override
    public UserResponseModel addUser(UserRequestModel userRequestModel) {
        return userServiceClient.addUser(userRequestModel);
    }

    @Override
    public UserResponseModel updateUser(UserRequestModel userRequestModel, String userId) {
        return userServiceClient.updateUser(userRequestModel, userId);
    }

    @Override
    public void deleteUser(String userId) {
        userServiceClient.deleteUser(userId);
    }
}
