package travs.impl;

import org.junit.jupiter.api.Test;
import travs.common.ActivitiesType;
import travs.common.SQLQueryParam;
import travs.common.type.ApproveStatus;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesGame;
import travs.entity.activities.ActivitiesImage;
import travs.repository.FavoriteRepository;
import travs.repository.activities.ActivitiesGameRepository;
import travs.repository.activities.ActivitiesImageRepository;
import travs.repository.activities.ActivitiesRepository;
import travs.request.FilterRequest;
import travs.request.SearchRequest;
import travs.request.activities.FilterRequestActivities;
import travs.response.activities.ActivitiesDTO;
import travs.response.activities.ActivitiesGameDTO;
import travs.service.impl.ActivitiesSearchServiceImpl;
import travs.utils.MappingUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class ActivitiesSearchServiceImplTest {
    @InjectMocks
    ActivitiesSearchServiceImpl activitiesSearchService;

    @Mock
    ActivitiesRepository activitiesRepository;
    @Mock
    EntityManager entityManager;
    @Mock
    ActivitiesGameRepository activitiesGameRepository;
    @Mock
    ActivitiesImageRepository activitiesImageRepository;
    @Mock
    FavoriteRepository favoriteRepository;


    @Test
    public void testSearchFilter() {

        Activities activities = Activities.builder()
                .id("1")
                .activitiesType(ActivitiesType.IN_DOOR)
                .address("hanoi")
                .adultTicketPrice(1000D)
                .childTicketPrice(2000D)
                .approveStatus(ApproveStatus.APPROVED)
                .code("AC-CODE1")
                .description("vui")
                .userId("1")
                .duration(1)
                .build();

        ActivitiesImage activitiesImage = ActivitiesImage.builder()
                .activitiesCode("AC-CODE1")
                .caption("anh ??p")
                .url("aaaaaaaaaaaaaaaa")
                .id(1)
                .build();

        ActivitiesGame activitiesGame = ActivitiesGame.builder()
                .activitiesCode("AC-CODE1")
                .description("hay")
                .id(1)
                .name("xinh du")
                .image("111111111111.jpg")
                .build();


        List<Activities> activitiesList = Arrays.asList(activities);
        List<ActivitiesImage> activitiesImageList = Arrays.asList(activitiesImage);
        List<ActivitiesGame> activitiesGameList = Arrays.asList(activitiesGame);

        List<ActivitiesDTO> activitiesDTOS = MappingUtils.map(activitiesList, ActivitiesDTO.class);

        Map<String, Object> sqlParams = new HashMap<>();

        SQLQueryParam productCodeRequest = SQLQueryParam.builder()
                .sql("select * from activities order by created_at")
                .params(sqlParams)
                .build();

        FilterRequestActivities filterRequestActivities =FilterRequestActivities.builder()
                .type("IN_DOOR")
                .build();

        SearchRequest searchRequest = SearchRequest.builder()
                .page(1)
                .perPage(15)
                .q("ha noi")
                .build();

        FilterRequest filterRequest = null;


        List<String> productCodes = Arrays.asList("AC-CODE1");


        Mockito.when(activitiesRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(searchRequest.getPage() - 1, searchRequest.getPerPage()))).thenReturn(activitiesList);

        Mockito.when(activitiesRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(searchRequest.getPage() - 1, searchRequest.getPerPage()))).thenReturn(activitiesList);

        Mockito.when(activitiesImageRepository.findUniqueImage(productCodes)).thenReturn(activitiesImageList);

        Mockito.when(activitiesGameRepository.findAllByActivitiesCodeIn(activitiesDTOS.stream().map(ActivitiesDTO::getCode).collect(Collectors.toList()))).thenReturn(activitiesGameList);

        Map<String, String> mapImage = activitiesImageList.stream().collect(Collectors.toMap(ActivitiesImage::getActivitiesCode, ActivitiesImage::getUrl));

        Map<String, List<ActivitiesGameDTO>> gameMaps = new HashMap<>();
        activitiesGameList.forEach(activitiesGame1 -> {
            ActivitiesGameDTO gameDTO = MappingUtils.map(activitiesGame1, ActivitiesGameDTO.class);
            if (gameMaps.get(activitiesGame1.getActivitiesCode()) == null || gameMaps.get(activitiesGame1.getActivitiesCode()).isEmpty()) {
                List<ActivitiesGameDTO> dtoList = new ArrayList<>();
                dtoList.add(gameDTO);
                gameMaps.put(activitiesGame1.getActivitiesCode(), dtoList);
            } else {
                List<ActivitiesGameDTO> dtoList = gameMaps.get(activitiesGame1.getActivitiesCode());
                dtoList.add(gameDTO);
                gameMaps.put(activitiesGame.getActivitiesCode(), dtoList);
            }
        });

        activitiesDTOS.forEach(activities1 -> {
            String imageUrl = mapImage.get(activities1.getCode());
            if (imageUrl != null) {
                activities1.setImage(imageUrl);
            }
            activities1.setActivitiesGameDTOS(gameMaps.get(activities.getCode()));
        });


    }




}
