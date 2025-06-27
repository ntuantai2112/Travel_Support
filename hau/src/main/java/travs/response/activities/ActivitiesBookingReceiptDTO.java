package travs.response.activities;

import travs.common.type.BookingStatus;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivitiesBookingReceiptDTO {

    private String id;

    private String createdAt;

    private String updatedAt;

    private String bookingId;

    private Integer numberTicketChild;

    private Integer numberTicketAdult;

    private BookingContactDTO contact;

    private BookingStatus status;

    private Integer travelDate;

    private Double price;

    private ActivitiesInfoDTO activitiesInfoDTO;

    private String partnerId;
}
