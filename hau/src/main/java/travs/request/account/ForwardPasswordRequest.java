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
public class ForwardPasswordRequest {

    @NotBlank(message = AccountConstants.TOKEN_REQUIRED)
    private String token;
    @NotBlank(message = AccountConstants.PASSWORD_REQUIRED)
    private String password;
}
