package travs.repository.restaurant;

import travs.entity.restaurant.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Long> {

    void deleteAllByRestaurantCode(String restaurantCode);

    @Query(nativeQuery = true, value = "select url from restaurant_image where restaurant_code = :restaurantCode ")
    List<String> findAllByRestaurantCode(@Param("restaurantCode") String restaurantCode);

    @Query(nativeQuery = true, value = "select distinct on(restaurant_code) * from restaurant_image ri " +
            " where restaurant_code in(:codeList) ")
    List<RestaurantImage> findUniqueImage(@Param("codeList") List<String> codeList);

    @Query(nativeQuery = true, value = "select res.* from restaurant_image res where restaurant_code = :restaurantCode ")
    List<RestaurantImage> findAllImageByRestaurantCode(@Param("restaurantCode") String restaurantCode);

}
