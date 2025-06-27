package travs.request.hotel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
@Setter
public class HotelUploadBody {
    private String hotelCode;
    private Integer[] amenities;
    private String description;
    private Integer destination;
    private String type;
    private Integer rank;
    private Integer star;
    private String address;
    private String title;
    private List<String> images;

    @JsonIgnore
    List<MultipartFile> imageList;
}
