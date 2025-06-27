package travs.repository.activities;

import travs.common.type.ApproveStatus;
import travs.entity.activities.Activities;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ActivitiesRepository extends JpaRepository<Activities, String> {

    List<Activities> findByCodeInOrderByCreatedAtDesc(List<String> code, Pageable pageable);

    List<Activities> findByCodeInOrderByCreatedAtDesc(List<String> code);

    Optional<Activities> findFirstBySlug(String slug);

    Activities findFirstByCode(String activitiesCode);

    @Query(value = "update activities set ts_search = to_tsvector(vn_unaccent(title)  || ' ' || vn_unaccent(description) || ' ' || vn_unaccent(address)) " +
            "where code = (:activitiesCode) ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateSearchVector(@Param("activitiesCode") String activitiesCode);

    void deleteAllByCode(String activitiesCode);

    List<Activities> findAllByUserId(String userId, Pageable pageable);

    int countAllByUserId(String userId);

    @Query(nativeQuery = true, value = "select * from activities where ts_search @@ plainto_tsquery(:title) " +
            " and approve_status = :status  order by created_at desc ")
    List<Activities> findAllByTitleAndApproveStatus(@Param("title") String title, @Param("status") String status);

    @Query(nativeQuery = true, value = "select * from activities where ts_search @@ plainto_tsquery(:title) order by created_at desc ")
    List<Activities> findAllByTitle(@Param("title") String title);

    List<Activities> findActivitiesByApproveStatusOrderByCreatedAtDesc(ApproveStatus status);

    @Query(nativeQuery = true, value = "select * from activities  order by created_at desc")
    List<Activities> findAllHotel();

    List<Activities> findAllByUserIdOrderByCreatedAtDesc(String userId);

    @Query(nativeQuery = true, value = "select * from activities where ts_search @@ plainto_tsquery(:title) " +
            "   order by created_at desc ")
    List<Activities> findAllByTitleTsSearch(@Param("title") String title);



}
