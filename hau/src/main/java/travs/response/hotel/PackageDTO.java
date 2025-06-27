package travs.response.hotel;

import travs.common.type.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageDTO {
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
}
