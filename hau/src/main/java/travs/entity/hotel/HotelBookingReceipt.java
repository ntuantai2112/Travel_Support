package travs.entity.hotel;

import travs.common.type.BookingStatus;
import travs.entity.BaseTimestamp;
import travs.entity.BookingContact;
import travs.response.hotel.HotelInfoDTO;
import travs.response.hotel.PackageDTO;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;


@Builder
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hotel_receipt")
public class HotelBookingReceipt extends BaseTimestamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "booking_id")
    private String bookingId;

    @Column(name = "total_nights")
    private Integer totalNights;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private BookingContact contact;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "from_date")
    private Integer checkin;

    @Column(name = "to_date")
    private Integer checkout;

    @Column(name = "price")
    private Double price;

    @Type(type = "jsonb")
    @Column(name = "hotel_info", columnDefinition = "jsonb")
    private HotelInfoDTO hotelInfoDTO;

    @Column(name = "phone_partner")
    private String phonePartner;

    @Type(type = "jsonb")
    @Column(name = "package_info", columnDefinition = "jsonb")
    private PackageDTO packageInfo;

    @Column(name = "partner_id")
    private String partnerId;

    @Column(name = "user_id")
    private String userId;

}
