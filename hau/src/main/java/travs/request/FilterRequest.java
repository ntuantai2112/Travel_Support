package travs.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Log4j2
public class FilterRequest {

    private Long star;
    @JsonProperty("type")
    private String type;

    public boolean isNotProductTypeFilter() {
        return type == null;
    }

    public boolean isNotProductFilter() {
        return star == null && type == null;
    }
}
