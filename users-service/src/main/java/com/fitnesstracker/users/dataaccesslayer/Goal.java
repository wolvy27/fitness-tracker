package com.fitnesstracker.users.dataaccesslayer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Goal {

    @Column(name = "goal_description")
    private String goalDescription;

    @Column(name = "daily_caloric_intake")
    private Integer dailyCaloricIntake;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_workout_days", joinColumns = @JoinColumn(name="user_id"))
    @Column(name = "workout_day")
    private List<String> workoutDays;

    public Goal(@NotNull String goalDescription, @NotNull Integer dailyCaloricIntake, @NotNull List<String> workoutDays) {
        this.goalDescription = goalDescription;
        this.dailyCaloricIntake = dailyCaloricIntake;
        this.workoutDays = workoutDays;
    }
}
