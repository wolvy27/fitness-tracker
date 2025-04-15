package com.fitnesstracker.users.dataaccesslayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter
@Setter
public class UserIdentifier {

    @Column(name = "user_id")
    private String userId;

    public UserIdentifier() {
        this.userId = UUID.randomUUID().toString();
    }

    public UserIdentifier(String userId) {
        this.userId = userId;
    }
}
