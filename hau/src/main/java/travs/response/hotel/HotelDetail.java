package travs.response.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelDetail {
    private String id;
    private String code;
    private String slug;
    private String title;
    private String description;
    private List<ImageDTO> images;
    private String address;
    private Double price;
    private boolean isFavorite;

    @JsonProperty("amenities")
    private List<FacilityDTO> facilityDTOList;
}
