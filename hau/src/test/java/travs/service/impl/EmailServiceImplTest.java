package travs.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import travs.dao.SendEmailAccountDao;
import travs.entity.account.Account;
import travs.entity.account.Role;
import travs.exception.RestApiException;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private SendEmailAccountDao mockSendEmailAccountDao;
    @Mock
    private JavaMailSender mockJavaMailSender;

    @InjectMocks
    private EmailServiceImpl emailServiceImplUnderTest;

    @Test
    void testSendSimpleMessage_Success_Test() throws Exception {
        // Setup
        // Configure JavaMailSender.createMimeMessage(...).
        final MimeMessage message = new MimeMessage(Session.getInstance(new Properties()));
        when(mockJavaMailSender.createMimeMessage()).thenReturn(message);

        // Run the test
        emailServiceImplUnderTest.sendSimpleMessage("email", "subject", "text");

        // Verify the results
        verify(mockJavaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void testUpdateResetPasswordToken_Success_Test() {
        // Setup
        // Configure SendEmailAccountDao.findAccountByEmail(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "token");
        when(mockSendEmailAccountDao.findAccountByEmail("email")).thenReturn(account);

        // Configure SendEmailAccountDao.save(...).
        final Account account1 = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "token");
        when(mockSendEmailAccountDao.save(any(Account.class))).thenReturn(account1);

        // Run the test
        emailServiceImplUnderTest.updateResetPasswordToken("token", "email");

        // Verify the results
        verify(mockSendEmailAccountDao).save(any(Account.class));
    }

    @Test
    void testUpdateResetPasswordToken_IsNull_Fail_Test() {
        // Run the test
        Assertions.assertThrows(RestApiException.class, () -> {
            emailServiceImplUnderTest.updateResetPasswordToken("token", "email");
        });
    }

    @Test
    void testGetByResetPasswordToken_Success_Test() {
        // Setup
        // Configure SendEmailAccountDao.findAccountByResetPasswordToken(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "token");
        when(mockSendEmailAccountDao.findAccountByResetPasswordToken("token")).thenReturn(account);

        // Run the test
        final Account result = emailServiceImplUnderTest.getByResetPasswordToken("token");
        assertThat(result).isEqualTo(account);
        assertThat(result).isNotNull();
    }

    @Test
    void testUpdatePassWord_Success_Test() {
        // Setup
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "token");

        // Configure SendEmailAccountDao.save(...).
        final Account account1 = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "token");
        when(mockSendEmailAccountDao.save(any(Account.class))).thenReturn(account1);

        // Run the test
        emailServiceImplUnderTest.updatePassWord(account, "newPassword");

        // Verify the results
        verify(mockSendEmailAccountDao).save(any(Account.class));
    }
}
