package com.fitnesstracker.apigateway.businesslayer.dailylogs;

import com.fitnesstracker.apigateway.domainclientlayer.dailylogs.DailyLogServiceClient;
import com.fitnesstracker.apigateway.presentationlayer.dailylogs.DailyLogRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.dailylogs.DailyLogResponseModel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DailyLogServiceImpl implements DailyLogService {
    private final DailyLogServiceClient dailyLogServiceClient;

    public DailyLogServiceImpl(DailyLogServiceClient dailyLogServiceClient) {
        this.dailyLogServiceClient = dailyLogServiceClient;
    }

    @Override
    public List<DailyLogResponseModel> getDailyLogs(String userId) {
        return dailyLogServiceClient.getDailyLogs(userId);
    }

    @Override
    public DailyLogResponseModel getDailyLogByDailyLogId(String dailyLogId, String userId) {
        return dailyLogServiceClient.getDailyLogByDailyLogId(dailyLogId, userId);
    }

    @Override
    public DailyLogResponseModel addDailyLog(DailyLogRequestModel dailyLogRequestModel, String userId) {
        return dailyLogServiceClient.addDailyLog(dailyLogRequestModel, userId);
    }

    @Override
    public DailyLogResponseModel updateDailyLog(DailyLogRequestModel dailyLogRequestModel, String dailyLogId, String userId) {
        return dailyLogServiceClient.updateDailyLog(dailyLogRequestModel, dailyLogId, userId);
    }

    @Override
    public void deleteDailyLog(String dailyLogId, String userId) {
        dailyLogServiceClient.deleteDailyLog(dailyLogId, userId);
    }
}