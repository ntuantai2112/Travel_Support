package travs.repository.customer;

import travs.entity.activities.Activities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivitiesCustomerRepository extends JpaRepository<Activities, String> {

    @Query(nativeQuery = true, value = "select * from activities where ts_search @@ plainto_tsquery(:title)  and approve_status = 'APPROVED' order by created_at desc" )
    List<Activities> findAllByTitle(@Param("title") String title);

    List<Activities> findAllByOrderByCreatedAtDesc();


}
