package travs.response.hotel;

import travs.common.type.BookingStatus;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelBookingReceiptDTO {

    private String id;

    private String createdAt;

    private String updatedAt;

    private String bookingId;

    private Integer totalNights;

    private BookingContactDTO contact;

    private BookingStatus status;

    private Integer checkin;

    private Integer checkout;

    private Double price;

    private HotelInfoDTO hotelInfoDTO;

    private PackageDTO packageInfo;

    private String partnerId;
}
