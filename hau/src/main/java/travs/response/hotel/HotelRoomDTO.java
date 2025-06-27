package travs.response.hotel;

import travs.common.type.RoomType;
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
public class HotelRoomDTO {
    private long id;
    private String hotelCode;
    private String hotelOptionCode;
    private Double price;
    private String currency;
    private Long startDate;
    private Long endDate;
    private Boolean available;
    private RoomType roomType;
    private String image;
    @JsonIgnore
    private List<Integer> amenities;
    @JsonProperty("amenities")
    private List<FacilityDTO> facilityDTOList;
}
