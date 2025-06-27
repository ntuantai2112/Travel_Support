package travs.impl;


import travs.entity.account.Account;
import travs.model.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import travs.service.impl.AuditorAwareImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuditorAwareImplTest {


    @InjectMocks
    AuditorAwareImpl auditorAwareImpl;


    @Test
    public void testGetCurrentAuditor() {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
             Optional.of(Account.builder()
                    .id(currentUser.getId())
                    .build());
        }
         Optional.ofNullable(null);

        auditorAwareImpl.getCurrentAuditor();

        }





//    @Override
//    public Optional<Account> getCurrentAuditor() {
//        if (SecurityContextHolder.getContext().getAuthentication() != null
//                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
//            UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
//                    .getPrincipal();
//            return Optional.of(Account.builder()
//                    .id(currentUser.getId())
//                    .build());
//        }
//        return Optional.ofNullable(null);
//    }
}
