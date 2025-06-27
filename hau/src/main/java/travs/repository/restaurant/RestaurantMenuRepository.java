package travs.repository.restaurant;

import travs.entity.restaurant.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {

    List<RestaurantMenu> findAllByRestaurantCodeOrderByPrice(String restaurantCode);

//    List<RestaurantMenu> findAllByRestaurantCode(String restaurantCode, Pageable pageable);

    List<RestaurantMenu> findAllByRestaurantCodeIn(List<String> restaurantCodes);

    void deleteAllByRestaurantCode(String restaurantCode);

    void deleteAllById(Long id);

    RestaurantMenu findFirstById(Long id);

    List<RestaurantMenu> findAllByIdIn(List<Long> ids);

    List<RestaurantMenu> findAllByRestaurantCode(String restaurantCode);

//    List<RestaurantMenu> findAllByRestaurantCode(List<String> restaurantCode);


}
