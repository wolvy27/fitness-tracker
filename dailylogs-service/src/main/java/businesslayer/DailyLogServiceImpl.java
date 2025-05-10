package businesslayer;


import dataaccesslayer.DailyLog;
import dataaccesslayer.DailyLogRepository;
import org.springframework.stereotype.Service;
import presentationlayer.DailyLogRequestModel;
import presentationlayer.DailyLogResponseModel;
import utils.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyLogServiceImpl implements DailyLogService {

    @Override
    public List<DailyLogResponseModel> getDailyLogs(String userId) {
        return List.of();
    }

    @Override
    public DailyLogResponseModel getDailyLogByDailyLogId(String dailyLogId, String userId) {
        return null;
    }

    @Override
    public DailyLogResponseModel addDailyLog(DailyLogRequestModel newDailyLogData, String userId) {
        return null;
    }

    @Override
    public DailyLogResponseModel updateDailyLog(DailyLogRequestModel newDailyLogData, String dailyLogId, String userId) {
        return null;
    }

    @Override
    public void deleteDailyLog(String dailyLogId, String userId) {

    }
}
