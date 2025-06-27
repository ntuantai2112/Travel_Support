package travs.service.impl;

import travs.constant.Constants;
import travs.dao.SendEmailAccountDao;
import travs.entity.account.Account;
import travs.exception.RestApiException;
import travs.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@Component
@Transactional
public class EmailServiceImpl implements EmailService {

    @Autowired
    private SendEmailAccountDao sendEmailAccountDao;

    @Autowired
    JavaMailSender javaMailSender;


    @Override
    @Async
    public void sendSimpleMessage(String email, String subject, String text) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper   = new MimeMessageHelper(message , Constants.ENCODING_UTF8);
        helper.setFrom("travelsphau@gmail.com");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(text , true);
        javaMailSender.send(message);
//        ltwhfaiyavsvdyuu

    }

    @Override
    public void updateResetPasswordToken(String token ,String email) {
        Account account = sendEmailAccountDao.findAccountByEmail(email);
        if (Objects.isNull(account)) {
            throw new RestApiException(4,"Could not find any customer with the email : " + email);
        }

            account.setResetPasswordToken(token);
            sendEmailAccountDao.save(account);
    }

    @Override
    public Account getByResetPasswordToken(String token) {
        return sendEmailAccountDao.findAccountByResetPasswordToken(token);
    }

    @Override
    public void updatePassWord(Account account, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(encodedPassword);

        account.setResetPasswordToken(null);
        sendEmailAccountDao.save(account);

    }
}
