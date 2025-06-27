package travs.service;


import travs.common.FavoriteType;
import travs.exception.GeneralException;
import travs.model.FavoriteDTO;
import travs.response.BaseResponse;

public interface FavoriteService {

    BaseResponse addFavorite(FavoriteDTO favoriteDTO) throws GeneralException;

    BaseResponse getListFavoriteByUserId(Integer page, Integer perPage) throws GeneralException;

    BaseResponse removeFavorBySlug(String activitySlug, FavoriteType type) throws GeneralException;

}
