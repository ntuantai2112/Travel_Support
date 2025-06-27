package travs.service.impl;

import travs.common.FavoriteType;
import travs.model.FavoriteDTO;
import travs.utils.AuthenticationUtils;
import travs.utils.MappingUtils;
import travs.entity.Favorite;
import travs.exception.ErrorCode;
import travs.exception.GeneralException;
import travs.repository.FavoriteRepository;
import travs.response.BaseResponse;
import travs.service.FavoriteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Override
    public BaseResponse addFavorite(FavoriteDTO favoriteDTO) throws GeneralException {
        if (favoriteDTO.getSlug() == null && favoriteDTO.getType() != null) {
            throw new GeneralException(ErrorCode.MISSING_PARAMETER);
        }
        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }
        Favorite favorite = Favorite.builder()
                .userId(userId)
                .imageUrl(favoriteDTO.getImageUrl())
                .slug(favoriteDTO.getSlug())
                .title(favoriteDTO.getTitle())
                .type(favoriteDTO.getType())
                .build();

        List<Favorite> listUserFavor = this.favoriteRepository.findAllByUserId(userId);

        if (!listUserFavor.isEmpty() && listUserFavor.stream().map(Favorite::getSlug).collect(Collectors.toList()).contains(favoriteDTO.getSlug())) {
            throw new GeneralException(ErrorCode.FAVORITE_ALREADY_EXIST);
        }
        this.favoriteRepository.save(favorite);
        return BaseResponse.ok(true);
    }

    @Override
    public BaseResponse getListFavoriteByUserId(Integer page, Integer perPage) throws GeneralException {
        String userId = AuthenticationUtils.getUserId();
        Map<String, Object> metaData = new HashMap<>();
        metaData.put("total", 0);
        if (userId == null) {
            return BaseResponse.ok(new ArrayList<>(), metaData);
        }
        List<Favorite> listUserFavorActivities = this.favoriteRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .orElse(new ArrayList<>());
        List<Favorite> hotelFavorList = listUserFavorActivities.stream().filter(favorite -> FavoriteType.HOTEL.equals(favorite.type)).collect(Collectors.toList());
        List<Favorite> restaurantFavorList = listUserFavorActivities.stream().filter(favorite -> FavoriteType.RESTAURANT.equals(favorite.type)).collect(Collectors.toList());
        List<Favorite> activitiesFavorList = listUserFavorActivities.stream().filter(favorite -> FavoriteType.ACTIVITIES.equals(favorite.type)).collect(Collectors.toList());

        Map<String, List<FavoriteDTO>> resultData = new HashMap<>();


        resultData.put("hotel", MappingUtils.map(hotelFavorList, FavoriteDTO.class));
        resultData.put("restaurant", MappingUtils.map(restaurantFavorList, FavoriteDTO.class));
        resultData.put("activities", MappingUtils.map(activitiesFavorList, FavoriteDTO.class));

        return BaseResponse.ok(resultData);
    }

    @Override
    public BaseResponse removeFavorBySlug(String slug, FavoriteType type) throws GeneralException {
        String userId = AuthenticationUtils.getUserId();
        Favorite favorite = this.favoriteRepository.findByUserIdAndSlug(userId, slug)

                .orElseThrow(() -> new GeneralException(ErrorCode.RESOURCE_NOT_FOUND));
        this.favoriteRepository.delete(favorite);
        return BaseResponse.ok(true);
    }

}
