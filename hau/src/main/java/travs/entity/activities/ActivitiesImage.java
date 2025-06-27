package travs.entity.activities;

import travs.entity.BaseTimestamp;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "activities_image",
        indexes = {@Index(name = "activities_image_hotel_code_idx", columnList = "activities_code")})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiesImage extends BaseTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "activities_code")
    private String activitiesCode;
    @Column(name = "caption", columnDefinition = "text")
    private String caption;
    @Column(name = "url")
    private String url;
}
