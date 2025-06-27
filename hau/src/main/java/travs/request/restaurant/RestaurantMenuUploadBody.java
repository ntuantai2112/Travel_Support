package travs.request.restaurant;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
@Setter
public class RestaurantMenuUploadBody {
    private Long menuId;
    private String restaurantCode;
    @Builder.Default
    private String currency = "VNĐ";
    private Double price;
    private String image;
    private String name;
    private String description;

    @JsonIgnore
    MultipartFile multipartFile;

}
