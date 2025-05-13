package com.fitnesstracker.dailylogs.presentationlayer;


import com.fitnesstracker.dailylogs.businesslayer.DailyLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fitnesstracker.dailylogs.utils.exceptions.InvalidInputException;

import java.util.List;

@RestController
@RequestMapping("api/v1/{userId}/dailyLogs")
public class DailyLogController {
    private final DailyLogService dailyLogService;
    private static final int UUID_LENGTH = 36;

    public DailyLogController(DailyLogService dailyLogService) {
        this.dailyLogService = dailyLogService;
    }

    @GetMapping()
    public ResponseEntity<List<DailyLogResponseModel>> getDailyLogs(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }
        return ResponseEntity.ok().body(dailyLogService.getDailyLogs(userId));
    }

    @GetMapping("/{dailyLogId}")
    public ResponseEntity<DailyLogResponseModel> getDailyLog(@PathVariable String userId, @PathVariable String dailyLogId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }

        if (userId.length() == UUID_LENGTH) {}

        if (dailyLogId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid dailyLogId provided: " + dailyLogId);
        }
        return ResponseEntity.ok().body(dailyLogService.getDailyLogByDailyLogId(dailyLogId, userId));
    }

    @PostMapping()
    public ResponseEntity<DailyLogResponseModel> addDailyLog(@PathVariable String userId, @RequestBody DailyLogRequestModel requestModel) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }
        if (requestModel == null) {
            throw new InvalidInputException("Request body cannot be null");
        }
        if (requestModel.getLogDate() == null) {
            throw new InvalidInputException("Log date is required");
        }

        return ResponseEntity.created(null).body(dailyLogService.addDailyLog(requestModel, userId));
    }

    @PutMapping("/{dailyLogId}")
    public ResponseEntity<DailyLogResponseModel> updateDailyLog(@PathVariable String userId, @PathVariable String dailyLogId, @RequestBody DailyLogRequestModel requestModel) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }

        if (dailyLogId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid dailyLogId provided: " + dailyLogId);
        }

        return ResponseEntity.ok().body(dailyLogService.updateDailyLog(requestModel, dailyLogId, userId));
    }

    @DeleteMapping("/{dailyLogId}")
    public ResponseEntity<Void> deleteDailyLog(@PathVariable String userId, @PathVariable String dailyLogId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }
        if (dailyLogId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid dailyLogId provided: " + dailyLogId);
        }
        dailyLogService.deleteDailyLog(dailyLogId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
