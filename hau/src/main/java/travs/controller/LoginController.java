package travs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import travs.constant.Constants;
import travs.constant.StatusCode;
import travs.model.UserPrincipal;
import travs.request.account.LoginRequest;
import travs.response.ApiResponse;
import travs.response.account.AccountResponse;
import travs.response.token.TokenResponse;
import travs.sercurity.JwtTokenProvider;
import travs.service.AccountService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = -1)
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final AccountService accountService;

    // api login
    @PostMapping(Constants.UrlPath.URL_API_LOGIN)
    public TokenResponse login(@RequestBody @Valid LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        return jwtTokenProvider.createToken(request.getUsername());
    }

    // api get profile
    @GetMapping(Constants.UrlPath.URL_API_PROFILE)
    public ApiResponse<AccountResponse> getAccount() {
        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        AccountResponse accountResponse = accountService.getById(currentUser.getId());
        return ApiResponse.build(StatusCode.SUCCESS.getStatus(),
                StatusCode.SUCCESS.getMessage(),
                accountResponse);
    }


}
