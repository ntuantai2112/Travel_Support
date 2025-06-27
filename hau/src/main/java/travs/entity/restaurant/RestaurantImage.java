package travs.entity.restaurant;

import travs.common.type.ImageType;
import travs.entity.BaseTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "restaurant_image",
        indexes = {@Index(name = "restaurant_image_code_idx", columnList = "restaurant_code")})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// lưu các ảnh của khách sạn
public class RestaurantImage extends BaseTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "restaurant_code")
    private String restaurantCode;
    @Column(name = "caption", columnDefinition = "text")
    private String caption;
    @Column(name = "url")
    private String url;
    @Enumerated(EnumType.STRING)
    @Column(name = "image_type")
    private ImageType imageType;
}
