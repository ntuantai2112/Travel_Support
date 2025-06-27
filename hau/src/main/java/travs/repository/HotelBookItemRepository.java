package travs.repository;

import travs.entity.hotel.HotelBookableItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelBookItemRepository extends JpaRepository<HotelBookableItem, Long> {

    List<HotelBookableItem> findAllByHotelCodeOrderByPriceAsc(String hotelCode);

    List<HotelBookableItem> findAllByHotelCode(String hotelCode, Pageable pageable);

    int countAllByHotelCode(String hotelCode);

    void deleteAllByHotelCode(String hotelCode);

    void deleteById(Long id);

    HotelBookableItem findFirstById(Long id);

}
