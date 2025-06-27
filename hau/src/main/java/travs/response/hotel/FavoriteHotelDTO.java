package travs.response.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FavoriteHotelDTO {

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image")
    private String imageUrl;

    @JsonProperty("location")
    private String location;

    @JsonProperty("title")
    private String title;

}
