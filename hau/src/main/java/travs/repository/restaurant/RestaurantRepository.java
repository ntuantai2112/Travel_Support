package travs.repository.restaurant;

import travs.common.type.ApproveStatus;
import travs.entity.restaurant.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    List<Restaurant> findByCodeInOrderByCreatedAtDesc(List<String> code, Pageable pageable);

    List<Restaurant> findByCodeInOrderByCreatedAtDesc(List<String> code);

    Optional<Restaurant> findFirstBySlug(String slug);

    Restaurant findFirstByCode(String restaurantCode);

    @Query(value = "update Restaurant set ts_search = to_tsvector(vn_unaccent(title)  || ' ' || vn_unaccent(description) || ' ' || vn_unaccent(address)) " +
            "where code = (:restaurantCode)", nativeQuery = true)
    @Modifying
    @Transactional
    void updateSearchVector(@Param("restaurantCode") String restaurantCode);

    void deleteAllByCode(String restaurantCode);

    List<Restaurant> findAllByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from restaurant where ts_search @@ plainto_tsquery(:title) " +
            " and approve_status = :status order by created_at desc ")
    List<Restaurant> findAllByTitleAndApproveStatus(@Param("title") String title, @Param("status") ApproveStatus status, Pageable pageable);


    @Query(nativeQuery = true, value = "select * from restaurant where ts_search @@ plainto_tsquery(:title) " +
            " and approve_status = :status order by created_at desc ")
    List<Restaurant> findAllByTitleAndApproveStatus(@Param("title") String title, @Param("status") String status);

    @Query(nativeQuery = true, value = "select * from restaurant where ts_search @@ plainto_tsquery(:title)  order by created_at desc")
    List<Restaurant> findAllByTitle(@Param("title") String title);

    List<Restaurant> findRestaurantByApproveStatusOrderByCreatedAtDesc(ApproveStatus status);

    @Query(nativeQuery = true, value = "select * from restaurant order by created_at desc ")
    List<Restaurant> findAllRestaurant();

    List<Restaurant> findAllByUserIdOrderByCreatedAtDesc( String userId);


    @Query(nativeQuery = true, value = "select * from restaurant where ts_search @@ plainto_tsquery(:title) " +
            "  order by created_at desc ")
    List<Restaurant> findAllByTitleTsSearch(@Param("title") String title);


}
