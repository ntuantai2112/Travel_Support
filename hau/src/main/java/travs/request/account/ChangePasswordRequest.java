package travs.request.account;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import travs.constant.AccountConstants;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    private String id;
    @NotBlank(message = AccountConstants.PASSWORD_REQUIRED)
    private String password;
    @NotBlank(message = AccountConstants.NEW_PASSWORD_REQUIRED)
    private String newPassword;
}
