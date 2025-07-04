package travs.impl;


import travs.constant.Constants;
import travs.dao.AccountDAO;
import travs.entity.account.Account;
import travs.entity.account.Role;
import travs.mapper.AccountMapper;
import travs.model.AccountDTO;
import travs.request.account.AccountRequest;
import travs.response.ApiResponse;
import travs.response.account.AccountResponse;
import travs.service.EmailService;
import travs.service.impl.AccountServiceImpl;
import travs.utils.FileStore;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    @Mock
    AccountDAO accountDAO;

    @Mock
    private EmailService emailService;

    @Mock
    private AccountMapper accountMapper;

    Account account = Account.builder()
            .id("1")
            .dob("2101/12/12")
            .email("TravSFu@gmail.com")
            .enabled(true)
            .name("trav")
            .gender(true)
            .password("123456")
            .role(new Role(1L, "ROLE_ADMIN"))
            .image("aaaaaaaaaaaaa.jpg")
            .phone("0983302976")
            .password("123456")
            .build();

    AccountRequest request = AccountRequest.builder()
            .id("1")
            .dob("2101/12/12")
            .email("TravSFu@gmail.com")
            .enabled(true)
            .name("tuan")
            .gender(true)
//                .role(new Role(1L , "ROLE_ADMIN"))
            .role("ROLE_MEMBER")
            .roleId(1L)
            .image("aaaaaaaaaaaaa.jpg")
            .phone("0983302976")
            .password("123456")
            .build();

    AccountDTO accountDTO1 = AccountDTO.builder()
            .id("1")
            .name("nguyen minh tuan")
            .dob("2000/12/12")
            .email("tuan.nguyen@ekoios.vn")
            .enabled(true)
            .phone("0983302976")
            .roleId(5L)
            .enabled(true)
            .build();
    AccountDTO accountDTO2 = AccountDTO.builder()
            .id("1234")
            .name("nguyen minh tuan ekoios")
            .dob("2000/12/12")
            .email("tuan.nguyen@ekoios.vn")
            .enabled(true)
            .phone("0983302977")
            .roleId(4L)
            .role("ROLE_ADMIN")
            .enabled(true)
            .build();


    @Test
    public void GetAccount_Success() {

        List<AccountDTO> listAccount = new ArrayList<>();
        listAccount.add(accountDTO1);
        listAccount.add(accountDTO2);

        ApiResponse apiResponse = ApiResponse.builder()
                .data(listAccount)
                .totalElement((long) listAccount.size())
                .build();

        Page<Account> accounts = Mockito.mock(Page.class);

        Mockito.when(accountDAO.searchAccountByNameEmailRole(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(accounts);
        Mockito.when(accountDAO.countAccountByNameAndEmailAndRole(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(1L);
        ApiResponse apiResponse1 = accountServiceImpl.searchByNameEmailRole("travalar", "TravSFu@gmail.com", "ROLE_ADMIN", 1, 2);
        MatcherAssert.assertThat(apiResponse1.getTotalElement(), Matchers.is(1L));
    }

    @Test
    public void getById_Success() {

        AccountResponse accountResponse1 = convert(account);
        Mockito.when(accountDAO.getAccountById(Mockito.anyString())).thenReturn(account);
        accountResponse1 = accountServiceImpl.getById("1");
        MatcherAssert.assertThat(accountResponse1.getId(), Matchers.is("1"));
    }

    @Test
    public void getByEmail_Success() {

        AccountResponse accountResponse1 = convert(account);
        Mockito.when(accountDAO.getAccountByEmail(Mockito.anyString())).thenReturn(account);
        accountResponse1 = accountServiceImpl.getByEmail("TravSFu@gmail.com");
        MatcherAssert.assertThat(accountResponse1.getEmail(), Matchers.is("TravSFu@gmail.com"));

    }

    @Test
    public void delete_Success() {

        Mockito.doNothing().when(accountDAO).deleteAllById("1");
        accountServiceImpl.delete("1");
        MatcherAssert.assertThat(account.getId(), Matchers.is("1"));
    }


    @Test
    public void register_Success() {
//        Mockito.when(accountDAO.save(account)).thenReturn(account);
        //accountServiceImpl.resgister(request);
        // MatcherAssert.assertThat(account.getId(), Matchers.is("1"));
    }


    @Test
    public void addAccount_Success() {

        //Mockito.when(accountDAO.save(account)).thenReturn(account);

        //accountServiceImpl.

        //MatcherAssert.assertThat(request.getEmail(), Matchers.is("TravSFu@gmail.com"));

    }

    @Test
    public void updateAccount_Success() {

        Mockito.when(accountDAO.getAccountById(Mockito.any())).thenReturn(account);
        accountServiceImpl.update(request, "1");
        MatcherAssert.assertThat(request.getId(), Matchers.is("1"));
        MatcherAssert.assertThat(request.getRole(), Matchers.is("ROLE_MEMBER"));

    }


    @Test
    public void updateProfileAccount_Success() {


        Mockito.when(accountDAO.getAccountById(Mockito.any())).thenReturn(account);
        accountServiceImpl.update(request, "1");
        MatcherAssert.assertThat(request.getId(), Matchers.is("1"));
        MatcherAssert.assertThat(request.getRole(), Matchers.is("ROLE_MEMBER"));

    }

    @Test
    public void updateProfile_Success() {


        Mockito.when(accountDAO.getAccountById(Mockito.any())).thenReturn(account);

        String image1 = FileStore.getFilePath(request.getMultipartFile(), "-user");
        if (image1 != null) {
            request.setImage(image1);
        }

        account.setPhone(request.getPhone());
        account.setName(request.getName());
        account.setDob(request.getDob());
        account.setEmail(request.getEmail());
        account.setGender(request.getGender());
        if (image1 != null) {
            if (account.getImage() != null) {
                String image = account.getImage();
                FileStore.deleteFile(image);
            }
            account.setImage(request.getImage());
        }
        account.setDob(request.getDob());

        Mockito.when(accountDAO.save(account)).thenReturn(account);
        accountServiceImpl.updateProfile(request, "1");


    }

    public AccountResponse convert(Account account) {
        return accountMapper.toResponse(account);
    }


}
