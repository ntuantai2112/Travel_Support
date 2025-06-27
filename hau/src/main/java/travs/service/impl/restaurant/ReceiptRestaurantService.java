package travs.service.impl.restaurant;

import travs.common.type.BookingStatus;
import travs.request.restaurant.BookingRestaurantRequest;
import travs.request.restaurant.RestaurantApproveBookingRequest;
import travs.response.BaseResponse;

public interface ReceiptRestaurantService {

    BaseResponse bookingRestaurant(BookingRestaurantRequest bookingRestaurantRequest);

    void restaurantApproStatusRepository(RestaurantApproveBookingRequest restaurantApproveBookingRequest);

    void restaurantApproStatusRepositoryUserId(RestaurantApproveBookingRequest restaurantApproveBookingRequest);

    BaseResponse getListRestaurantReceipt(BookingStatus status, Integer page, Integer perPage);

    BaseResponse getListRestaurantReceiptByUserId(BookingStatus status, Integer page, Integer perPage);


    BaseResponse getReceiptRestaurantDetail(String id);
}
