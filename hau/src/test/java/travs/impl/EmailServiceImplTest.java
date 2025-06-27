package travs.impl;


import travs.dao.SendEmailAccountDao;
import travs.entity.account.Account;
import travs.entity.account.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.service.impl.EmailServiceImpl;


@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @Mock
    SendEmailAccountDao sendEmailAccountDao;

    @InjectMocks
    EmailServiceImpl emailServiceImpl;

    Account account = Account.builder()
            .id("1")
            .dob("2101/12/12")
            .email("tuan.nguyen@gmail.com")
            .enabled(true)
            .name("tuan")
            .gender(true)
            .password("123456")
            .role(new Role(1L, "ROLE_ADMIN"))
            .image("aaaaaaaaaaaaa.jpg")
            .phone("0983302976")
            .password("123456")
            .resetPasswordToken("abc")
            .build();

    @Test
    public void testUpdateResetPasswordToken() {
        Mockito.when(sendEmailAccountDao.findAccountByEmail(Mockito.any())).thenReturn(account);

        Mockito.when(sendEmailAccountDao.save(Mockito.any())).thenReturn(account);
        emailServiceImpl.updateResetPasswordToken("abc" , "tuan.nguyen@gmail.com");

    }

    @Test
    public void testGetByResetPasswordToken() {
        Mockito.when(sendEmailAccountDao.findAccountByResetPasswordToken(Mockito.any())).thenReturn(account);
        emailServiceImpl.getByResetPasswordToken("abc");
    }


    @Test
    public void testUpdatePassWord() {
         String newPassword = "aaaaaaaaaa";
        Mockito.when(sendEmailAccountDao.save(Mockito.any())).thenReturn(account);

        sendEmailAccountDao.save(account);
        emailServiceImpl.updatePassWord(account ,newPassword);
    }

}
