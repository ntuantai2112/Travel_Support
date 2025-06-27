package travs.request.hotel;


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
public class HotelRoomUploadBody {
    private Long roomId;
    private Integer[] amenities;
    private String hotelCode;
    @Builder.Default
    private String currency = "VNĐ";
    private Double price;
    private String hotelOptionCode;
    private String type;
    private String imageRoom;

    @JsonIgnore
    MultipartFile image;

}
