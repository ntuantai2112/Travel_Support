package travs.repository.customer;

import travs.entity.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantCustomerRepository extends JpaRepository<Restaurant, String> {

    @Query(nativeQuery = true, value = "select * from restaurant where ts_search @@ plainto_tsquery(:title)  and approve_status = 'APPROVED' order by created_at desc  ")
    List<Restaurant> findAllByTitle(@Param("title") String title);

    List<Restaurant> findAllByOrderByCreatedAtDesc();

}
