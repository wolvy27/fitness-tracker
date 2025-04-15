package com.fitnesstracker.users.businesslayer;



import com.fitnesstracker.users.presentationlayer.UserRequestModel;
import com.fitnesstracker.users.presentationlayer.UserResponseModel;

import java.util.List;

public interface UserService {
    List<UserResponseModel> getUsers();
    UserResponseModel getUserByUserId(String userId);
    UserResponseModel addUser(UserRequestModel newUserData);
    UserResponseModel updateUser(UserRequestModel newUserData, String userId);
    void deleteUser(String userId);

    // For DailyLog aggregate
    String getUserFirstNameById(String userId);
    String getUserLastNameById(String userId);
    Integer getDailyCalorieIntakeById(String userId);
    List<String> getWorkoutDaysById(String userId);
}
