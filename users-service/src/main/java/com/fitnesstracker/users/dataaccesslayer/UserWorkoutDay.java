package com.fitnesstracker.users.dataaccesslayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Embeddable
@NoArgsConstructor
@Getter
public class UserWorkoutDay {

    @Column(name = "workout_day")
    private String workoutDay;

    public UserWorkoutDay(@NotNull String workoutDay) {
        this.workoutDay = workoutDay;
    }
}
