package travs.request.activities;


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
@Builder
public class ActivitiesGameUploadBody {
    private Integer gameId;
    private String activitiesCode;
    private String description;
    private String name;

    @JsonIgnore
    MultipartFile image;

}
