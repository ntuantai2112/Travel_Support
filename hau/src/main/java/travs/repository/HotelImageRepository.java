package travs.repository;

import travs.entity.hotel.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelImageRepository extends JpaRepository<HotelImage, Long> {
    List<HotelImage> findAllByHotelCode(String hotelCode);

    @Query(nativeQuery = true, value = "select url from hotel_image where hotel_code = :hotelCode ")
    List<String> findAllHotelByCode(@Param("hotelCode") String hotelCode);

    void deleteAllByHotelCode(String hotelCode);

    @Query(nativeQuery = true, value = "select distinct on(hotel_code) * from hotel_image hi " +
            " where image_type = 'MAIN' and hotel_code in(:codeList) ")
    List<HotelImage> findUniqueImage(@Param("codeList") List<String> codeList);
}
