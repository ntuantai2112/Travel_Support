package travs.service.impl.restaurant;

import travs.common.type.BookingStatus;
import travs.request.restaurant.BookingRestaurantRequest;
import travs.request.restaurant.RestaurantApproveBookingRequest;
import travs.response.BaseResponse;
import travs.response.restaurant.RestaurantBookingReceiptDTO;

import java.util.List;
import java.util.Map;

public interface ReceiptRestaurantService {

    BaseResponse bookingRestaurant(BookingRestaurantRequest bookingRestaurantRequest);

    void restaurantApproStatusRepository(RestaurantApproveBookingRequest restaurantApproveBookingRequest);

    void restaurantApproStatusRepositoryUserId(RestaurantApproveBookingRequest restaurantApproveBookingRequest);

    BaseResponse getListRestaurantReceipt(BookingStatus status, Integer page, Integer perPage);

    BaseResponse<List<RestaurantBookingReceiptDTO>, Map<String, Integer>> getListRestaurantReceiptByUserId(BookingStatus status, Integer page, Integer perPage);


    BaseResponse getReceiptRestaurantDetail(String id);
}
