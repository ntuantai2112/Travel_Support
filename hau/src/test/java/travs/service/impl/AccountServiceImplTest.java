package travs.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import travs.dao.AccountDAO;
import travs.entity.account.Account;
import travs.entity.account.Role;
import travs.exception.RestApiException;
import travs.mapper.AccountMapper;
import travs.model.UserPrincipal;
import travs.request.account.AccountRequest;
import travs.request.account.ChangePasswordRequest;
import travs.response.ApiResponse;
import travs.response.account.AccountResponse;
import travs.service.EmailService;
import travs.utils.FileStore;

import javax.mail.MessagingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountDAO mockAccountDAO;
    @Mock
    private EmailService mockEmailService;

    @Mock
    private AccountMapper accountMapper;


    private AccountServiceImpl accountServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        accountServiceImplUnderTest = new AccountServiceImpl(mockAccountDAO, mockEmailService,accountMapper);
    }

    @Test
    void testAdd_Success_Test() {
        // Setup
        final AccountRequest request = new AccountRequest("id", "name", "password", "0123465790", "email@gmail.com", false, false,
                "role", "image", "dob", 0L, null);

        // Configure AccountDAO.getAccountByEmail(...).
        when(mockAccountDAO.getAccountByEmail(any())).thenReturn(null);

        // Configure AccountDAO.save(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.save(any(Account.class))).thenReturn(account);

        // Run the test
        accountServiceImplUnderTest.add(request);

        // Verify the results
        verify(mockAccountDAO).save(any(Account.class));
    }

    @Test
    void testAdd_Fail_Test() throws MessagingException {
        // Setup
        final AccountRequest request = new AccountRequest("id", "name", "password", "0123465790", "email@gmail.com", false, false,
                "role", "image", "dob", 0L, null);

        // Configure AccountDAO.getAccountByEmail(...).
        when(mockAccountDAO.getAccountByEmail(any())).thenReturn(null);

        // Configure AccountDAO.save(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.save(any(Account.class))).thenReturn(account);
        doThrow(MessagingException.class).when(mockEmailService).sendSimpleMessage(any(), any(), any());
        // Run the test
        accountServiceImplUnderTest.add(request);
        verify(mockAccountDAO).save(any(Account.class));
    }

    @Test
    void testAdd_ObjNull_ThrowsRestApiException_Fail_Test() throws Exception {
        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.add(null));

    }

    @Test
    void testAdd_Email_ThrowsRestApiException_Fail_Test() throws Exception {
        // Setup
        final AccountRequest request = new AccountRequest("id", "name", "password", "phone", "email", false, false,
                "role", "image", "dob", 0L, null);

        // Configure AccountDAO.getAccountByEmail(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.getAccountByEmail(any())).thenReturn(account);

        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.add(request));

    }

    @Test
    void testAdd_isEmail_ThrowsRestApiException_Fail_Test() throws Exception {
        // Setup
        final AccountRequest request = new AccountRequest("id", "name", "password", "phone", "email", false, false,
                "role", "image", "dob", 0L, null);

        when(mockAccountDAO.getAccountByEmail(any())).thenReturn(null);
        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.add(request));

    }

    @Test
    void testAdd_isPhone_ThrowsRestApiException_Fail_Test() throws Exception {
        // Setup
        final AccountRequest request = new AccountRequest("id", "name", "password", "phone", "email@gmail.com", false, false,
                "role", "image", "dob", 0L, null);

        when(mockAccountDAO.getAccountByEmail(any())).thenReturn(null);
        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.add(request));

    }

    @Test
    void testResgister_Success_Test() {
        // Setup
        final AccountRequest request = new AccountRequest("id", "name", "password", "01234567890", "email@gmail.com", false, false,
                "role", "image", "dob", 0L, null);

        // Configure AccountDAO.getAccountByEmail(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.getAccountByEmail(any())).thenReturn(null);

        // Configure AccountDAO.save(...).
        final Account account1 = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.save(any(Account.class))).thenReturn(account1);

        // Run the test
        accountServiceImplUnderTest.resgister(request);

        // Verify the results
        verify(mockAccountDAO).save(any(Account.class));
    }

    @Test
    void testResgister_objNull_ThrowRestApiException_Fail_Test() {
        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.resgister(null));
    }

    @Test
    void testResgister_Email_ThrowRestApiException_Fail_Test() {
        final AccountRequest request = new AccountRequest("id", "name", "password", "phone", "email", false, false,
                "role", "image", "dob", 0L, null);

        // Configure AccountDAO.getAccountByEmail(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.getAccountByEmail(any())).thenReturn(account);

        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.resgister(request));
    }

    @Test
    void testResgister_isEmail_ThrowRestApiException_Fail_Test() {
        final AccountRequest request = new AccountRequest("id", "name", "password", "phone", "email", false, false,
                "role", "image", "dob", 0L, null);

        when(mockAccountDAO.getAccountByEmail(any())).thenReturn(null);

        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.resgister(request));
    }

    @Test
    void testResgister_isPhone_ThrowRestApiException_Fail_Test() {
        final AccountRequest request = new AccountRequest("id", "name", "password", "phone", "email@gmail.com", false, false,
                "role", "image", "dob", 0L, null);

        when(mockAccountDAO.getAccountByEmail(any())).thenReturn(null);

        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.resgister(request));
    }

    @Test
    void testUpdate_Success_Test() {
        // Setup
        final AccountRequest request = new AccountRequest("id", "name", "password", "01234567890", "email@gmail.com", false, false,
                "role", "image", "dob", 0L, null);

        // Configure AccountDAO.getAccountById(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.getAccountById(any())).thenReturn(account);

        // Configure AccountDAO.save(...).
        final Account account1 = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.save(any(Account.class))).thenReturn(account1);

        // Run the test
        accountServiceImplUnderTest.update(request, "id");

        // Verify the results
        verify(mockAccountDAO).save(any(Account.class));
    }

    @Test
    void testUpdate_objNull_ThrowRestApiException_Fail_Test() {
        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.update(null, null));
    }

    @Test
    void testUpdate_isEmail_ThrowRestApiException_Fail_Test() {
        final AccountRequest request = new AccountRequest("id", "name", "password", "01234567890", "email", false, false,
                "role", "image", "dob", 0L, null);

        // Run the test
        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.update(request, "id"));
    }

    @Test
    void testUpdate_isPhone_ThrowRestApiException_Fail_Test() {
        final AccountRequest request = new AccountRequest("id", "name", "password", "phone", "email@gmail.com", false, false,
                "role", "image", "dob", 0L, null);

        // Run the test
        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.update(request, "id"));
    }

    @Test
    void testUpdate_AccountNull_ThrowRestApiException_Fail_Test() {
        final AccountRequest request = new AccountRequest("id", "name", "password", "01234567890", "email@gmail.com", false, false,
                "role", "image", "dob", 0L, null);
        when(mockAccountDAO.getAccountById(any())).thenReturn(null);
        // Run the test
        assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.update(request, "id"));
    }

    @Test
    void testChangePassword_Success_Test() {
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            // Setup
            final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("id", "Test123576@1", "Test123576@12");

            // Configure AccountDAO.getAccountById(...).
            final Account account = new Account("id", "name", "email", "phone", false, "$2a$12$P.P8YJzUY4kw4.KuXaqSL.ys1bSs1EZPWPT3feBe2i8CoStrHBmx.", "image", "dob", false,
                    new Role(0L, "name"), "resetPasswordToken");
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            UserPrincipal userPrincipal = mock(UserPrincipal.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userPrincipal);
            when(userPrincipal.getId()).thenReturn("id");
            when(mockAccountDAO.getAccountById(any())).thenReturn(account);

            // Configure AccountDAO.save(...).
            final Account account1 = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                    new Role(0L, "name"), "resetPasswordToken");
            when(mockAccountDAO.save(any(Account.class))).thenReturn(account1);
            // Run the test
            accountServiceImplUnderTest.changePassword(changePasswordRequest);
            // Verify the results
            verify(mockAccountDAO, atLeast(1)).save(any(Account.class));
        }
    }

    @Test
    void testChangePassword_WrongPassword_Fail_Test() {
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            // Setup
            final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("id", "Test123576@1", "Test123576@12");

            // Configure AccountDAO.getAccountById(...).
            final Account account = new Account("id", "name", "email", "phone", false, "2a$12$P.P8YJzUY4kw4.KuXaqSL.ys1bSs1EZPWPT3feBe2i8CoStrHBmx.", "image", "dob", false,
                    new Role(0L, "name"), "resetPasswordToken");
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            UserPrincipal userPrincipal = mock(UserPrincipal.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userPrincipal);
            when(userPrincipal.getId()).thenReturn("id");
            when(mockAccountDAO.getAccountById(any())).thenReturn(account);

            // Configure AccountDAO.save(...).
            final Account account1 = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                    new Role(0L, "name"), "resetPasswordToken");
            // Run the test
            Assertions.assertThrows(DataIntegrityViolationException.class, () -> accountServiceImplUnderTest.changePassword(changePasswordRequest));
        }
    }

    @Test
    void testChangePassword_isPassword_Fail_Test() {
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            // Setup
            final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("id", "Test123576@1", "12312312");

            // Configure AccountDAO.getAccountById(...).
            final Account account = new Account("id", "name", "email", "phone", false, "2a$12$P.P8YJzUY4kw4.KuXaqSL.ys1bSs1EZPWPT3feBe2i8CoStrHBmx.", "image", "dob", false,
                    new Role(0L, "name"), "resetPasswordToken");
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            UserPrincipal userPrincipal = mock(UserPrincipal.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userPrincipal);
            when(userPrincipal.getId()).thenReturn("id");
            when(mockAccountDAO.getAccountById(any())).thenReturn(account);

            // Configure AccountDAO.save(...).
            final Account account1 = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                    new Role(0L, "name"), "resetPasswordToken");
            // Run the test
            Assertions.assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.changePassword(changePasswordRequest));
        }
    }

    @Test
    void testChangePassword_PasswordIsNewPassword_Fail_Test() {
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            // Setup
            final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("id", "Test123576@1", "Test123576@1");

            // Configure AccountDAO.getAccountById(...).
            final Account account = new Account("id", "name", "email", "phone", false, "$2a$12$P.P8YJzUY4kw4.KuXaqSL.ys1bSs1EZPWPT3feBe2i8CoStrHBmx.", "image", "dob", false,
                    new Role(0L, "name"), "resetPasswordToken");
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            UserPrincipal userPrincipal = mock(UserPrincipal.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userPrincipal);
            when(userPrincipal.getId()).thenReturn("id");
            when(mockAccountDAO.getAccountById(any())).thenReturn(account);

            // Configure AccountDAO.save(...).
            final Account account1 = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                    new Role(0L, "name"), "resetPasswordToken");
            // Run the test
            Assertions.assertThrows(RestApiException.class, () -> accountServiceImplUnderTest.changePassword(changePasswordRequest));
        }
    }

    @Test
    void testUpdateProfile_Success_Test() {
        // Setup
        try (MockedStatic<FileStore> mockedStatic = mockStatic(FileStore.class)) {
            final AccountRequest request = new AccountRequest("id", "name", "password", "01234567890", "email@gmail.com", false, false,
                    "role", "image", "dob", 0L, null);

            // Configure AccountDAO.getAccountById(...).
            final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                    new Role(0L, "name"), "resetPasswordToken");
            when(mockAccountDAO.getAccountById("id")).thenReturn(account);
            mockedStatic.when(() -> FileStore.getFilePath(any(), any())).thenReturn("src/test/resources");
            // Configure AccountDAO.save(...).
            final Account account1 = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                    new Role(0L, "name"), "resetPasswordToken");
            when(mockAccountDAO.save(any(Account.class))).thenReturn(account1);

            // Run the test
            accountServiceImplUnderTest.updateProfile(request, "id");

            // Verify the results
            verify(mockAccountDAO).save(any(Account.class));
        }
    }

    @Test
    void testUpdateProfile_ObjisNull_Fail_Test() {
        // Run the test
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.updateProfile(null, "id");
        });
    }

    @Test
    void testUpdateProfile_AccountisNull_Fail_Test() {
        // Setup
        final AccountRequest request = new AccountRequest("id", "name", "password", "01234567890", "email@gmail.com", false, false,
                "role", "image", "dob", 0L, null);

        // Configure AccountDAO.getAccountById(...).
        when(mockAccountDAO.getAccountById("id")).thenReturn(null);

        // Run the test
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.updateProfile(request, "id");
        });
    }

    @Test
    void testUpdateProfile_isEmail_Fail_Test() {
        // Setup
        final AccountRequest request = new AccountRequest("id", "name", "password", "01234567890", "emailgmail.com", false, false,
                "role", "image", "dob", 0L, null);

        // Run the test
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.updateProfile(request, "id");
        });
    }

    @Test
    void testUpdateProfile_isPhone_Fail_Test() {
        // Setup
        final AccountRequest request = new AccountRequest("id", "name", "password", "012345678", "email@gmail.com", false, false,
                "role", "image", "dob", 0L, null);

        // Run the test
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.updateProfile(request, "id");
        });
    }

    @Test
    void testDelete_Success_Test() {
        // Run the test
        accountServiceImplUnderTest.delete("id");
        // Verify the results
        verify(mockAccountDAO).deleteAllById("id");
    }

    @Test
    void testDelete_Fail_Test() {
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.delete("");
        });
    }

    @Test
    void testSearchByNameEmailRole() {
        // Setup
        when(mockAccountDAO.countAccountByNameAndEmailAndRole(any(), any(), any())).thenReturn(0L);

        // Configure AccountDAO.searchAccountByNameEmailRole(...).
        final Page<Account> accounts = new PageImpl<>(
                List.of(new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                        new Role(0L, "name"), "resetPasswordToken")));
        when(mockAccountDAO.searchAccountByNameEmailRole(any(), any(), any(),
                any(Pageable.class))).thenReturn(accounts);

        // Run the test
        final ApiResponse result = accountServiceImplUnderTest.searchByNameEmailRole("name", "email", "role", 1, 2);

        // Verify the results
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getTotalElement());
    }

    @Test
    void testSearchByNameEmailRole_Success_Test() {
        // Setup
        when(mockAccountDAO.countAccountByNameAndEmailAndRole(any(), any(), any())).thenReturn(0L);

        // Configure AccountDAO.searchAccountByNameEmailRole(...).
        final Page<Account> accounts = new PageImpl<>(
                List.of(new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                        new Role(0L, "name"), "resetPasswordToken")));
        when(mockAccountDAO.searchAccountByNameEmailRole(any(), any(), any(),
                any(Pageable.class))).thenReturn(accounts);

        // Run the test
        final ApiResponse result = accountServiceImplUnderTest.searchByNameEmailRole("name", "email", "", 1, 2);

        // Verify the results
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getTotalElement());
    }

    @Test
    void testGetById_Success_Test() {
        // Setup
        // Configure AccountDAO.getAccountById(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.getAccountById("id")).thenReturn(account);

        // Run the test
        final AccountResponse result = accountServiceImplUnderTest.getById("id");

        // Verify the results
        Assertions.assertNotNull(result);
        Assertions.assertEquals("id", result.getId());
        Assertions.assertEquals("name", result.getName());
        Assertions.assertEquals("email", result.getEmail());
        Assertions.assertEquals("phone", result.getPhone());
    }

    @Test
    void testGetById_Fail_Test() {
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.getById("");
        });
    }

    @Test
    void testGetById_isNull_Fail_Test() {
        // Setup
        when(mockAccountDAO.getAccountById("id")).thenReturn(null);
        // Run the test
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.getById("id");
        });
    }

    @Test
    void testGetByEmailSuccess_Test() {
        // Setup
        // Configure AccountDAO.getAccountByEmail(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.getAccountByEmail("email")).thenReturn(account);

        // Run the test
        final AccountResponse result = accountServiceImplUnderTest.getByEmail("email");

        // Verify the results
        Assertions.assertNotNull(result);
        Assertions.assertEquals("id", result.getId());
        Assertions.assertEquals("name", result.getName());
        Assertions.assertEquals("email", result.getEmail());
        Assertions.assertEquals("phone", result.getPhone());
    }

    @Test
    void testGetByEmail_Fail_Test() {
        when(mockAccountDAO.getAccountByEmail("email")).thenReturn(null);

        // Run the test
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.getByEmail("email");
        });
    }

    @Test
    void testGetByEmail_isEmpty_Fail_Test() {
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.getByEmail("");
        });
    }

    @Test
    void testChangeAccountLock_Success_Test() {
        // Setup
        // Configure AccountDAO.getAccountById(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.getAccountById("id")).thenReturn(account);

        // Configure AccountDAO.save(...).
        final Account account1 = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.save(any(Account.class))).thenReturn(account1);

        // Run the test
        accountServiceImplUnderTest.changeAccountLock("id");

        // Verify the results
        verify(mockAccountDAO).save(any(Account.class));
    }

    @Test
    void testChangeAccountLock_isEnpty_Fail_Test() {
        // Run the test
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.changeAccountLock("");
        });
    }

    @Test
    void testChangeAccountLock_isNull_Fail_Test() {
        when(mockAccountDAO.getAccountById("id")).thenReturn(null);

        // Run the test
        Assertions.assertThrows(RestApiException.class, () -> {
            accountServiceImplUnderTest.changeAccountLock("id");
        });
    }

    @Test
    void testConvert() {
        // Setup
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");

        // Run the test
        final AccountResponse result = accountServiceImplUnderTest.convert(account);

        // Verify the results
        Assertions.assertNotNull(result);
        Assertions.assertEquals("id", result.getId());
        Assertions.assertEquals("name", result.getName());
        Assertions.assertEquals("email", result.getEmail());
        Assertions.assertEquals("phone", result.getPhone());
    }

    @Test
    void testConvert_Success_Test() {
        // Setup
        final Account account = new Account("id", "name", "email", "phone", true, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");

        // Run the test
        final AccountResponse result = accountServiceImplUnderTest.convert(account);

        // Verify the results
        Assertions.assertNotNull(result);
        Assertions.assertEquals("id", result.getId());
        Assertions.assertEquals("name", result.getName());
        Assertions.assertEquals("email", result.getEmail());
        Assertions.assertEquals("phone", result.getPhone());
    }

    @Test
    void testLoadUserByUsername() {
        // Setup
        // Configure AccountDAO.getAccountByEmail(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.getAccountByEmail("email")).thenReturn(account);

        // Run the test
        final UserDetails result = accountServiceImplUnderTest.loadUserByUsername("email");

        // Verify the results
        Assertions.assertNotNull(result);
        Assertions.assertEquals("email", result.getUsername());
        Assertions.assertEquals("password", result.getPassword());
    }

    @Test
    void testLoadUserByUsername_AccountDAOReturnsNull() {
        // Setup
        when(mockAccountDAO.getAccountByEmail("email")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> accountServiceImplUnderTest.loadUserByUsername("email"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

}
