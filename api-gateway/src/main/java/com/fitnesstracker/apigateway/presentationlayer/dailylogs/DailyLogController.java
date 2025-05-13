package com.fitnesstracker.apigateway.presentationlayer.dailylogs;

import com.fitnesstracker.apigateway.businesslayer.dailylogs.DailyLogService;
import com.fitnesstracker.apigateway.utils.exceptions.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/users/{userId}/dailyLogs")
public class DailyLogController {
    private final DailyLogService dailyLogService;
    private static final int UUID_LENGTH = 36;

    public DailyLogController(DailyLogService dailyLogService) {
        this.dailyLogService = dailyLogService;
    }

    @GetMapping()
    public ResponseEntity<List<DailyLogResponseModel>> getDailyLogs(@PathVariable String userId) {
        return ResponseEntity.ok(dailyLogService.getDailyLogs(userId));
    }

    @GetMapping("/{dailyLogId}")
    public ResponseEntity<DailyLogResponseModel> getDailyLog(@PathVariable String userId, @PathVariable String dailyLogId) {
        return ResponseEntity.ok(dailyLogService.getDailyLogByDailyLogId(dailyLogId, userId));
    }

    @PostMapping()
    public ResponseEntity<DailyLogResponseModel> addDailyLog(@PathVariable String userId, @RequestBody DailyLogRequestModel requestModel) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dailyLogService.addDailyLog(requestModel, userId));
    }

    @PutMapping("/{dailyLogId}")
    public ResponseEntity<DailyLogResponseModel> updateDailyLog(@PathVariable String userId, @PathVariable String dailyLogId,
                                                                @RequestBody DailyLogRequestModel requestModel) {
        return ResponseEntity.ok(dailyLogService.updateDailyLog(requestModel, dailyLogId, userId));
    }

    @DeleteMapping("/{dailyLogId}")
    public ResponseEntity<Void> deleteDailyLog(@PathVariable String userId, @PathVariable String dailyLogId) {
        dailyLogService.deleteDailyLog(dailyLogId, userId);
        return ResponseEntity.noContent().build();
    }
}