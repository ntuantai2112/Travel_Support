package travs.repository;

import travs.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, String> {

    List<Favorite> findAllByUserId(String userId);

    Optional<List<Favorite>> findByUserIdOrderByCreatedAtDesc(String userId);

    Optional<Favorite> findByUserIdAndSlug(String userId, String slug);


}
