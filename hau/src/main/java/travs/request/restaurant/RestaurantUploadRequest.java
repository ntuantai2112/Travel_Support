package travs.request.restaurant;

import travs.common.RestaurantType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RestaurantUploadRequest {

    private Integer[] feature;
    private String description;
    private RestaurantType restaurantType;
    private String address;
    private String title;
    private List<String> images;
    private String restaurantCode;

    @JsonIgnore
    List<MultipartFile> imageList;


}
