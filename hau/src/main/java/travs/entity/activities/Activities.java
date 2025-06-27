package travs.entity.activities;

import travs.common.ActivitiesType;
import travs.common.type.ApproveStatus;
import travs.entity.BaseTimestamp;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Builder
@Getter
@Setter
@TypeDefs({
        @TypeDef(name = "string-array", typeClass = StringArrayType.class),
        @TypeDef(name = "int-array", typeClass = IntArrayType.class),
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activities")
public class Activities extends BaseTimestamp {

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
    @Column(name = "type")
    private ActivitiesType activitiesType;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "star")
    private Integer star;

    @Column(name = "child_ticket_price")
    private Double childTicketPrice;

    @Column(name = "adult_ticket_price")
    private Double adultTicketPrice;

    @Column(name = "address")
    private String address;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "approve_status")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatus;

}
