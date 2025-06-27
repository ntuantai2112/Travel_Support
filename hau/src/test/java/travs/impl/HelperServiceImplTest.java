package travs.impl;

import travs.common.FavoriteType;
import travs.entity.Favorite;
import travs.model.FavoriteDTO;
import travs.repository.FavoriteRepository;
import travs.service.impl.HelperService;
import travs.utils.AuthenticationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class HelperServiceImplTest {

    @Mock
    FavoriteRepository favoriteRepository;

    @InjectMocks
    HelperService helperService;

    @Test
    public void checkFavorite() {

        String userId = "2";
        //PowerMockito.mockStatic(AuthenticationUtils.class);
        Mockito.when(AuthenticationUtils.getUserId()).thenReturn(userId);

        FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                .imageUrl("abc.jpg")
                .slug("slug-abc")
                .type(FavoriteType.valueOf("HOTEL"))
                .title("oki")
                .build();

        Favorite favorite = Favorite.builder()
                .userId(userId)
                .imageUrl(favoriteDTO.getImageUrl())
                .slug("ssss")
                .title(favoriteDTO.getTitle())
                .type(favoriteDTO.getType())
                .build();

         Mockito.when(favoriteRepository.findByUserIdAndSlug(userId , "aaaaa")).thenReturn(Optional.ofNullable(favorite));

        helperService.checkFavorite("aaaaa");
    }


}
