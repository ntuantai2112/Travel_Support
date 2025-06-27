package travs.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.common.FavoriteType;
import travs.entity.Favorite;
import travs.exception.GeneralException;
import travs.model.FavoriteDTO;
import travs.repository.FavoriteRepository;
import travs.response.BaseResponse;
import travs.utils.AuthenticationUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository mockFavoriteRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteServiceImplUnderTest;

    @Test
    void testAddFavorite_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            // Setup
            final FavoriteDTO favoriteDTO = new FavoriteDTO("slug", "imageUrl", "title", FavoriteType.HOTEL);
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            // Configure FavoriteRepository.findAllByUserId(...).
            final List<Favorite> favorites = List.of();
            when(mockFavoriteRepository.findAllByUserId("userId")).thenReturn(favorites);

            // Configure FavoriteRepository.save(...).
            final Favorite favorite = new Favorite("id", "userId", FavoriteType.HOTEL, "slug", "imageUrl", "title");
            when(mockFavoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

            // Run the test
            final BaseResponse result = favoriteServiceImplUnderTest.addFavorite(favoriteDTO);

            // Verify the results
            verify(mockFavoriteRepository).save(any(Favorite.class));
            assertThat(result).isNotNull();
        }
    }

    @Test
    void testAddFavorite_Fail_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            // Setup
            final FavoriteDTO favoriteDTO = new FavoriteDTO("slug", "imageUrl", "title", FavoriteType.HOTEL);
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            // Configure FavoriteRepository.findAllByUserId(...).
            final List<Favorite> favorites = List.of(
                    new Favorite("id", "userId", FavoriteType.HOTEL, "slug", "imageUrl", "title"));
            when(mockFavoriteRepository.findAllByUserId("userId")).thenReturn(favorites);

            // Configure FavoriteRepository.save(...).
            final Favorite favorite = new Favorite("id", "userId", FavoriteType.HOTEL, "slug", "imageUrl", "title");

            // Run the test
            Assertions.assertThrows(GeneralException.class, () -> favoriteServiceImplUnderTest.addFavorite(favoriteDTO));
        }
    }

    @Test
    void testAddFavorite_UserId_Fail_Test() {
        final FavoriteDTO favoriteDTO = new FavoriteDTO("slug", "imageUrl", "title", FavoriteType.HOTEL);
        Assertions.assertThrows(GeneralException.class, () -> favoriteServiceImplUnderTest.addFavorite(favoriteDTO));
    }

    @Test
    void testAddFavorite_SlugNull_Fail_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            // Setup
            final FavoriteDTO favoriteDTO = new FavoriteDTO(null, "imageUrl", "title", FavoriteType.HOTEL);
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            // Run the test
            Assertions.assertThrows(GeneralException.class, () -> {
                favoriteServiceImplUnderTest.addFavorite(favoriteDTO);
            });
        }
    }

    @Test
    void testGetListFavoriteByUserId_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            // Setup
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            // Configure FavoriteRepository.findAllByUserId(...).
            final Optional<List<Favorite>> favorites = Optional.of(
                    List.of(new Favorite("id", "userId", FavoriteType.HOTEL, "slug", "imageUrl", "title")));
            when(mockFavoriteRepository.findByUserIdOrderByCreatedAtDesc("userId")).thenReturn(favorites);

            // Run the test
            final BaseResponse result = favoriteServiceImplUnderTest.getListFavoriteByUserId(2, 55);
            assertThat(result).isNotNull();
        }
    }
    @Test
    void testGetListFavoriteByUserId_Fail_Test() {
        BaseResponse baseResponse = favoriteServiceImplUnderTest.getListFavoriteByUserId(2, 55);
        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.isSuccess()).isTrue();
    }

    @Test
    void testRemoveFavorBySlug_Success_Test() {
        // Setup
        // Configure FavoriteRepository.findByUserIdAndSlug(...).
        final Optional<Favorite> favorite = Optional.of(
                new Favorite("id", "userId", FavoriteType.HOTEL, "slug", "imageUrl", "title"));
        when(mockFavoriteRepository.findByUserIdAndSlug(any(), any())).thenReturn(favorite);

        // Run the test
        final BaseResponse result = favoriteServiceImplUnderTest.removeFavorBySlug("slug", FavoriteType.HOTEL);

        // Verify the results
        verify(mockFavoriteRepository).delete(any(Favorite.class));
        assertThat(result).isNotNull();
    }
}
