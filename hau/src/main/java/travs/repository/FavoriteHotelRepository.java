package travs.repository;

import travs.entity.hotel.FavoriteHotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteHotelRepository extends JpaRepository<FavoriteHotel, String> {

    List<FavoriteHotel> findAllByUserId(String userId);

    Optional<List<FavoriteHotel>> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    Optional<FavoriteHotel> findByUserIdAndSlug(String userId, String slug);

    Integer countAllByUserId(String userId);
}
