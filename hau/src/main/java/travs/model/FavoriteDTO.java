package travs.model;

import travs.common.FavoriteType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FavoriteDTO {

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("image")
    private String imageUrl;

    @JsonProperty("title")
    private String title;

    @JsonProperty("type")
    private FavoriteType type;

}
