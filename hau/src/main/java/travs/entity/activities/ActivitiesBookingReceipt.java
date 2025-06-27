package travs.entity.activities;

import travs.common.type.BookingStatus;
import travs.entity.BaseTimestamp;
import travs.entity.BookingContact;
import travs.response.activities.ActivitiesInfoDTO;
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
@Table(name = "activities_receipt")
public class ActivitiesBookingReceipt extends BaseTimestamp {

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

    @Column(name = "travel_date")
    private Integer travelDate;

    @Column(name = "number_ticket_child")
    private Integer numberTicketChild;

    @Column(name = "number_ticket_adult")
    private Integer numberTicketAdult;

    @Column(name = "phone_partner")
    private String phonePartner;

    @Column(name = "price")
    private Double price;

    @Type(type = "jsonb")
    @Column(name = "activities_info", columnDefinition = "jsonb")
    private ActivitiesInfoDTO activitiesInfoDTO;

    @Column(name = "partner_id")
    private String partnerId;

    @Column(name = "user_id")
    private String userId;

}
