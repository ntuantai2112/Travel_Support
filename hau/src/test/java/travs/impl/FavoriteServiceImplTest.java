package travs.impl;


import travs.common.FavoriteType;
import travs.entity.Favorite;
import travs.exception.GeneralException;
import travs.model.FavoriteDTO;
import travs.repository.FavoriteRepository;
import travs.service.impl.FavoriteServiceImpl;
import travs.utils.AuthenticationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceImplTest {

    @InjectMocks
    FavoriteServiceImpl favoriteServiceImpl;

    @Mock
    FavoriteRepository favoriteRepository;


    @Test
    public void testAddFavorite() throws GeneralException {

        String userId = "2";
        //Mockito.mockstatic

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

        List<Favorite> listUserFavor = Arrays.asList(favorite);

        Mockito.when(favoriteRepository.findAllByUserId(Mockito.anyString())).thenReturn(listUserFavor);

        Mockito.when(favoriteRepository.save(favorite)).thenReturn(favorite);

        favoriteServiceImpl.addFavorite(favoriteDTO);


    }


    @Test
    public void removeFavorBySlug() {

        String userId = "1";
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
                .slug(favoriteDTO.getSlug())
                .title(favoriteDTO.getTitle())
                .type(favoriteDTO.getType())
                .build();

        Mockito.when(favoriteRepository.findByUserIdAndSlug(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.ofNullable(favorite));

        Mockito.doNothing().when(favoriteRepository).delete(favorite);

    }



}
