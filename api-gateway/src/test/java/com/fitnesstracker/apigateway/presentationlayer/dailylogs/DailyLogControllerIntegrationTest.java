package com.fitnesstracker.apigateway.presentationlayer.dailylogs;

import com.fitnesstracker.apigateway.businesslayer.dailylogs.DailyLogService;
import com.fitnesstracker.apigateway.domainclientlayer.dailylogs.GoalStatus;
import com.fitnesstracker.apigateway.utils.exceptions.InvalidInputException;
import com.fitnesstracker.apigateway.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DailyLogControllerIntegrationTest {

    @Mock
    private DailyLogService dailyLogService;

    @InjectMocks
    private DailyLogController dailyLogController;

    private MockMvc mockMvc;

    private final String testUserId = "user-123";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dailyLogController).build();
    }

    @Test
    void whenPostDailyLog_thenReturnsCreatedLog() throws Exception {
        DailyLogRequestModel requestModel = new DailyLogRequestModel(
                "workout-123",
                LocalDate.now(),
                "breakfast-abc",
                "lunch-def",
                "dinner-ghi",
                List.of("snack-1", "snack-2")
        );

        DailyLogResponseModel responseModel = new DailyLogResponseModel(
                "log-123",
                "user-123",
                "John",
                "Doe",
                "workout-123",
                "Morning Workout",
                30,
                LocalDate.now(),
                "breakfast-abc",
                "Oatmeal",
                200,
                "lunch-def",
                "Salad",
                300,
                "dinner-ghi",
                "Steak",
                500,
                List.of("snack-1", "snack-2"),
                List.of("Apple", "Banana"),
                List.of(100, 150),
                GoalStatus.ACHIEVED,
                GoalStatus.ACHIEVED,
                GoalStatus.ACHIEVED
        );

        // Mocking the service
        when(dailyLogService.addDailyLog(any(), eq(testUserId))).thenReturn(responseModel);

        mockMvc.perform(post("/api/v1/users/{userId}/dailyLogs", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"workoutIdentifier\": \"workout-123\",\n" +
                                "  \"logDate\": \"2025-05-13\",\n" +
                                "  \"breakfastIdentifier\": \"breakfast-abc\",\n" +
                                "  \"lunchIdentifier\": \"lunch-def\",\n" +
                                "  \"dinnerIdentifier\": \"dinner-ghi\",\n" +
                                "  \"snacksIdentifier\": [\"snack-1\", \"snack-2\"]\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dailyLogIdentifier").value("log-123"))
                .andExpect(jsonPath("$.userIdentifier").value("user-123"))
                .andExpect(jsonPath("$.workoutIdentifier").value("workout-123"))
                .andExpect(jsonPath("$.workoutName").value("Morning Workout"));
    }

    @Test
    void whenGetAllDailyLogs_thenReturnsList() throws Exception {
        DailyLogResponseModel log1 = new DailyLogResponseModel("log-123", "user-123", "John", "Doe",
                "workout-123", "Morning Workout", 30, LocalDate.now(),
                "breakfast-abc", "Oatmeal", 200, "lunch-def", "Salad", 300, "dinner-ghi", "Steak", 500,
                List.of("snack-1", "snack-2"), List.of("Apple", "Banana"), List.of(100, 150), GoalStatus.ACHIEVED,
                GoalStatus.ACHIEVED, GoalStatus.ACHIEVED);

        when(dailyLogService.getDailyLogs(testUserId)).thenReturn(List.of(log1));

        mockMvc.perform(get("/api/v1/users/{userId}/dailyLogs", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dailyLogIdentifier").value("log-123"))
                .andExpect(jsonPath("$[0].userIdentifier").value("user-123"))
                .andExpect(jsonPath("$[0].workoutIdentifier").value("workout-123"));
    }

    @Test
    void whenGetDailyLogById_thenReturnsDailyLog() throws Exception {
        DailyLogResponseModel log = new DailyLogResponseModel("log-123", "user-123", "John", "Doe",
                "workout-123", "Morning Workout", 30, LocalDate.now(),
                "breakfast-abc", "Oatmeal", 200, "lunch-def", "Salad", 300, "dinner-ghi", "Steak", 500,
                List.of("snack-1", "snack-2"), List.of("Apple", "Banana"), List.of(100, 150), GoalStatus.ACHIEVED,
                GoalStatus.ACHIEVED, GoalStatus.ACHIEVED);

        when(dailyLogService.getDailyLogByDailyLogId("log-123", testUserId)).thenReturn(log);

        mockMvc.perform(get("/api/v1/users/{userId}/dailyLogs/{dailyLogId}", testUserId, "log-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dailyLogIdentifier").value("log-123"))
                .andExpect(jsonPath("$.userIdentifier").value("user-123"))
                .andExpect(jsonPath("$.workoutIdentifier").value("workout-123"));
    }

    @Test
    void whenDeleteDailyLog_thenReturnsNoContent() throws Exception {
        doNothing().when(dailyLogService).deleteDailyLog("log-123", testUserId);

        mockMvc.perform(delete("/api/v1/users/{userId}/dailyLogs/{dailyLogId}", testUserId, "log-123"))
                .andExpect(status().isNoContent());
    }

}
