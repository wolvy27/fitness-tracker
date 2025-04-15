package com.fitnesstracker.users.businesslayer;


import com.fitnesstracker.users.dataaccesslayer.Goal;
import com.fitnesstracker.users.dataaccesslayer.User;
import com.fitnesstracker.users.dataaccesslayer.UserIdentifier;
import com.fitnesstracker.users.dataaccesslayer.UserRepository;
import com.fitnesstracker.users.datamapperlayer.UserRequestMapper;
import com.fitnesstracker.users.datamapperlayer.UserResponseMapper;
import com.fitnesstracker.users.presentationlayer.UserRequestModel;
import com.fitnesstracker.users.presentationlayer.UserResponseModel;
import com.fitnesstracker.users.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserResponseMapper userResponseMapper;
    private UserRequestMapper userRequestMapper;

    public UserServiceImpl(UserRepository userRepository, UserResponseMapper userResponseMapper, UserRequestMapper userRequestMapper) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.userRequestMapper = userRequestMapper;
    }

    @Override
    public List<UserResponseModel> getUsers() {
        List<User> users = userRepository.findAll();
        return userResponseMapper.entityListToResponseModelList(users);
    }

    @Override
    public UserResponseModel getUserByUserId(String userId) {
        User user = userRepository.findUserByUserIdentifier_UserId(userId);
        if (user == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }
        return userResponseMapper.entityToResponseModel(user);
    }

    @Override
    public UserResponseModel addUser(UserRequestModel newUserData) {
        Goal goal = new Goal(newUserData.getGoalDescription(), newUserData.getDailyCaloricIntake(), newUserData.getWorkoutDays());
        User user = userRequestMapper.requestModelToEntity(newUserData);
        user.setUserIdentifier(new UserIdentifier());
        user.setGoal(goal);
        return userResponseMapper.entityToResponseModel(userRepository.save(user));
    }

    @Override
    public UserResponseModel updateUser(UserRequestModel newUserData, String userId) {
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }
        Goal goal = new Goal(newUserData.getGoalDescription(), newUserData.getDailyCaloricIntake(), newUserData.getWorkoutDays());
        User updatedUser = userRequestMapper.requestModelToEntity(newUserData);
        updatedUser.setUserIdentifier(foundUser.getUserIdentifier());
        updatedUser.setGoal(goal);
        updatedUser.setId(foundUser.getId());

        User savedUser = userRepository.save(updatedUser);
        return userResponseMapper.entityToResponseModel(savedUser);
    }

    @Override
    public void deleteUser(String userId) {
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }
        userRepository.delete(foundUser);
    }

    @Override
    public String getUserFirstNameById(String userId) {
        User user = userRepository.findUserByUserIdentifier_UserId(userId);
        if (user == null){
            throw new NotFoundException("UserId not found: " + userId);
        }
        return user.getFirstName();
    }

    @Override
    public String getUserLastNameById(String userId) {
        User user = userRepository.findUserByUserIdentifier_UserId(userId);
        if (user == null){
            throw new NotFoundException("UserId not found: " + userId);
        }
        return user.getLastName();
    }

    @Override
    public Integer getDailyCalorieIntakeById(String userId) {
        User user = userRepository.findUserByUserIdentifier_UserId(userId);
        if (user == null){
            throw new NotFoundException("UserId not found: " + userId);
        }
        return user.getGoal().getDailyCaloricIntake();
    }

    @Override
    public List<String> getWorkoutDaysById(String userId) {
        User user = userRepository.findUserByUserIdentifier_UserId(userId);
        if (user == null){
            throw new NotFoundException("UserId not found: " + userId);
        }
        return user.getGoal().getWorkoutDays();
    }
}
