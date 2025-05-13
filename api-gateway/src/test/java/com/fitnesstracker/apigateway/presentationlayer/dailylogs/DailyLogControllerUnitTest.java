package com.fitnesstracker.apigateway.presentationlayer.dailylogs;

import com.fitnesstracker.apigateway.businesslayer.dailylogs.DailyLogService;
import com.fitnesstracker.apigateway.domainclientlayer.dailylogs.GoalStatus;
import com.fitnesstracker.apigateway.utils.exceptions.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DailyLogControllerUnitTest {

    private DailyLogService dailyLogService;
    private DailyLogController dailyLogController;

    @BeforeEach
    void setUp() {
        dailyLogService = mock(DailyLogService.class);
        dailyLogController = new DailyLogController(dailyLogService);
    }

    @Test
    void testGetDailyLogs() {
        String userId = "user123";
        DailyLogResponseModel mockResponse = createMockResponse();
        when(dailyLogService.getDailyLogs(userId)).thenReturn(List.of(mockResponse));

        ResponseEntity<List<DailyLogResponseModel>> response = dailyLogController.getDailyLogs(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(dailyLogService, times(1)).getDailyLogs(userId);
    }

    @Test
    void testGetDailyLogById() {
        String userId = "user123";
        String dailyLogId = "log456";
        DailyLogResponseModel mockResponse = createMockResponse();
        when(dailyLogService.getDailyLogByDailyLogId(dailyLogId, userId)).thenReturn(mockResponse);

        ResponseEntity<DailyLogResponseModel> response = dailyLogController.getDailyLog(userId, dailyLogId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cardio", response.getBody().getWorkoutName());
        verify(dailyLogService).getDailyLogByDailyLogId(dailyLogId, userId);
    }

    @Test
    void testAddDailyLog() {
        String userId = "user123";
        DailyLogRequestModel requestModel = createMockRequest();
        DailyLogResponseModel mockResponse = createMockResponse();
        when(dailyLogService.addDailyLog(requestModel, userId)).thenReturn(mockResponse);

        ResponseEntity<DailyLogResponseModel> response = dailyLogController.addDailyLog(userId, requestModel);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Cardio", response.getBody().getWorkoutName());
        verify(dailyLogService).addDailyLog(requestModel, userId);
    }

    @Test
    void testUpdateDailyLog() {
        String userId = "user123";
        String dailyLogId = "log456";
        DailyLogRequestModel requestModel = createMockRequest();
        DailyLogResponseModel mockResponse = createMockResponse();
        when(dailyLogService.updateDailyLog(requestModel, dailyLogId, userId)).thenReturn(mockResponse);

        ResponseEntity<DailyLogResponseModel> response = dailyLogController.updateDailyLog(userId, dailyLogId, requestModel);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cardio", response.getBody().getWorkoutName());
        verify(dailyLogService).updateDailyLog(requestModel, dailyLogId, userId);
    }

    @Test
    void testDeleteDailyLog() {
        String userId = "user123";
        String dailyLogId = "log456";

        ResponseEntity<Void> response = dailyLogController.deleteDailyLog(userId, dailyLogId);

        assertEquals(204, response.getStatusCodeValue());
        verify(dailyLogService).deleteDailyLog(dailyLogId, userId);
    }

    @Test
    void testAddDailyLog_InvalidInput_Throws422() {
        String userId = "user123";
        DailyLogRequestModel invalidRequest = createMockRequest(); // Assume it's invalid by business logic

        // Simulate that service throws InvalidInputException
        when(dailyLogService.addDailyLog(invalidRequest, userId))
                .thenThrow(new InvalidInputException("Invalid input data"));

        InvalidInputException thrown = assertThrows(InvalidInputException.class, () -> {
            dailyLogController.addDailyLog(userId, invalidRequest);
        });

        assertEquals("Invalid input data", thrown.getMessage());
        verify(dailyLogService, times(1)).addDailyLog(invalidRequest, userId);
    }

    @Test
    void testUpdateDailyLog_InvalidInput_Throws422() {
        String userId = "user123";
        String dailyLogId = "log456";
        DailyLogRequestModel invalidRequest = createMockRequest();

        when(dailyLogService.updateDailyLog(invalidRequest, dailyLogId, userId))
                .thenThrow(new InvalidInputException("Invalid update data"));

        InvalidInputException thrown = assertThrows(InvalidInputException.class, () -> {
            dailyLogController.updateDailyLog(userId, dailyLogId, invalidRequest);
        });

        assertEquals("Invalid update data", thrown.getMessage());
        verify(dailyLogService).updateDailyLog(invalidRequest, dailyLogId, userId);
    }

    @Test
    void testGetDailyLog_InvalidId_Throws422() {
        String userId = "user123";
        String invalidLogId = "bad-id";

        when(dailyLogService.getDailyLogByDailyLogId(invalidLogId, userId))
                .thenThrow(new InvalidInputException("Invalid daily log ID"));

        InvalidInputException thrown = assertThrows(InvalidInputException.class, () -> {
            dailyLogController.getDailyLog(userId, invalidLogId);
        });

        assertEquals("Invalid daily log ID", thrown.getMessage());
        verify(dailyLogService).getDailyLogByDailyLogId(invalidLogId, userId);
    }

    @Test
    void testDeleteDailyLog_InvalidId_Throws422() {
        String userId = "user123";
        String invalidLogId = "bad-id";

        doThrow(new InvalidInputException("Invalid delete request"))
                .when(dailyLogService).deleteDailyLog(invalidLogId, userId);

        InvalidInputException thrown = assertThrows(InvalidInputException.class, () -> {
            dailyLogController.deleteDailyLog(userId, invalidLogId);
        });

        assertEquals("Invalid delete request", thrown.getMessage());
        verify(dailyLogService).deleteDailyLog(invalidLogId, userId);
    }

    // Helper methods to reduce duplication
    private DailyLogRequestModel createMockRequest() {
        return new DailyLogRequestModel(
                "workout123",
                LocalDate.of(2025, 3, 10),
                "breakfast123",
                "lunch123",
                "dinner123",
                List.of("snack123")
        );
    }

    private DailyLogResponseModel createMockResponse() {
        return new DailyLogResponseModel(
                "log456",
                "user123",
                "Alice",
                "Johnson",
                "workout123",
                "Cardio",
                45,
                LocalDate.of(2025, 3, 10),
                "breakfast123",
                "Oats",
                350,
                "lunch123",
                "Salad",
                450,
                "dinner123",
                "Steak",
                700,
                List.of("snack123"),
                List.of("Bar"),
                List.of(200),
                GoalStatus.ACHIEVED,
                GoalStatus.ACHIEVED,
                GoalStatus.ACHIEVED
        );
    }


}
