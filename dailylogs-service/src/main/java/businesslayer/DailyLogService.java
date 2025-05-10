package businesslayer;



import presentationlayer.DailyLogRequestModel;
import presentationlayer.DailyLogResponseModel;

import java.util.List;

public interface DailyLogService {
    List<DailyLogResponseModel> getDailyLogs(String userId);
    DailyLogResponseModel getDailyLogByDailyLogId(String dailyLogId, String userId);
    DailyLogResponseModel addDailyLog(DailyLogRequestModel newDailyLogData, String userId);
    DailyLogResponseModel updateDailyLog(DailyLogRequestModel newDailyLogData, String dailyLogId, String userId);
    void deleteDailyLog(String dailyLogId, String userId);
}
