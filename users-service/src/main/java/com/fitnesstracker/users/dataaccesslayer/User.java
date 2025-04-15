package com.fitnesstracker.users.dataaccesslayer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private UserIdentifier userIdentifier;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "height_in_cm")
    private Integer heightInCm;

    @Column(name = "weight_in_kg")
    private Integer weightInKg;


    @Embedded
    private Goal goal;

    public User(@NotNull String firstName, @NotNull String lastName, @NotNull Integer age, @NotNull Integer heightInCm, @NotNull Integer weightInKg, @NotNull Goal goal) {
        this.userIdentifier = new UserIdentifier();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.heightInCm = heightInCm;
        this.weightInKg = weightInKg;
        this.goal = goal;
    }
}
