package travs.service.impl;

import travs.common.ActivitiesType;
import travs.common.type.ApproveStatus;
import travs.exception.ErrorCode;
import travs.exception.GeneralException;
import travs.exception.ResourceNotFoundException;
import travs.repository.FavoriteRepository;
import travs.repository.activities.ActivitiesGameRepository;
import travs.repository.activities.ActivitiesImageRepository;
import travs.repository.activities.ActivitiesRepository;
import travs.response.BaseResponse;
import travs.response.activities.ActivitiesDTO;
import travs.response.activities.ActivitiesDetail;
import travs.response.activities.ActivitiesGameDTO;
import travs.response.hotel.ImageDTO;
import travs.utils.AuthenticationUtils;
import travs.utils.FileStore;
import travs.utils.HelperUtils;
import travs.utils.MappingUtils;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesGame;
import travs.entity.activities.ActivitiesImage;
import travs.request.activities.ActivitiesGameUploadBody;
import travs.request.activities.ActivitiesUploadBody;
import travs.service.ActivitiesService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j2
@AllArgsConstructor
public class ActivitiesServiceImpl implements ActivitiesService {
    private ActivitiesRepository activitiesRepository;
    private ActivitiesImageRepository activitiesImageRepository;
    private FavoriteRepository favoriteRepository;
    private ActivitiesGameRepository activitiesGameRepository;

    @Override
    public BaseResponse get(String slug) {
        Activities activities = activitiesRepository.findFirstBySlug(slug).orElseThrow(ResourceNotFoundException::new);
        List<ActivitiesGame> gameList = activitiesGameRepository.findAllByActivitiesCode(activities.getCode());

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
        buildImages(activitiesDetail);

        return BaseResponse.ok(activitiesDetail);
    }


    private void buildImages(ActivitiesDetail activitiesDetail) {
        List<ActivitiesImage> activitiesImages = activitiesImageRepository.findAllByActivitiesCode(activitiesDetail.getCode());
        activitiesDetail.setImages(MappingUtils.map(activitiesImages, ImageDTO.class));
    }


    private boolean checkFavoriteActivities(String slug) {
        String userId = AuthenticationUtils.getUserId();
        if (userId != null) {
            return favoriteRepository.findByUserIdAndSlug(userId, slug).isPresent();
        }
        return false;
    }

    @Transactional
    public BaseResponse uploadActivities(ActivitiesUploadBody activitiesUploadBody) {
        String userId = AuthenticationUtils.getUserId();
        String activitiesCode = HelperUtils.randomActivity();
        List<String> images = FileStore.getFilePaths(activitiesUploadBody.getImageList(), "activities");

        //luu image cua hotel vao table image
        List<ActivitiesImage> activitiesImages = new ArrayList<>();
        images.forEach(imageName -> activitiesImages.add(ActivitiesImage.builder()
                .activitiesCode(activitiesCode)
                .caption("image for activities")
                .url(imageName)
                .build()));
        activitiesImageRepository.saveAll(activitiesImages);

        Activities activities = Activities.builder()
                .activitiesType(ActivitiesType.valueOf(activitiesUploadBody.getType()))
                .code(activitiesCode)
                .address(activitiesUploadBody.getAddress())
                .slug(HelperUtils.toSlug(activitiesUploadBody.getTitle() + activitiesCode))
                .description(activitiesUploadBody.getDescription())
                .star(activitiesUploadBody.getStar())
                .title(activitiesUploadBody.getTitle())
                .adultTicketPrice(activitiesUploadBody.getAdultTicketPrice())
                .childTicketPrice(activitiesUploadBody.getChildTicketPrice())
                .duration(activitiesUploadBody.getDuration())
                .userId(userId)
                .approveStatus(ApproveStatus.PENDING)
                .build();
        activities = activitiesRepository.save(activities);
        activitiesRepository.updateSearchVector(activities.getCode());
        return BaseResponse.ok(MappingUtils.map(activities, ActivitiesDTO.class));
    }

    @Override
    public BaseResponse updateActivities(ActivitiesUploadBody activitiesUploadBody) {

        Activities activities = activitiesRepository.findFirstByCode(activitiesUploadBody.getActivitiesCode());
        if (activities == null) throw new ResourceNotFoundException();

        List<String> images = FileStore.getFilePaths(activitiesUploadBody.getImageList(), "activities");

        //luu image cua hotel vao table image
        List<ActivitiesImage> activitiesImages = new ArrayList<>();
        images.forEach(imageName -> activitiesImages.add(ActivitiesImage.builder()
                .activitiesCode(activitiesUploadBody.getActivitiesCode())
                .caption("image for hotel")
                .url(imageName)
                .build()));
        activitiesImageRepository.saveAll(activitiesImages);

        //luu hotel vao table hotel
        if (!activitiesUploadBody.getAddress().isEmpty()) activities.setAddress(activitiesUploadBody.getAddress());
        if (activitiesUploadBody.getDescription() != null) activities.setDescription(activitiesUploadBody.getDescription());
        if (activitiesUploadBody.getTitle() != null) activities.setTitle(activitiesUploadBody.getTitle());
        if (activitiesUploadBody.getDuration() != null) activities.setDuration(activitiesUploadBody.getDuration());
        if (activitiesUploadBody.getChildTicketPrice() != null) activities.setChildTicketPrice(activitiesUploadBody.getChildTicketPrice());
        if (activitiesUploadBody.getAdultTicketPrice() != null) activities.setAdultTicketPrice(activitiesUploadBody.getAdultTicketPrice());
        if (activitiesUploadBody.getType() != null) activities.setActivitiesType(ActivitiesType.valueOf(activitiesUploadBody.getType()));

        activities = activitiesRepository.save(activities);
        activitiesRepository.updateSearchVector(activities.getCode());
        return BaseResponse.ok(activities);

    }


    @Transactional
    public BaseResponse uploadGameActivities(ActivitiesGameUploadBody activitiesGameUploadBody) {
        String image = FileStore.getFilePath(activitiesGameUploadBody.getImage(), "activities-game");

        ActivitiesGame activitiesGame = ActivitiesGame.builder()
                .activitiesCode(activitiesGameUploadBody.getActivitiesCode())
                .description(activitiesGameUploadBody.getDescription())
                .image(image)
                .name(activitiesGameUploadBody.getName())
                .build();
        activitiesGame = activitiesGameRepository.save(activitiesGame);
        return BaseResponse.ok(activitiesGame);
    }

    @Override
    public BaseResponse updateGameActivities(ActivitiesGameUploadBody activitiesGameUploadBody) {
        ActivitiesGame game = activitiesGameRepository.findFirstById(activitiesGameUploadBody.getGameId());
        if (game == null) throw new ResourceNotFoundException();


        String image = FileStore.getFilePath(activitiesGameUploadBody.getImage(), "activities-game");
        //luu image cua hotel vao table image
        ActivitiesImage activitiesImage = ActivitiesImage.builder()
                .activitiesCode(activitiesGameUploadBody.getActivitiesCode())
                .caption("image for room hotel")
                .url(image)
                .build();
        activitiesImageRepository.save(activitiesImage);

        if (activitiesGameUploadBody.getName() != null) game.setName(activitiesGameUploadBody.getName());
        if (activitiesGameUploadBody.getDescription() != null) game.setDescription(activitiesGameUploadBody.getDescription());
        if (image != null) game.setImage(image);
        game=  activitiesGameRepository.save(game);
        return BaseResponse.ok(game);


    }

    public BaseResponse getListActivities(Integer page, Integer perPage) {
        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }

        List<Activities> activitiesList = activitiesRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        List<String> productCodes = activitiesList.stream().map(Activities::getCode).collect(Collectors.toList());

        List<Activities> activitiesListQuery = activitiesRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(page, perPage));
        List<ActivitiesDTO> activitiesDTOS = MappingUtils.map(activitiesListQuery, ActivitiesDTO.class);

        List<ActivitiesImage> activitiesImages = activitiesImageRepository.findUniqueImage(productCodes);
        Map<String, String> mapImage = activitiesImages.stream().collect(Collectors.toMap(ActivitiesImage::getActivitiesCode, ActivitiesImage::getUrl));

        List<ActivitiesGame> activitiesGameList = activitiesGameRepository.findAllByActivitiesCodeIn(activitiesDTOS.stream().map(ActivitiesDTO::getCode).collect(Collectors.toList()));

        Map<String, List<ActivitiesGameDTO>> gameMaps = new HashMap<>();
        activitiesGameList.forEach(activitiesGame -> {
            ActivitiesGameDTO gameDTO = MappingUtils.map(activitiesGame, ActivitiesGameDTO.class);
            if (gameMaps.get(activitiesGame.getActivitiesCode()) == null || gameMaps.get(activitiesGame.getActivitiesCode()).isEmpty()) {
                List<ActivitiesGameDTO> dtoList = new ArrayList<>();
                dtoList.add(gameDTO);
                gameMaps.put(activitiesGame.getActivitiesCode(), dtoList);
            } else {
                List<ActivitiesGameDTO> dtoList = gameMaps.get(activitiesGame.getActivitiesCode());
                dtoList.add(gameDTO);
                gameMaps.put(activitiesGame.getActivitiesCode(), dtoList);
            }
        });

        activitiesDTOS.forEach(activities -> {
            String imageUrl = mapImage.get(activities.getCode());
            if (imageUrl != null) {
                activities.setImage(imageUrl);
            }
            activities.setActivitiesGameDTOS(gameMaps.get(activities.getCode()));
        });

        Map<String, Integer> mapReturn = new HashMap<>();
        mapReturn.put("total", productCodes.size());
        return BaseResponse.ok(activitiesDTOS, mapReturn);
    }

    @Transactional
    public void deleteActivities(String activitiesCode) {
        activitiesImageRepository.deleteAllByActivitiesCode(activitiesCode);
        activitiesRepository.deleteAllByCode(activitiesCode);
        activitiesGameRepository.deleteAllByActivitiesCode(activitiesCode);
    }

    @Transactional
    public void deleteGameActivities(Integer id) {
        activitiesGameRepository.deleteById(id);
    }

}
