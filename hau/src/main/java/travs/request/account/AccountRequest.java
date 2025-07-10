package travs.request.account;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import travs.constant.AccountConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountRequest {
    private String id;


    @NotBlank(message = AccountConstants.NAME_REQUIRED)
    private String name;

    private String password;

    @NotBlank(message = AccountConstants.PHONE_REQUIRED)
    private String phone;

    @NotBlank(message = AccountConstants.EMAIL_REQUIRED)
    private String email;

    private Boolean enabled;

    private Boolean gender;

    private String role;

    private String image;

    @NotBlank(message = AccountConstants.DOB_REQUIRED)
    private String dob;

    private Long roleId;


    @JsonIgnore
    private MultipartFile multipartFile;

}
