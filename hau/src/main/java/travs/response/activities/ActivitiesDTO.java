package travs.response.activities;

import travs.common.ActivitiesType;
import travs.common.type.ApproveStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivitiesDTO {
    private String code;
    private String title;
    private String description;
    private String slug;
    @JsonProperty("type")
    private ActivitiesType activitiesType;
    private Double star;
    @JsonIgnore
    private List<Integer> games;
    @JsonProperty("games")
    private List<ActivitiesGameDTO> activitiesGameDTOS;
    private Double childTicketPrice;
    private Double adultTicketPrice;
    private Integer duration;
    private String address;
    private String image;
    private boolean isFavor;
    private ApproveStatus approveStatus;
}
