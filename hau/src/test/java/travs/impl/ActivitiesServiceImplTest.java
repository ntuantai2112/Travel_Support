package travs.impl;

import travs.common.ActivitiesType;
import travs.common.type.ApproveStatus;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesGame;
import travs.entity.activities.ActivitiesImage;
import travs.repository.FavoriteRepository;
import travs.repository.activities.ActivitiesGameRepository;
import travs.repository.activities.ActivitiesImageRepository;
import travs.repository.activities.ActivitiesRepository;
import travs.request.activities.ActivitiesGameUploadBody;
import travs.request.activities.ActivitiesUploadBody;
import travs.response.BaseResponse;
import travs.response.activities.ActivitiesDTO;
import travs.response.activities.ActivitiesDetail;
import travs.response.activities.ActivitiesGameDTO;
import travs.response.hotel.ImageDTO;
import travs.service.impl.ActivitiesServiceImpl;
import travs.utils.MappingUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ActivitiesServiceImplTest  extends  Thread{

    @InjectMocks
    ActivitiesServiceImpl activitiesServiceImpl;

    @Mock
    private ActivitiesRepository activitiesRepository;

    @Mock
    private ActivitiesImageRepository activitiesImageRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ActivitiesGameRepository activitiesGameRepository;



    Activities activities = Activities.builder()
            .id("1")
            .activitiesType(ActivitiesType.IN_DOOR)
            .address("hanoi")
            .adultTicketPrice(1000D)
            .childTicketPrice(2000D)
            .slug("AC-slug-test")
            .star(1)
            .title("aaa")
            .approveStatus(ApproveStatus.APPROVED)
            .code("AC-CODE1")
            .description("vui")
            .userId("1")
            .duration(1)
            .build();

    ActivitiesGame activitiesGame = ActivitiesGame.builder()
            .activitiesCode("AC-CODE1")
            .description("hay")
            .id(1)
            .name("xinh du")
            .image("111111111111.jpg")
            .build();
    ActivitiesImage activitiesImage = ActivitiesImage.builder()
            .activitiesCode("AC-CODE1")
            .caption("anh ??p")
            .url("aaaaaaaaaaaaaaaa")
            .id(1)
            .build();

    ActivitiesUploadBody activitiesUploadBody = ActivitiesUploadBody.builder()
            .activitiesType("IN_DOOR")
            .address("hanoi")
            .adultTicketPrice(1000D)
            .childTicketPrice(2000D)
            .star(1)
            .title("aaa")
            .activitiesCode("AC-CODE1")
            .description("vui")
            .duration(1)
            .build();

    ActivitiesGameUploadBody activitiesGameUploadBody = ActivitiesGameUploadBody.builder()
            .activitiesCode("AC-CODE1")
            .gameId(1)
            .name("cho troiw")
            .description("description")
            .build();


    @Test
    public void testGetActivities() {

        List<ActivitiesImage> activitiesImages = Arrays.asList(activitiesImage);
        List<ActivitiesGame> gameList = Arrays.asList(activitiesGame);

        Mockito.when(activitiesRepository.findFirstBySlug(Mockito.any())).thenReturn(Optional.ofNullable(activities));

        Mockito.when(activitiesGameRepository.findAllByActivitiesCode(Mockito.any())).thenReturn(gameList);

        Mockito.when(activitiesImageRepository.findAllByActivitiesCode(Mockito.any())).thenReturn(activitiesImages);
        ActivitiesDetail activitiesDetail = ActivitiesDetail.builder()
                .id(activities.getId())
                .title(activities.getTitle())
                .slug(activities.getSlug())
                .code(activities.getCode())
                .duration(activities.getDuration())
                .address(activities.getAddress())
                .childPrice(activities.getChildTicketPrice())
                .adultPrice(activities.getAdultTicketPrice())
                .description(activities.getDescription())
                .isFavorite(this.checkFavoriteActivities(activities.getSlug()))
                .activitiesGameDTOS(MappingUtils.map(gameList, ActivitiesGameDTO.class))
                .build();
        activitiesDetail.setImages(MappingUtils.map(activitiesImages, ImageDTO.class));
        BaseResponse baseResponse = activitiesServiceImpl.get("AC-slug-test");
        MatcherAssert.assertThat(baseResponse.getData(), Matchers.anything(String.valueOf(activitiesDetail)));

    }

    @Test
    public void testUploadActivities() {
        List<String> images = Arrays.asList("aaaaaaaaaaaaaa.jpg");
        //luu image cua hotel vao table image
        List<ActivitiesImage> activitiesImages = new ArrayList<>();
        images.forEach(imageName -> activitiesImages.add(ActivitiesImage.builder()
                .activitiesCode("AC-CODE1")
                .caption("image for activities")
                .url(imageName)
                .build()));

        Mockito.when(activitiesImageRepository.saveAll(Mockito.any())).thenReturn(activitiesImages);
        Mockito.when(activitiesRepository.save(Mockito.any())).thenReturn(activities);

        MappingUtils.map(activities, ActivitiesDTO.class);

        BaseResponse baseResponse = BaseResponse.builder()
                .data(activities)
                .build();

        activitiesRepository.updateSearchVector(activities.getCode());
        BaseResponse.ok(baseResponse);
    }


    @Test
    public void testUpdateActivities() {
        List<String> images = Arrays.asList("aaaaaaaaaaaaaa.jpg");

        List<ActivitiesImage> activitiesImages = new ArrayList<>();
        images.forEach(imageName -> activitiesImages.add(ActivitiesImage.builder()
                .activitiesCode(activitiesUploadBody.getActivitiesCode())
                .caption("image for hotel")
                .url(imageName)
                .build()));

        Mockito.when(activitiesRepository.findFirstByCode(Mockito.any())).thenReturn(activities);
        Mockito.when(activitiesImageRepository.saveAll(Mockito.any())).thenReturn(activitiesImages);

         Mockito.when(activitiesRepository.save(Mockito.any())).thenReturn(activities);

        Mockito.doNothing().when(activitiesRepository).updateSearchVector("AC-CODE1");

        activitiesServiceImpl.updateActivities(activitiesUploadBody);
    }

    @Test
    public void testUploadGameActivities() {
        String image = "activities-gameabc.jpg";
        ActivitiesGame activitiesGame = ActivitiesGame.builder()
                .activitiesCode(activitiesGameUploadBody.getActivitiesCode())
                .description(activitiesGameUploadBody.getDescription())
                .image(image)
                .name(activitiesGameUploadBody.getName())
                .build();

        Mockito.when(activitiesGameRepository.save(Mockito.any())).thenReturn(activitiesGame);
        activitiesServiceImpl.uploadGameActivities(activitiesGameUploadBody);

    }

   @Test
    public void testDeleteActivities() throws InterruptedException {
       Mockito.doNothing().when(activitiesImageRepository).deleteAllByActivitiesCode(activities.getCode());
       Thread.sleep(500);
       Mockito.doNothing().when(activitiesRepository).deleteAllByCode(activitiesGame.getActivitiesCode());
       Thread.sleep(500);
       Mockito.doNothing().when(activitiesGameRepository).deleteAllByActivitiesCode(activitiesImage.getActivitiesCode());
       Thread.sleep(500);
       activitiesServiceImpl.deleteActivities("AC-CODE1");


    }

    private boolean checkFavoriteActivities(String slug) {
        String userId = "1";
        if (userId != null) {
            return favoriteRepository.findByUserIdAndSlug("1", slug).isPresent();
        }
        return false;
    }

}
