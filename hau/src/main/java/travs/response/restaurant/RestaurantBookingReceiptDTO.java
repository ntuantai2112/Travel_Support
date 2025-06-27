package travs.response.restaurant;

import travs.common.type.BookingStatus;
import travs.response.hotel.BookingContactDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantBookingReceiptDTO {

    private String id;

    private String createdAt;

    private String updatedAt;

    private String bookingId;

    private BookingContactDTO contact;

    private BookingStatus status;

    private Integer checkinDay;

    private String checkinTime;

    private RestaurantInfoDTO restaurantInfoDTO;

    private String partnerId;
}
