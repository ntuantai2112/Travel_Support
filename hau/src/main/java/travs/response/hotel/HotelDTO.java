package travs.response.hotel;

import travs.common.HotelType;
import travs.common.type.ApproveStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class HotelDTO {
    private String code;
    private String title;
    private String description;
    private String slug;
    @JsonProperty("type")
    private HotelType hotelType;
    private Double star;
    @JsonIgnore
    private List<Integer> amenities;
    private Integer rank;

    @JsonProperty("amenities")
    private List<FacilityDTO> facilityDTOList;
    private Double minPrice;
    private String address;
    private String image;
    private boolean isFavor;
    private ApproveStatus approveStatus;
}
