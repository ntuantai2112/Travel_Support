package travs.entity.hotel;

import travs.common.type.RoomType;
import travs.entity.BaseTimestamp;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "hotel_bookable_item",
        indexes = {@Index(name = "p_b_i_hotel_code_idx", columnList = "hotel_code")})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelBookableItem extends BaseTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "hotel_code")
    private String hotelCode;
    @Column(name = "hotel_option_code")
    private String hotelOptionCode;
    @Column(name = "price")
    private Double price;
    @Column(name = "currency")
    private String currency;
    @Column(name = "available")
    private Boolean available;
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;
    @Column(name = "image")
    private String image;
    @Type(type = "int-array")
    @Column(name = "amenities", columnDefinition = "integer[]")
    private Integer[] amenities;
}
