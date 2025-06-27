package travs.repository.customer;

import travs.entity.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelCustomerRepository extends JpaRepository<Hotel, String> {

    @Query(nativeQuery = true, value = "select * from hotel where ts_search @@ plainto_tsquery(:title)   and approve_status = 'APPROVED' order by created_at desc")
    List<Hotel> findAllByTitle(@Param("title") String title);

    List<Hotel> findAllByOrderByCreatedAtDesc();


}
