package travs.repository.activities;

import travs.entity.activities.ActivitiesGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivitiesGameRepository extends JpaRepository<ActivitiesGame, Integer> {

    ActivitiesGame findFirstById(Integer id);

    List<ActivitiesGame> findAllByIdIn(List<Integer> ids);

    List<ActivitiesGame> findAllByActivitiesCode(String activitiesCode);

    List<ActivitiesGame> findAllByActivitiesCodeIn(List<String> activitiesCode);

    void deleteAllByActivitiesCode(String activitiesCode);
}
