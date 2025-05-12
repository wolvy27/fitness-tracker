package com.fitnesstracker.dailylogs.presentationlayer;

import com.fitnesstracker.dailylogs.businesslayer.DailyLogService;
import com.fitnesstracker.dailylogs.presentationlayer.DailyLogRequestModel;
import com.fitnesstracker.dailylogs.presentationlayer.DailyLogResponseModel;
import com.fitnesstracker.dailylogs.utils.exceptions.InvalidInputException;
import com.fitnesstracker.dailylogs.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class DailyLogControllerUnitTest {

    @Autowired
    DailyLogController dailyLogController;

    @MockitoBean
    DailyLogService dailyLogService;

    private final String FOUND_DAILYLOG_ID = UUID.randomUUID().toString();
    private final String FOUND_USER_ID = "2dbdcb96-6479-430b-860f-bf99fd854940";
    private final String NOT_FOUND_USER_ID = UUID.randomUUID().toString();
    private final String INVALID_USER_ID = "invalid-user-id";
    private final String INVALID_DAILYLOG_ID = "invalid-dailylog-id";

    // GET All Tests
    @Test
    public void whenNoDailyLogsExist_thenReturnEmptyList() {
        when(dailyLogService.getDailyLogs(FOUND_USER_ID))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<DailyLogResponseModel>> response =
                dailyLogController.getDailyLogs(FOUND_USER_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(dailyLogService, times(1)).getDailyLogs(FOUND_USER_ID);
    }

    @Test
    public void whenDailyLogsExist_thenReturnList() {
        DailyLogResponseModel mockLog = createMockDailyLogResponse();
        when(dailyLogService.getDailyLogs(FOUND_USER_ID))
                .thenReturn(List.of(mockLog));

        ResponseEntity<List<DailyLogResponseModel>> response =
                dailyLogController.getDailyLogs(FOUND_USER_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(mockLog, response.getBody().get(0));
    }

    @Test
    public void whenUserIdInvalid_thenThrowInvalidInputException() {
        InvalidInputException exception = assertThrowsExactly(
                InvalidInputException.class,
                () -> dailyLogController.getDailyLogs(INVALID_USER_ID)
        );

        assertEquals("Invalid userId provided: " + INVALID_USER_ID, exception.getMessage());
        verify(dailyLogService, never()).getDailyLogs(INVALID_USER_ID);
    }

    // GET By ID Tests
    @Test
    public void whenValidDailyLogId_thenReturnDailyLog() {
        DailyLogResponseModel mockResponse = createMockDailyLogResponse();
        when(dailyLogService.getDailyLogByDailyLogId(FOUND_DAILYLOG_ID, FOUND_USER_ID))
                .thenReturn(mockResponse);

        ResponseEntity<DailyLogResponseModel> response =
                dailyLogController.getDailyLog(FOUND_USER_ID, FOUND_DAILYLOG_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(dailyLogService, times(1))
                .getDailyLogByDailyLogId(FOUND_DAILYLOG_ID, FOUND_USER_ID);
    }

    @Test
    public void whenDailyLogNotFound_thenThrowNotFoundException() {
        when(dailyLogService.getDailyLogByDailyLogId(FOUND_DAILYLOG_ID, NOT_FOUND_USER_ID))
                .thenThrow(new NotFoundException("Daily log not found"));

        assertThrows(NotFoundException.class, () ->
                dailyLogController.getDailyLog(NOT_FOUND_USER_ID, FOUND_DAILYLOG_ID));
    }

    @Test
    public void whenDailyLogIdInvalid_thenThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                dailyLogController.getDailyLog(FOUND_USER_ID, INVALID_DAILYLOG_ID));
    }

    // POST Tests
    @Test
    public void whenAddDailyLog_thenReturnCreated() {
        DailyLogRequestModel requestModel = createMockDailyLogRequest();
        DailyLogResponseModel mockResponse = createMockDailyLogResponse();

        when(dailyLogService.addDailyLog(requestModel, FOUND_USER_ID))
                .thenReturn(mockResponse);

        ResponseEntity<DailyLogResponseModel> response =
                dailyLogController.addDailyLog(FOUND_USER_ID, requestModel);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(dailyLogService, times(1))
                .addDailyLog(requestModel, FOUND_USER_ID);
    }
    /*
    @Test
    public void whenAddDailyLogWithInvalidData_thenThrowException() {
        DailyLogRequestModel invalidRequest = new DailyLogRequestModel(); // Empty request

        assertThrows(InvalidInputException.class, () ->
                dailyLogController.addDailyLog(FOUND_USER_ID, invalidRequest));
    }

     */

    // PUT Tests
    @Test
    public void whenUpdateDailyLog_thenReturnUpdatedLog() {
        DailyLogRequestModel requestModel = createMockDailyLogRequest();
        DailyLogResponseModel mockResponse = createMockDailyLogResponse();

        when(dailyLogService.updateDailyLog(requestModel, FOUND_DAILYLOG_ID, FOUND_USER_ID))
                .thenReturn(mockResponse);

        ResponseEntity<DailyLogResponseModel> response =
                dailyLogController.updateDailyLog(FOUND_USER_ID, FOUND_DAILYLOG_ID, requestModel);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    public void whenUpdateNonExistentLog_thenThrowNotFoundException() {
        DailyLogRequestModel requestModel = createMockDailyLogRequest();

        when(dailyLogService.updateDailyLog(requestModel, FOUND_DAILYLOG_ID, NOT_FOUND_USER_ID))
                .thenThrow(new NotFoundException("Daily log not found"));

        assertThrows(NotFoundException.class, () ->
                dailyLogController.updateDailyLog(NOT_FOUND_USER_ID, FOUND_DAILYLOG_ID, requestModel));
    }

    // DELETE Tests
    @Test
    public void whenDeleteDailyLog_thenReturnNoContent() {
        doNothing().when(dailyLogService)
                .deleteDailyLog(FOUND_DAILYLOG_ID, FOUND_USER_ID);

        ResponseEntity<Void> response =
                dailyLogController.deleteDailyLog(FOUND_USER_ID, FOUND_DAILYLOG_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(dailyLogService, times(1))
                .deleteDailyLog(FOUND_DAILYLOG_ID, FOUND_USER_ID);
    }

    @Test
    public void whenDeleteNonExistentLog_thenThrowNotFoundException() {
        doThrow(new NotFoundException("Daily log not found"))
                .when(dailyLogService)
                .deleteDailyLog(FOUND_DAILYLOG_ID, NOT_FOUND_USER_ID);

        assertThrows(NotFoundException.class, () ->
                dailyLogController.deleteDailyLog(NOT_FOUND_USER_ID, FOUND_DAILYLOG_ID));
    }

    // Helper methods
    private DailyLogRequestModel createMockDailyLogRequest() {
        return DailyLogRequestModel.builder()
                .workoutIdentifier(UUID.randomUUID().toString())
                .logDate(LocalDate.now())
                .breakfastIdentifier(UUID.randomUUID().toString())
                .build();
    }

    private DailyLogResponseModel createMockDailyLogResponse() {
        return DailyLogResponseModel.builder()
                .dailyLogIdentifier(FOUND_DAILYLOG_ID)
                .userIdentifier(FOUND_USER_ID)
                .userFirstName("Test")
                .userLastName("User")
                .workoutName("Test Workout")
                .workoutDurationInMinutes(30)
                .logDate(LocalDate.now())
                .breakfastCalories(500)
                .lunchCalories(600)
                .dinnerCalories(700)
                .build();
    }
}