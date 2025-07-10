package travs.controller.acount;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import travs.constant.Constants;
import travs.constant.PropertyKeys;
import travs.constant.StatusCode;
import travs.dao.SendEmailAccountDao;
import travs.entity.account.Account;
import travs.exception.RestApiException;
import travs.model.UserPrincipal;
import travs.request.account.AccountRequest;
import travs.request.account.ChangePasswordRequest;
import travs.request.account.ForwardPasswordRequest;
import travs.response.ApiResponse;
import travs.service.AccountService;
import travs.service.EmailService;
import travs.utils.AuthenticationUtils;
import travs.utils.PropertiesReader;
import travs.utils.RandomNumber;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final SendEmailAccountDao sendEmailAccountDao;

    private final EmailService emailService;

    private final AccountService accountService;


    // api update profile
    @PostMapping("/member/update/profile")
    public ApiResponse<Void> updateAccountProfile(@ModelAttribute AccountRequest accountRequest) {

        UserPrincipal currentUser = AuthenticationUtils.getUserInfo();
        accountService.updateProfile(accountRequest, currentUser.getId());
        return ApiResponse.build(StatusCode.UPDATE_PROFILE_SUCCESS.getStatus(),
                StatusCode.UPDATE_PROFILE_SUCCESS.getMessage());
    }

    // api forward-password with send Email
    @PostMapping(Constants.UrlPath.URL_API_FORWARD_PASSWORD)
    public ApiResponse<Void> sendEmail(@RequestParam(name = "email", required = false, defaultValue = "") String email) throws NoSuchAlgorithmException {

        String token = RandomNumber.getRandomNumberString();
        emailService.updateResetPasswordToken(token, email);

        String resetPassWordLink = "http://localhost:3000/forward-password";

        try {
            StringBuilder content = new StringBuilder();
            content.append("Hello");
            content.append("<p>You have requested to reset your password </p>");
            content.append("<p>Click the link below to change your password </p>");
            content.append("<p><b><a href=\"" + resetPassWordLink + "\"> Change my Password </a><b></p>");
            content.append("<p> Ignore this email if you do remember your password , or you have  not made the request</p>");
            content.append("<p>   your token  is :   \"" + token + "\"   </p>");


            emailService.sendSimpleMessage(email, PropertiesReader.getProperty(PropertyKeys.SEND_EMAIL), content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.build(StatusCode.RESET_PASSWORD_SUCCESS.getStatus(), StatusCode.RESET_PASSWORD_SUCCESS.getMessage());
    }

    // api reset password
    @PostMapping(Constants.UrlPath.URL_API_RESET_PASSWORD)
    public ApiResponse<Void> resetPassword(@RequestBody @Valid ForwardPasswordRequest forwardPasswordRequest) {
        String token = forwardPasswordRequest.getToken();
        Account account = sendEmailAccountDao.findAccountByResetPasswordToken(token);
        if (Objects.isNull(account)) {
            throw new RestApiException(StatusCode.TOKEN_INVALID.getStatus(),
                    StatusCode.TOKEN_INVALID.getMessage());
        }
        emailService.updatePassWord(account, forwardPasswordRequest.getPassword());
        return ApiResponse.build(StatusCode.RESET_PASSWORD_SUCCESS.getStatus(),
                StatusCode.RESET_PASSWORD_SUCCESS.getMessage());
    }

    // api thay đổi pass word
    @PostMapping(Constants.UrlPath.URL_API_CHANCE_PASSWORD)
    public ApiResponse<Void> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        accountService.changePassword(changePasswordRequest);
        return ApiResponse.build(StatusCode.SUCCESS.getStatus(), StatusCode.SUCCESS.getMessage());
    }

    // api đăng ký tài khoản
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody @Valid AccountRequest accountRequest) {
        accountService.resgister(accountRequest);
        return ApiResponse.build(StatusCode.REGISTER_SUCCESS.getStatus(), StatusCode.REGISTER_SUCCESS.getMessage());
    }


}
