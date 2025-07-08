package travs.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import travs.constant.AccountConstants;
import travs.constant.RoleEnum;
import travs.constant.StatusCode;
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
import travs.service.AccountService;
import travs.service.EmailService;
import travs.utils.FileStore;
import travs.utils.PasswordGenerator;
import travs.utils.ValidateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
@Log4j2
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private final AccountDAO accountDAO;

    private final EmailService emailService;

    private final AccountMapper accountMapper;


    //add account
    @Override
    public void add(AccountRequest request) {
        validateRegisterAccount(request);
        Account account = accountMapper.toEntity(request);
        account.setPassword(PasswordGenerator.getHashString("123@123aB"));
        accountDAO.save(account);

        //send mail
        sendMail(request.getEmail());
        log.info("Add Account Successfully!");
    }


    @Override
    public void resgister(AccountRequest request) {
        validateRegisterAccount(request);
        Account account = accountMapper.toEntity(request);
        accountDAO.save(account);
        log.info("Register Account Successfully!");
    }


    private void sendMail(String email) {
        try {
            StringBuilder content = new StringBuilder();
            content.append("Xin Chào");
            content.append("<p> tài khoản của bạn là  \"" + email + "\"   </p>");
            content.append("<p>mật khẩu của bạn là : 123@123aB  </p>");
            content.append("<p>Yêu Cầu bạn đổi mật khẩu khi đăng nhập vào hệ thống  </p>");

            String subject = "HAU ---THÔNG TIN TÀI KHOẢN";
            emailService.sendSimpleMessage(email, subject, content.toString());
        } catch (Exception e) {
            log.debug(e);
        }
    }

    @Override
    public void update(AccountRequest request, String id) {

        validateRegisterAccount(request);
        Account account = accountDAO.getAccountById(id);
        if (Objects.isNull(account)) {
            throw new RestApiException(StatusCode.ACCOUNT_NOT_EXIST);
        }
        account.setEmail(request.getEmail());
        account.setDob(request.getDob());
        account.setPhone(request.getPhone());
        if (account.getRole() != null) {
            account.setRole(new Role(request.getRoleId()));
        }
        account.setName(request.getName());
        account.setGender(request.getGender());
        accountDAO.save(account);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {

        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Account account = accountDAO.getAccountById(currentUser.getId());
        validateChangePassword(account,changePasswordRequest);
        log.info("Change New Password Successfully!");
    }


    @Override
    public void updateProfile(AccountRequest request, String id) {
        Account account = accountDAO.getAccountById(id);

        if (Objects.isNull(account)) {
            throw new RestApiException(StatusCode.ACCOUNT_NOT_EXIST);
        }

        String userAvatarPath = FileStore.getFilePath(request.getMultipartFile(), "-user");
        if (userAvatarPath != null) {
            request.setImage(userAvatarPath);
        }

        account.setPhone(request.getPhone());
        account.setName(request.getName());
        account.setDob(request.getDob());
        account.setGender(request.getGender());
        if (userAvatarPath != null) {
            if (account.getImage() != null) {
                String image = account.getImage();
                FileStore.deleteFile(image);
            }
            account.setImage(request.getImage());
        }
        account.setDob(request.getDob());
        accountDAO.save(account);
        log.info("Update Profile Successfully with id:{}",id);
    }

    @Override
    public void delete(String id) {
        if (!StringUtils.hasText(id)) {
            throw new RestApiException(StatusCode.DATA_EMPTY);
        }
        accountDAO.deleteAllById(id);
        log.info("Delete Account Successfully with id:{}",id);
    }


    @Override
    public ApiResponse searchByNameEmailRole(String name, String email, String role, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        List<String> roles = null;
        if (role.isEmpty()) {
            roles = Arrays.asList(RoleEnum.ROLE_ADMIN.name(), RoleEnum.ROLE_PARTNER.name(),
                    RoleEnum.ROLE_MEMBER.name(), RoleEnum.ROLE_EMPLOYEE.name(), RoleEnum.ROLE_CONTENT.name());
        } else {
            roles = Arrays.asList(role);
        }


        Long totalPage = accountDAO.countAccountByNameAndEmailAndRole(name, email, roles);
        Page<Account> accounts = accountDAO.searchAccountByNameEmailRole(name, email, roles, pageable);

        List<AccountResponse> accountResponses = new ArrayList<>();
        accounts.forEach(account -> accountResponses.add(convert(account)));

        return ApiResponse.builder()
                .data(accountResponses)
                .totalElement(totalPage)
                .build();
    }

    @Override
    public AccountResponse getById(String id) {
        if (!StringUtils.hasText(id)) {
            throw new RestApiException(StatusCode.DATA_EMPTY);
        }
        Account account = accountDAO.getAccountById(id);
        if (Objects.isNull(account)) {
            throw new RestApiException(StatusCode.ACCOUNT_NOT_EXIST);
        }

        AccountResponse response = convert(account);
        log.info("Get Account Successfully with id:{}",id);
        return response;
    }

    @Override
    public AccountResponse getByEmail(String email) {
        if(!StringUtils.hasText(email)) {
            throw new RestApiException(StatusCode.DATA_EMPTY);
        }
        Account account = accountDAO.getAccountByEmail(email);
        if (Objects.isNull(account)) {
            throw new RestApiException(StatusCode.ACCOUNT_NOT_EXIST);
        }
        log.info("Get Account Successfully with email:{}",email);
        return convert(account);

    }

    @Override
    public void changeAccountLock(String id) {
        if (Objects.isNull(id)) {
            throw new RestApiException(StatusCode.DATA_EMPTY);
        }
        Account account = accountDAO.getAccountById(id);
        if (Objects.isNull(account)) {
            throw new RestApiException(StatusCode.ACCOUNT_NOT_EXIST);
        }
        account.setEnabled(!account.getEnabled());
        accountDAO.save(account);
    }


    public AccountResponse convert(Account account) {
        return accountMapper.toResponse(account);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountDAO.getAccountByEmail(email.trim());
        if (account == null) {
            throw new UsernameNotFoundException("not found");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole().getName()));
        UserPrincipal accountDTO = new UserPrincipal(account.getEmail(), account.getPassword(), account.getEnabled(), true, true,
                true, authorities);
        accountDTO.setId(account.getId());
        accountDTO.setName(account.getName());
        accountDTO.setRoleId(account.getRole().getId());
        return accountDTO;
    }


    private void validateRegisterAccount(AccountRequest request) {

        Account searchAccountByEmail = accountDAO.getAccountByEmail(request.getEmail());
        if (Objects.nonNull(searchAccountByEmail)) {
            throw new RestApiException(StatusCode.ACCOUNT_REGISTER);
        }

        if (!ValidateUtil.isEmail(request.getEmail())) {
            throw new RestApiException(StatusCode.EMAIL_NOT_RIGHT_FORMAT);
        }
        if (!ValidateUtil.isPhoneNumber(request.getPhone())) {
            throw new RestApiException(StatusCode.PHONE_NUMBER_NOT_RIGHT_FORMAT);
        }
    }

    private void validateChangePassword(Account account,ChangePasswordRequest changePasswordRequest){
        if(Objects.isNull(account)){
            throw new RestApiException(StatusCode.ACCOUNT_NOT_EXIST.getStatus(),
                    StatusCode.ACCOUNT_NOT_EXIST.getMessage());
        }

        if (!ValidateUtil.isPassword(changePasswordRequest.getNewPassword())) {
            throw new RestApiException(StatusCode.NEW_PASSWORD_INVALID.getStatus(),
                    StatusCode.NEW_PASSWORD_INVALID.getMessage());
        }
        if (PasswordGenerator.checkHashStrings(changePasswordRequest.getPassword(), account.getPassword())) {
            if (PasswordGenerator.checkHashStrings(changePasswordRequest.getNewPassword(), account.getPassword())) {
                throw new RestApiException(StatusCode.NEW_PASSWORD_SAME_AS_OLD.getStatus(),
                        StatusCode.NEW_PASSWORD_SAME_AS_OLD.getMessage());
            }
            account.setPassword(PasswordGenerator.getHashString(changePasswordRequest.getNewPassword()));
            accountDAO.save(account);
        } else {
            throw new DataIntegrityViolationException(AccountConstants.PASSWORD_WRONG);
        }
    }
}
