package travs.service;

import travs.common.type.ApproveStatus;
import travs.response.BaseResponse;
import travs.request.activities.ActivitiesApproveRequest;
import travs.request.hotel.HotelApproveRequest;
import travs.request.restaurant.RestaurantApproveRequest;

public interface EmployeeService {

    void approveHotel(HotelApproveRequest hotelApproveRequest);

    BaseResponse getListHotel(String hotelName, ApproveStatus status, Integer page, Integer perPage);

    void approveActivities(ActivitiesApproveRequest activitiesApproveRequest);

    BaseResponse getListActivities(String activitiesName, ApproveStatus status, Integer page, Integer perPage);


    void approveRestaurant(RestaurantApproveRequest restaurantApproveRequest);

    BaseResponse getListRestaurant(String activitiesName, ApproveStatus status, Integer page, Integer perPage);
}
