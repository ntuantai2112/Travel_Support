package travs.response.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import travs.common.type.BookingStatus;
import travs.response.BaseBookingReceiptResponse;
import travs.response.hotel.BookingContactDTO;


@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantBookingReceiptDTO extends BaseBookingReceiptResponse {

    private BookingContactDTO contact;

    private BookingStatus status;

    private Integer checkinDay;

    private String checkinTime;

    private RestaurantInfoDTO restaurantInfoDTO;

    private String partnerId;
}
