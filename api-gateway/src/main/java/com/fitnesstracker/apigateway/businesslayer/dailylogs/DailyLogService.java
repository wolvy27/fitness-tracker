package com.fitnesstracker.apigateway.businesslayer.dailylogs;

import com.fitnesstracker.apigateway.presentationlayer.dailylogs.DailyLogRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.dailylogs.DailyLogResponseModel;
import java.util.List;

public interface DailyLogService {
    List<DailyLogResponseModel> getDailyLogs(String userId);
    DailyLogResponseModel getDailyLogByDailyLogId(String dailyLogId, String userId);
    DailyLogResponseModel addDailyLog(DailyLogRequestModel dailyLogRequestModel, String userId);
    DailyLogResponseModel updateDailyLog(DailyLogRequestModel dailyLogRequestModel, String dailyLogId, String userId);
    void deleteDailyLog(String dailyLogId, String userId);
}