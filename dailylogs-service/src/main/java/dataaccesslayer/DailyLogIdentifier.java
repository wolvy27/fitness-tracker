package dataaccesslayer;


import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;


@Getter
@Data
public class DailyLogIdentifier {

    @Indexed(unique = true)
    private String dailyLogId;

    public DailyLogIdentifier() {
        this.dailyLogId = UUID.randomUUID().toString();
    }

    public DailyLogIdentifier(String dailyLogId) {
        this.dailyLogId = dailyLogId;
    }
}
