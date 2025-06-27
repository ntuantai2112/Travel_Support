package travs.controller.acount;

import travs.dao.SendEmailAccountDao;
import travs.model.UserPrincipal;
import travs.service.AccountService;
import travs.service.EmailService;
import travs.utils.PropertiesReader;
import travs.utils.RandomNumber;
import travs.constant.Constants;
import travs.constant.PropertyKeys;
import travs.entity.account.Account;
import travs.exception.RestApiException;
import travs.request.account.AccountRequest;
import travs.request.account.ChangePasswordRequest;
import travs.request.account.ForwardPasswordRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AccountController {

    private SendEmailAccountDao  sendEmailAccountDao;

    private EmailService emailService;

    private AccountService accountService;


    // api update profile
    @PostMapping("/member/update/profile")
    public ResponseEntity<?> updateAccountProfile(@ModelAttribute AccountRequest accountRequest) {

        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        accountService.updateProfile(accountRequest, currentUser.getId());
        ResponseEntity<String> response = new ResponseEntity<>("Update profile success", HttpStatus.OK);
        return response;
    }

    // api forward-password
    @PostMapping(Constants.UrlPath.URL_API_FORWARD_PASSWORD)
    public ResponseEntity<?> sendEmail(HttpServletRequest request,
                                       @RequestParam(name = "email", required = false, defaultValue = "") String email) throws MessagingException, NoSuchAlgorithmException {

        String token = RandomNumber.getRandomNumberString();
        emailService.updateResetPasswordToken(token, email);

        String resetPassWordLink = "http://localhost:3000/forward-password";

        try {
            StringBuilder content = new StringBuilder();
            content.append("Hello");
            content.append("<p>You have requested to reset your password </p>");
            content.append("<p>Click the link below to change your password </p>");
            content.append("<p><b><a href=\"" + resetPassWordLink + "\"> Change my Password </a><b></p>");
            content.append("<p> Ignore this email if you do remember your password , or you havav not made the request</p>");
            content.append("<p>   your token  is :   \"" + token + "\"   </p>");


            emailService.sendSimpleMessage(email, PropertiesReader.getProperty(PropertyKeys.SEND_EMAIL), content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResponseEntity<String> response = new ResponseEntity<>("Send Link  Forward Password Success", HttpStatus.OK);
        return response;
    }

    // api reset password
    @PostMapping(Constants.UrlPath.URL_API_Reset_PassWord)
    public ResponseEntity<?> resetPassword(HttpServletRequest request,
                                           @RequestBody ForwardPasswordRequest forwardPasswordRequest) {
        String token = forwardPasswordRequest.getToken();
        Account account = sendEmailAccountDao.findAccountByResetPasswordToken(token);
        if (Objects.isNull(account)) {
            throw new RestApiException(400, "token false");
        }
        emailService.updatePassWord(account, forwardPasswordRequest.getPassword());
        ResponseEntity<String> response = new ResponseEntity<>("Reset Password Success", HttpStatus.OK);
        return response;
    }

    // api thay đổi pass word
    @PostMapping(Constants.UrlPath.URL_API_Change_PassWord)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {

        accountService.changePassword(changePasswordRequest);

        ResponseEntity<String> response = new ResponseEntity<>("Success", HttpStatus.OK);
        return response;
    }

    // api đăng ký tài khoản
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountRequest accountRequest) {
        accountService.resgister(accountRequest);
        ResponseEntity<String> response = new ResponseEntity<>(" Register Success", HttpStatus.OK);
        return response;
    }


}
