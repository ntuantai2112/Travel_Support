package travs.service;

import travs.entity.account.Account;

import javax.mail.MessagingException;

public interface EmailService {


    void sendSimpleMessage(String email, String subject, String text) throws MessagingException;

    void updateResetPasswordToken(String token, String email);

    Account getByResetPasswordToken(String token);

    void updatePassWord(Account account, String newPassword);

}
