package travs.response.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import travs.common.type.BookingStatus;
import travs.response.BaseBookingReceiptResponse;


@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelBookingReceiptDTO extends BaseBookingReceiptResponse {

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
