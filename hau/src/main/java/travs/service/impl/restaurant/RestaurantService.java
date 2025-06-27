package travs.service.impl.restaurant;

import travs.request.restaurant.RestaurantUploadRequest;
import travs.response.BaseResponse;
import travs.request.restaurant.RestaurantMenuUploadBody;

public interface RestaurantService {
    BaseResponse get(String slug);

    BaseResponse uploadRestaurant(RestaurantUploadRequest restaurantUploadRequest) ;

    BaseResponse updateRestaurant(RestaurantUploadRequest restaurantUploadRequest);

    void uploadRestaurantMenu(RestaurantMenuUploadBody restaurantMenuUploadBody);

    void updateRestaurantMenu(RestaurantMenuUploadBody restaurantMenuUploadBody);

    void deleteRestaurant(String restaurantCode);

    void deleteRestaurantMenu(Long menuId);

    BaseResponse getListRestaurant(Integer page, Integer perPage);

//    BaseResponse getListRestaurantMenu(String RestaurantCode, Integer page, Integer perPage);

}
