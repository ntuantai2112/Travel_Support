package travs.request.activities;


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
public class ActivitiesUploadBody {
    private String description;
    private String type;
    private Integer star;
    private String address;
    private String title;
    private List<String> images;
    private Double childTicketPrice;
    private Double adultTicketPrice;
    private String activitiesType;
    private Integer duration;
    private String activitiesCode;

    @JsonIgnore
    List<MultipartFile> imageList;
}
