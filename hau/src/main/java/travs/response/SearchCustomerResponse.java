package travs.response;

import travs.response.activities.ActivitiesDTO;
import travs.response.hotel.HotelDTO;
import travs.response.restaurant.RestaurantDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class SearchCustomerResponse {
    private List<HotelDTO>  hotels;
    private List<RestaurantDTO>  restaurants;
    private List<ActivitiesDTO> activities;
}
