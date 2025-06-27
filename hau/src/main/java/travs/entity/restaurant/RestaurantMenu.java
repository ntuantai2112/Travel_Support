package travs.entity.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "restaurant_menu",
        indexes = {@Index(name = "r_m_restaurant_code_idx", columnList = "restaurant_code")})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "restaurant_code")
    private String restaurantCode;

    @Column(name = "price")
    private Double price;

    @Column(name = "currency")
    private String currency;

    @Column(name = "description")
    private String description ;

    @Column(name = "name")
    private String name ;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "image")
    private String image;


}
