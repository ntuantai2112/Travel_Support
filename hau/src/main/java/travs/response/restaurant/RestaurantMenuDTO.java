package travs.response.restaurant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class RestaurantMenuDTO {
    private long id;
    private Double price;
    private String currency;
    private String image;
    private String description ;
    private String name ;
}
