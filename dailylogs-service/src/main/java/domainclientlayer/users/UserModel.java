package domainclientlayer.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class UserModel {
    private String userId;
    private String firstName;
    private String lastName;
}
