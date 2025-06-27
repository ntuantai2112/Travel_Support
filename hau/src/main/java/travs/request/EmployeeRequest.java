package travs.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

    private String name;
    private Boolean gender;
    private String dob;
    private String phoneNumber;
    private String email;
    private String identifyCard;
    private String currentAddress;
    private String homeTown;
    private Long position;

}
