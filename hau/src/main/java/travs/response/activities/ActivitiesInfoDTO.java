package travs.response.activities;

import travs.common.ActivitiesType;
import travs.common.type.ApproveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivitiesInfoDTO {
    private String id;

    private String code;

    private String title;

    private String description;

    private String slug;

    private ActivitiesType activitiesType;

    private Double star;

    private Double childTicketPrice;

    private Double adultTicketPrice;

    private String phonePartner;

    private String address;

    private String userId;

    private ApproveStatus approveStatus;

    private List<String> imagesList;

    private List<ActivitiesGameDTO> gameDTOList;
}
