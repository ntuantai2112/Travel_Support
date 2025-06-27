package travs.request.restaurant;


import travs.common.type.ApproveStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RestaurantApproveRequest {
    private String restaurantCode;
    private ApproveStatus status;
}
