package travs.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import travs.constant.Constants;
import travs.constant.StatusCode;
import travs.dao.SendEmailAccountDao;
import travs.entity.account.Account;
import travs.exception.RestApiException;
import travs.service.EmailService;
import travs.utils.PasswordGenerator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final SendEmailAccountDao sendEmailAccountDao;

    final JavaMailSender javaMailSender;


    @Override
    @Async
    public void sendSimpleMessage(String email, String subject, String text) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, Constants.ENCODING_UTF8);
        helper.setFrom("travelsphau@gmail.com");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(text, true);
        javaMailSender.send(message);
        log.info("Send email successfully to email:{}", email);

    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        Account account = sendEmailAccountDao.findAccountByEmail(email);
        if (Objects.isNull(account)) {
            throw new RestApiException(StatusCode.CUSTOMER_EMAIL_NOT_FOUND.getStatus()
                    , StatusCode.CUSTOMER_EMAIL_NOT_FOUND.getMessage() + email);
        }
        account.setResetPasswordToken(token);
        sendEmailAccountDao.save(account);
        log.info("Reset password token  successfully with token:{}", token);
    }

    @Override
    public Account getByResetPasswordToken(String token) {
        return sendEmailAccountDao.findAccountByResetPasswordToken(token);
    }

    @Override
    public void updatePassWord(Account account, String newPassword) {
        String encodedPassword = PasswordGenerator.getHashString(newPassword);
        account.setPassword(encodedPassword);

        account.setResetPasswordToken(null);
        sendEmailAccountDao.save(account);
        log.info("Update New Password successfully!");
    }
}
