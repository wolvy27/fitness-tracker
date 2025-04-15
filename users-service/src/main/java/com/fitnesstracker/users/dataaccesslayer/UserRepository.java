package com.fitnesstracker.users.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUserIdentifier_UserId(String userId);
}
