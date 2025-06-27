package travs.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import travs.entity.account.Account;
import travs.model.UserPrincipal;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class AuditorAwareImplTest {

    private AuditorAwareImpl auditorAwareImplUnderTest;

    @BeforeEach
    void setUp() {
        auditorAwareImplUnderTest = new AuditorAwareImpl();
    }

    @Test
    void testGetCurrentAuditor() {
        // Setup
        // Run the test
        final Optional<Account> result = auditorAwareImplUnderTest.getCurrentAuditor();

        // Verify the results
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void testGetCurrentAuditor_Success_Test() {
        // Setup
        // Run the test
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            UserPrincipal userPrincipal = mock(UserPrincipal.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userPrincipal);
            when(userPrincipal.getId()).thenReturn("1L");
            final Optional<Account> result = auditorAwareImplUnderTest.getCurrentAuditor();
            // Verify the results
            Assertions.assertThat(result).isNotNull();
        }
    }
}
