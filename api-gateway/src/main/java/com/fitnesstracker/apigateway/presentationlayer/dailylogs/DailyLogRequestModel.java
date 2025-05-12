package com.fitnesstracker.apigateway.presentationlayer.dailylogs;

import com.fitnesstracker.apigateway.domainclientlayer.dailylogs.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogRequestModel {
    private String workoutIdentifier;
    private LocalDate logDate;
    private String breakfastIdentifier;
    private String lunchIdentifier;
    private String dinnerIdentifier;
    private List<String> snacksIdentifier;
}