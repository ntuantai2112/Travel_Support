package travs.repository;

import travs.common.type.ApproveStatus;
import travs.entity.hotel.Hotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, String> {

    List<Hotel> findByCodeInOrderByCreatedAtDesc(List<String> code, Pageable pageable);

    List<Hotel> findByCodeInOrderByCreatedAtDesc(List<String> code);

    Optional<Hotel> findFirstBySlug(String slug);

    Hotel findFirstByCode(String hotelCode);

    @Query(value = "update hotel set ts_search = to_tsvector(vn_unaccent(title)  || ' ' || vn_unaccent(description) || ' ' || vn_unaccent(address)) " +
            "where code = (:hotelCode) ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateSearchVector(@Param("hotelCode") String hotelCode);

    void deleteAllByCode(String hotelCode);

    List<Hotel> findAllByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    int countAllByUserId(String userId);

    @Query(nativeQuery = true, value = "select * from hotel where ts_search @@ plainto_tsquery(:title) " +
            " and approve_status = :status order by created_at desc ")
    List<Hotel> findAllByTitleAndApproveStatus(@Param("title") String title, @Param("status") String status);

    @Query(nativeQuery = true, value = "select * from hotel where ts_search @@ plainto_tsquery(:title)  order by created_at desc")
    List<Hotel> findAllByTitle(@Param("title") String title);

    List<Hotel> findHotelByApproveStatusOrderByCreatedAtDesc(ApproveStatus status);

    @Query(nativeQuery = true, value = "select * from hotel order by created_at desc ")
    List<Hotel>findAllHotel();

    List<Hotel> findAllByUserIdOrderByCreatedAtDesc( String userId);

    @Query(nativeQuery = true, value = "select * from hotel where ts_search @@ plainto_tsquery(:title) " +
            "  order by created_at desc ")
    List<Hotel> findAllByTitleAndTsSearch(@Param("title") String title);

}
