package com.fitnesstracker.users.dataaccesslayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired UserRepository userRepository;

    @BeforeEach
    public void setUpDb() {
        userRepository.deleteAll();
    }

    @Test
    public void whenUserExist_thenReturnAllUsers() {
        // Arrange
        Goal goal1 = new Goal("Lose weight", 1800, List.of("Monday", "Wednesday", "Friday"));
        User user1 = new User("Alice", "Smith", 28, 165, 68, goal1);

        Goal goal2 = new Goal("Gain muscle", 2500, List.of("Tuesday", "Thursday", "Saturday"));
        User user2 = new User("Bob", "Johnson", 32, 180, 75, goal2);

        userRepository.save(user1);
        userRepository.save(user2);
        long afterSizeDb = userRepository.count();

        // Act
        List<User> users = userRepository.findAll();

        // Assert
        assertNotNull(users);
        assertNotEquals(0, afterSizeDb);
        assertEquals(afterSizeDb, users.size());

    }

    @Test
    public void whenUserExists_thenReturnUserByUserId() {
        // Arrange
        Goal goal1 = new Goal("Lose weight", 1800, List.of("Monday", "Wednesday", "Friday"));
        User user1 = new User("Alice", "Smith", 28, 165, 68, goal1);

        Goal goal2 = new Goal("Gain muscle", 2500, List.of("Tuesday", "Thursday", "Saturday"));
        User user2 = new User("Bob", "Johnson", 32, 180, 75, goal2);

        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        User foundUser = userRepository.findUserByUserIdentifier_UserId(user1.getUserIdentifier().getUserId());

        // Assert
        assertNotNull(foundUser);
        assertEquals(user1.getUserIdentifier().getUserId(), foundUser.getUserIdentifier().getUserId());
        assertEquals(user1.getFirstName(), foundUser.getFirstName());
        assertEquals(user1.getLastName(), foundUser.getLastName());
        assertEquals(user1.getAge(), foundUser.getAge());
        assertEquals(user1.getHeightInCm(), foundUser.getHeightInCm());
        assertEquals(user1.getWeightInKg(), foundUser.getWeightInKg());

        assertNotNull(foundUser.getGoal());
        assertEquals(user1.getGoal().getGoalDescription(), foundUser.getGoal().getGoalDescription());
        assertEquals(user1.getGoal().getDailyCaloricIntake(), foundUser.getGoal().getDailyCaloricIntake());
        assertIterableEquals(user1.getGoal().getWorkoutDays(), foundUser.getGoal().getWorkoutDays());
    }

    @Test
    public void whenUserDoesNotExist_thenReturnNull(){
        // Arrange
        final String NOT_FOUND_USER_ID = "4d75e21a-8f9b-4063-bbb0-c2437a2c14b5";

        // Act
        User foundUser = userRepository.findUserByUserIdentifier_UserId(NOT_FOUND_USER_ID);

        // Assert
        assertNull(foundUser);
    }

    @Test
    public void whenUserIsValid_thenAddUser() {
        // Arrange
        Goal goal1 = new Goal("Lose weight", 1800, List.of("Monday", "Wednesday", "Friday"));
        User user1 = new User("Alice", "Smith", 28, 165, 68, goal1);

        // Act
        User savedUser = userRepository.save(user1);

        // Arrange
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getUserIdentifier());
        assertNotNull(savedUser.getUserIdentifier().getUserId());
        assertEquals(user1.getUserIdentifier().getUserId(), savedUser.getUserIdentifier().getUserId());
        assertEquals(user1.getFirstName(), savedUser.getFirstName());
        assertEquals(user1.getLastName(), savedUser.getLastName());
        assertEquals(user1.getAge(), savedUser.getAge());
        assertEquals(user1.getHeightInCm(), savedUser.getHeightInCm());
        assertEquals(user1.getWeightInKg(), savedUser.getWeightInKg());

        assertNotNull(savedUser.getGoal());
        assertEquals(user1.getGoal().getGoalDescription(), savedUser.getGoal().getGoalDescription());
        assertEquals(user1.getGoal().getDailyCaloricIntake(), savedUser.getGoal().getDailyCaloricIntake());
        assertIterableEquals(user1.getGoal().getWorkoutDays(), savedUser.getGoal().getWorkoutDays());


    }


}