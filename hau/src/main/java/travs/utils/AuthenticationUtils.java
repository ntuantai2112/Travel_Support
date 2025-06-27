package travs.utils;

import travs.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuthenticationUtils {

    private AuthenticationUtils() {
    }

    public static Optional<UserPrincipal> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            return Optional.empty();
        }
        return Optional.of((UserPrincipal) authentication.getPrincipal());
    }

    public static String getUserId() {
        Optional<UserPrincipal> accountOptional = getAuthenticatedUser();
        return accountOptional.map(UserPrincipal::getId).orElse(null);
    }

    public static UserPrincipal getUserInfo() {
        return getAuthenticatedUser().orElse(null);
    }

    public static boolean isAuthenticated() {
        return getAuthenticatedUser().isPresent();
    }

}
