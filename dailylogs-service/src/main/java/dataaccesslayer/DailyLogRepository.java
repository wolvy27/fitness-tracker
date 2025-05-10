package dataaccesslayer;



import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyLogRepository extends MongoRepository<DailyLog, String> {
    DailyLog findDailyLogByDailyLogIdentifier_DailyLogIdAndUserModel_userId(String dailyLogId, String userId);
    List<DailyLog> findAllByUserModel_userId(String userId);
    DailyLog findDailyLogByLogDateAndUserModel_userId(LocalDate logDate, String userId);

    // Simply to push change
}
