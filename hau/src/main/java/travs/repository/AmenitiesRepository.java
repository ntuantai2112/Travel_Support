package travs.repository;

import travs.entity.hotel.Amenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenitiesRepository extends JpaRepository<Amenities, Integer> {
    List<Amenities> findAllByIdIn(List<Integer> ids);
}
