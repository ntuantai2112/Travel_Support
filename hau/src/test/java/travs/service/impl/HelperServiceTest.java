package travs.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.common.FavoriteType;
import travs.entity.Favorite;
import travs.repository.FavoriteRepository;
import travs.utils.AuthenticationUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HelperServiceTest {

    @Mock
    private FavoriteRepository mockFavoriteRepository;

    @InjectMocks
    private HelperService helperServiceUnderTest;

    @Test
    void testCheckFavorite_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            // Setup
            final String slug = "slug";
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            // Configure FavoriteRepository.findByUserIdAndSlug(...).
            final Optional<Favorite> favorite = Optional.of(new Favorite("id", "userId", FavoriteType.HOTEL, "slug", "imageUrl", "title"));
            when(mockFavoriteRepository.findByUserIdAndSlug("userId", "slug")).thenReturn(favorite);

            // Run the test
            final boolean result = helperServiceUnderTest.checkFavorite(slug);

            // Verify the results
            assertThat(result).isTrue();
        }
    }

    @Test
    void testCheckFavorite_FavoriteRepositoryReturnsAbsent() {
        // Run the test
        final boolean result = helperServiceUnderTest.checkFavorite("slug");
        // Verify the results
        assertThat(result).isFalse();
    }
}
