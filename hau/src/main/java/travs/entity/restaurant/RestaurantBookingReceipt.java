package travs.entity.restaurant;

import travs.common.type.BookingStatus;
import travs.entity.BaseTimestamp;
import travs.entity.BookingContact;
import travs.response.restaurant.RestaurantInfoDTO;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Builder
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurant_receipt")
public class RestaurantBookingReceipt extends BaseTimestamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "booking_id")
    private String bookingId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private BookingContact contact;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "checkin_day")
    private Integer checkinDay;

    @Column(name = "checkin_time")
    private String checkinTime;

    @Column(name = "number_child")
    private Integer numberChild;

    @Column(name = "number_adult")
    private Integer numberAdult;

    @Type(type = "jsonb")
    @Column(name = "restaurant_info", columnDefinition = "jsonb")
    private RestaurantInfoDTO restaurantInfoDTO;

    @Column(name = "phone_partner")
    private String phonePartner;

    @Column(name = "partner_id")
    private String partnerId;

    @Column(name = "user_id")
    private String userId;

}
