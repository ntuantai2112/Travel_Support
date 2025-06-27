package travs.repository.activities;

import travs.entity.activities.ActivitiesImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivitiesImageRepository extends JpaRepository<ActivitiesImage, Long> {
    List<ActivitiesImage> findAllByActivitiesCode(String activitiesCode);

    @Query(nativeQuery = true, value = "select url from activities_image where activities_code = :activitiesCode ")
    List<String> findAllActivitiesCode(@Param("activitiesCode") String activitiesCode);

    void deleteAllByActivitiesCode(String hotelCode);

    @Query(nativeQuery = true, value = "select distinct on(activities_code) * from activities_image hi " +
            " where activities_code in(:codeList) ")
    List<ActivitiesImage> findUniqueImage(@Param("codeList") List<String> codeList);
}
