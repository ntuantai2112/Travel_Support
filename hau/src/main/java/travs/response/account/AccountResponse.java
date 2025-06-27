package travs.response.account;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private String id;
    private String name;
    private String password;
    private String phone;
    private String email;
    private Boolean enabled;
    private String gender;
    private String image;
    private String dob;
    private Long roleId;
    private String roleName;
}
