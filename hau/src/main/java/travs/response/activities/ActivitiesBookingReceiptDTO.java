package travs.response.activities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.SuperBuilder;
import travs.common.type.BookingStatus;
import lombok.*;
import travs.response.BaseBookingReceiptResponse;


@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivitiesBookingReceiptDTO extends BaseBookingReceiptResponse {


    private Integer numberTicketChild;

    private Integer numberTicketAdult;

    private BookingContactDTO contact;

    private BookingStatus status;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Integer travelDate;

    private Double price;

    private ActivitiesInfoDTO activitiesInfoDTO;

    private String partnerId;
}
