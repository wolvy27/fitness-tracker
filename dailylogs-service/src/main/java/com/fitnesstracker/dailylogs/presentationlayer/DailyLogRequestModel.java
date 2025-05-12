package com.fitnesstracker.dailylogs.presentationlayer;


import com.fitnesstracker.dailylogs.dataaccesslayer.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class DailyLogRequestModel {
    private String workoutIdentifier;
    private LocalDate logDate;
    private String breakfastIdentifier;
    private String lunchIdentifier;
    private String dinnerIdentifier;
    private List<String> snacksIdentifier;
}
