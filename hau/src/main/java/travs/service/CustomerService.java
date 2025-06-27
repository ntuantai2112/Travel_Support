package travs.service;

import travs.response.activities.ActivitiesDTO;
import travs.response.hotel.HotelDTO;
import travs.response.restaurant.RestaurantDTO;

import java.util.List;

public interface CustomerService {

    List<RestaurantDTO> getListRestaurant(String title);

    List<HotelDTO> getListHotel(String title);

    List<ActivitiesDTO>getListActivities(String title);


}
