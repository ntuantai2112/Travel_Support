package travs.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import travs.entity.hotel.Hotel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class SearchFilterMeta {
    private List<String> hotelCodes;
    private List<Hotel> filteredHotel;
}
