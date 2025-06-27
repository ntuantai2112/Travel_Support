package travs.entity.restaurant;

import travs.common.RestaurantType;
import travs.common.type.ApproveStatus;
import travs.entity.BaseTimestamp;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Builder
@Getter
@Setter
@TypeDefs({
        @TypeDef(name = "string-array", typeClass = StringArrayType.class),
        @TypeDef(name = "int-array", typeClass = IntArrayType.class),
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurant")
public class Restaurant extends BaseTimestamp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "code")
    private String code;

    @Column(name = "title", length = 1000)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "slug", length = 1000)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(name = "restaurant_type")
    private RestaurantType restaurantType;

    // tiện ích của nhà hàng
    @Type(type = "int-array")
    @Column(name = "feature", columnDefinition = "integer[]")
    private Integer[] feature;

    @Column(name = "address")
    private String address;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "approve_status")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatus;

}
