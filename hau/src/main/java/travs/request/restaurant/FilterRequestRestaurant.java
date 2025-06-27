package travs.request.restaurant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Log4j2
public class FilterRequestRestaurant {


    @JsonProperty("type")
    private String type;

    public boolean isNotProductFilter() {
        return type == null;
    }
}
