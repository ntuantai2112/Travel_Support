package travs.response.restaurant;

import com.fasterxml.jackson.annotation.JsonProperty;
import travs.response.hotel.FacilityDTO;
import travs.response.hotel.ImageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantDetail {
    private String id;
    private String code;
    private String slug;
    private String title;
    private String description;
    private List<ImageDTO> images;
    private String address;
    private boolean isFavorite;

    @JsonProperty("feature")
    private List<FacilityDTO> facilityDTOList;


    @JsonProperty("menus")
    private List<RestaurantMenuDTO> restaurantMenuDTO;
}
