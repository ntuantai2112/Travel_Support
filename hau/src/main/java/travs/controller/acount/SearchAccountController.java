package travs.controller.acount;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import travs.request.account.AccountRequest;
import travs.response.ApiResponse;
import travs.response.account.AccountResponse;
import travs.service.AccountService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Log4j2
public class SearchAccountController {

    private AccountService accountService;


    // api trả về list account
    @GetMapping("/admin/account/search")
    public ResponseEntity<?> searchAccountByNameEmailRoles(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "email", required = false, defaultValue = "") String email,
            @RequestParam(name = "role", required = false, defaultValue = "") String role,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize
    ) {
        log.info(" search account  request" + name + " - " + email + " - " + role);
        ApiResponse apiResponse = accountService.searchByNameEmailRole(name, email, role, pageNo, pageSize);
        ResponseEntity<ApiResponse> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("searchEmployee response : " + new Gson().toJson(response));
        return response;
    }

    // api add account
    @PostMapping("/admin/account/add")
    public ResponseEntity<?> addAccount(@RequestBody @Valid AccountRequest accountRequest) {
        log.debug("add account request : " + new Gson().toJson(accountRequest));
        accountService.add(accountRequest);
        ResponseEntity<String> response = new ResponseEntity<>("Success", HttpStatus.CREATED);
        log.debug("addAccount response" + new Gson().toJson(accountRequest));
        return response;
    }

    // api update account by id
    @PutMapping("/admin/account/update" + "/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable("id") String accountId, @RequestBody AccountRequest accountRequest) {
        log.debug("addEmployee: request " + new Gson().toJson(accountRequest));
        accountService.update(accountRequest, accountId);
        ResponseEntity<String> response = new ResponseEntity<>("update Success", HttpStatus.OK);
        log.debug("addEmployee response: " + new Gson().toJson(response));
        return response;
    }

    // api lấy  account by id
    @GetMapping("/admin/account/get" + "/{id}")
    public ResponseEntity<?> getAccount(@PathVariable("id") String accountId) {
        log.debug("get account:  " + new Gson().toJson(accountId));
        AccountResponse accountResponse = accountService.getById(accountId);
        ResponseEntity<AccountResponse> response = new ResponseEntity<AccountResponse>(accountResponse, HttpStatus.OK);
        log.debug("get account response: " + new Gson().toJson(accountResponse));
        return response;
    }

    // api delet account by id
    @DeleteMapping("/admin/adcount/delete" + "/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable("id") String accountId) {
        log.debug("delete account:  " + new Gson().toJson(accountId));
        accountService.delete(accountId);
        ResponseEntity<String> response = new ResponseEntity<>("delete account success", HttpStatus.OK);
        log.debug("delete account response: " + new Gson().toJson(response));
        return response;
    }

    // api khóa account by id
    @PostMapping("/admin/account/changeAccountLock" + "/{id}")
    public ResponseEntity<?> lockAccount(@PathVariable("id") String accountId) {
        log.debug("lock account request : " + new Gson().toJson(accountId));
        accountService.changeAccountLock(accountId);
        ResponseEntity<String> response = new ResponseEntity<>(" lock account Success", HttpStatus.CREATED);
        log.debug("lock Account response" + new Gson().toJson(accountId));
        return response;
    }

}
