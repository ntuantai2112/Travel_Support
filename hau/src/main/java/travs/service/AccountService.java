package travs.service;

import travs.request.account.AccountRequest;
import travs.request.account.ChangePasswordRequest;
import travs.response.account.AccountResponse;
import travs.response.ApiResponse;

public interface AccountService {


    void resgister(AccountRequest accountRequest);

    void add(AccountRequest accountRequest);

    void update(AccountRequest accountRequest , String id);

    void changePassword(ChangePasswordRequest changePasswordRequest );

    void updateProfile(AccountRequest accountRequest , String id);

    void delete(String id);

    AccountResponse getById(String id);

    AccountResponse getByEmail(String  email);

    ApiResponse searchByNameEmailRole(String name , String email, String role  , Integer page, Integer size);

    void changeAccountLock(String id);

}
