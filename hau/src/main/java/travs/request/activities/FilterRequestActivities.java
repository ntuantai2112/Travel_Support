package travs.request.activities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Log4j2
@Builder
public class FilterRequestActivities {

    private Integer durationFrom;
    private Integer durationTo;
    @JsonProperty("type")
    private String type;

    public boolean isNotProductFilter() {

        return durationFrom == null && durationTo == null && type == null;
    }
}
