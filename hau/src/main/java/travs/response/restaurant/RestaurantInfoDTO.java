package travs.response.restaurant;

import travs.common.RestaurantType;
import travs.common.type.ApproveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantInfoDTO {
    private String id;

    private String code;

    private String title;

    private String description;

    private String phonePartner;

    private String slug;

    private RestaurantType restaurantType;

    private Integer[] feature;

    private String address;

    private String userId;

    private ApproveStatus approveStatus;

    private List<String> imagesList;

    private List<RestaurantMenuDTO> menuDTOList;
}
