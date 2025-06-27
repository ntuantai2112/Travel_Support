package travs.service.impl.restaurant;

import travs.common.type.ApproveStatus;
import travs.common.type.ImageType;
import travs.utils.AuthenticationUtils;
import travs.utils.FileStore;
import travs.utils.HelperUtils;
import travs.utils.MappingUtils;

import travs.entity.restaurant.Feature;
import travs.entity.restaurant.Restaurant;
import travs.entity.restaurant.RestaurantImage;
import travs.entity.restaurant.RestaurantMenu;
import travs.exception.ErrorCode;
import travs.exception.GeneralException;
import travs.exception.ResourceNotFoundException;
import travs.repository.restaurant.FeatureRepository;
import travs.repository.restaurant.RestaurantImageRepository;
import travs.repository.restaurant.RestaurantMenuRepository;
import travs.repository.restaurant.RestaurantRepository;
import travs.request.restaurant.RestaurantMenuUploadBody;
import travs.request.restaurant.RestaurantUploadRequest;
import travs.response.BaseResponse;
import travs.response.hotel.FacilityDTO;
import travs.response.hotel.ImageDTO;
import travs.response.restaurant.RestaurantDTO;
import travs.response.restaurant.RestaurantDetail;
import travs.response.restaurant.RestaurantMenuDTO;
import travs.service.impl.HelperService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Log4j2
@AllArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private HelperService helperService;
    private RestaurantRepository restaurantRepository;
    private RestaurantImageRepository restaurantImageRepository;
    private RestaurantMenuRepository restaurantMenuRepository;
    private FeatureRepository featureRepository;

//    private HotelBookItemRepository hotelBookItemRepository;

    @Override
    public BaseResponse get(String slug) {


        Restaurant restaurant = restaurantRepository.findFirstBySlug(slug).orElseThrow(ResourceNotFoundException::new);
        List<Feature> features = featureRepository.findAll();
        Map<Integer, Feature> map = features.stream().collect(Collectors.toMap(Feature::getId, Function.identity()));
        List<FacilityDTO> facilityDTOList = buildFavorite(restaurant, map);


        List<RestaurantMenu> menuList = restaurantMenuRepository.findAllByRestaurantCode(restaurant.getCode());


        RestaurantDetail restaurantDetail = RestaurantDetail.builder()
                .id(restaurant.getId())
                .title(restaurant.getTitle())
                .slug(restaurant.getSlug())
                .code(restaurant.getCode())
                .address(restaurant.getAddress())
                .facilityDTOList(facilityDTOList)
                .description(restaurant.getDescription())
                .isFavorite(helperService.checkFavorite(restaurant.getSlug()))
                .restaurantMenuDTO(MappingUtils.map(menuList, RestaurantMenuDTO.class))
                .build();
        buildImages(restaurantDetail);

        return BaseResponse.ok(restaurantDetail);
    }

    private List<FacilityDTO> buildFavorite(Restaurant restaurant, Map<Integer, Feature> map) {
        List<Integer> favorite = Arrays.asList(restaurant.getFeature());
        return favorite.stream().map(favoriteId -> FacilityDTO.builder()
                .id(favoriteId.toString())
                .name(map.get(favoriteId).getName())
                .build()).collect(Collectors.toList());
    }

    @Override
    public BaseResponse uploadRestaurant(RestaurantUploadRequest restaurantUploadRequest) {

        String userId = AuthenticationUtils.getUserId();
        String restaurantCode = HelperUtils.randomRestaurantCode();

        List<String> images = FileStore.getFilePaths(restaurantUploadRequest.getImageList(), "restaurant");

        List<RestaurantImage> restaurantImage = new ArrayList<>();
        images.forEach(imageName -> restaurantImage.add(RestaurantImage.builder()
                .restaurantCode(restaurantCode)
                .caption("image for restaurant")
                .url(imageName)
                .imageType(ImageType.MAIN)
                .build()));
        restaurantImageRepository.saveAll(restaurantImage);

        // lưu restaurant vao restaurant
        Restaurant restaurant = Restaurant.builder()
                .slug(HelperUtils.toSlug(restaurantUploadRequest.getTitle() + restaurantCode))
                .feature(restaurantUploadRequest.getFeature())
                .description(restaurantUploadRequest.getDescription())
                .title(restaurantUploadRequest.getTitle())
                .address(restaurantUploadRequest.getAddress())
                .restaurantType((restaurantUploadRequest.getRestaurantType()))
                .code(restaurantCode)
                .userId(userId)
                .approveStatus(ApproveStatus.PENDING)
                .build();
        restaurant = restaurantRepository.save(restaurant);
        restaurantRepository.updateSearchVector(restaurant.getCode());

        return BaseResponse.ok(restaurant);
    }

    @Override
    public BaseResponse updateRestaurant(RestaurantUploadRequest restaurantUploadRequest) {
        Restaurant restaurant = restaurantRepository.findFirstByCode(restaurantUploadRequest.getRestaurantCode());
        if (restaurant == null) throw new ResourceNotFoundException();

        List<String> images = FileStore.getFilePaths(restaurantUploadRequest.getImageList(), "restaurant");

        //luu image cua hotel vao table image
        List<RestaurantImage> hotelImages = new ArrayList<>();
        images.forEach(imageName -> hotelImages.add(RestaurantImage.builder()
                .restaurantCode(restaurantUploadRequest.getRestaurantCode())
                .caption("image for hotel")
                .url(imageName)
                .imageType(ImageType.MAIN)
                .build()));
        restaurantImageRepository.saveAll(hotelImages);

        //luu hotel vao table hotel
        if (restaurantUploadRequest.getFeature() != null) restaurant.setFeature(restaurantUploadRequest.getFeature());
        if (!restaurantUploadRequest.getAddress().isEmpty())
            restaurant.setAddress(restaurantUploadRequest.getAddress());
        if (restaurantUploadRequest.getDescription() != null)
            restaurant.setDescription(restaurantUploadRequest.getDescription());
        if (restaurantUploadRequest.getTitle() != null) restaurant.setTitle(restaurantUploadRequest.getTitle());

        restaurant = restaurantRepository.save(restaurant);
        restaurantRepository.updateSearchVector(restaurant.getCode());
        return BaseResponse.ok(restaurant);
    }

    @Override
    public void uploadRestaurantMenu(RestaurantMenuUploadBody restaurantMenuUploadBody) {
        String image = FileStore.getFilePath(restaurantMenuUploadBody.getMultipartFile(), "restaurant-menu");
        RestaurantMenu restaurantMenu = RestaurantMenu.builder()
                .available(true)
                .restaurantCode(restaurantMenuUploadBody.getRestaurantCode())
                .image(image)
                .currency(restaurantMenuUploadBody.getCurrency())
                .name(restaurantMenuUploadBody.getName())
                .description(restaurantMenuUploadBody.getDescription())
                .price(restaurantMenuUploadBody.getPrice())
                .build();
        restaurantMenuRepository.save(restaurantMenu);
    }

    @Override
    public void updateRestaurantMenu(RestaurantMenuUploadBody restaurantMenuUploadBody) {
        RestaurantMenu menu = restaurantMenuRepository.findFirstById(restaurantMenuUploadBody.getMenuId());
        if (menu == null) throw new ResourceNotFoundException();


        String image = FileStore.getFilePath(restaurantMenuUploadBody.getMultipartFile(), "hotel-room-");
        //luu image cua hotel vao table image
        RestaurantImage restaurantImage = RestaurantImage.builder()
                .restaurantCode(restaurantMenuUploadBody.getRestaurantCode())
                .caption("image for room hotel")
                .url(image)
                .imageType(ImageType.ROOM)
                .build();
        restaurantImageRepository.save(restaurantImage);

        if (restaurantMenuUploadBody.getName() != null) menu.setName(restaurantMenuUploadBody.getName());
        if (restaurantMenuUploadBody.getDescription() != null)
            menu.setDescription(restaurantMenuUploadBody.getDescription());
        if (restaurantMenuUploadBody.getPrice() != null) menu.setPrice(restaurantMenuUploadBody.getPrice());
        if (image != null) menu.setImage(image);
        restaurantMenuRepository.save(menu);


    }


    @Override
    public void deleteRestaurant(String restaurantCode) {
        restaurantRepository.deleteAllByCode(restaurantCode);
        restaurantImageRepository.deleteAllByRestaurantCode(restaurantCode);
        restaurantMenuRepository.deleteAllByRestaurantCode(restaurantCode);
    }

    @Override
    public void deleteRestaurantMenu(Long menuId) {
        restaurantMenuRepository.deleteAllById(menuId);
    }


    @Override
    public BaseResponse getListRestaurant(Integer page, Integer perPage) {

        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }

        List<Restaurant> restaurantList = restaurantRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        List<String> restaurantCodes = restaurantList.stream().map(Restaurant::getCode).collect(Collectors.toList());

        List<Restaurant> restaurantListQuery = restaurantRepository.findByCodeInOrderByCreatedAtDesc(restaurantCodes, PageRequest.of(page, perPage));

        List<RestaurantDTO> restaurantDTOs = MappingUtils.map(restaurantListQuery, RestaurantDTO.class);

        List<Feature> features = featureRepository.findAll();
        Map<Integer, Feature> map = features.stream().collect(Collectors.toMap(Feature::getId, Function.identity()));


        restaurantDTOs.forEach(restaurant -> restaurant.setFacilityDTOList(restaurant.getFeature()
                .stream().map(restaurantId -> FacilityDTO.builder()
                        .id(restaurantId.toString())
                        .name(map.get(restaurantId).getName())
                        .build())
                .collect(Collectors.toList())));

        List<RestaurantImage> restaurantImages = restaurantImageRepository.findUniqueImage(restaurantCodes);

        Map<String, String> mapImage = restaurantImages.stream().collect(Collectors.toMap(RestaurantImage::getRestaurantCode, RestaurantImage::getUrl));

        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAllByRestaurantCodeIn(restaurantDTOs.stream().map(RestaurantDTO::getCode).collect(Collectors.toList()));

        Map<String, List<RestaurantMenuDTO>> menuMaps = new HashMap<>();
        restaurantMenuList.forEach(restaurantMenu -> {
            RestaurantMenuDTO menuDTO = MappingUtils.map(restaurantMenu, RestaurantMenuDTO.class);
            if (menuMaps.get(restaurantMenu.getRestaurantCode()) == null || menuMaps.get(restaurantMenu.getRestaurantCode()).isEmpty()) {
                List<RestaurantMenuDTO> dtoList = new ArrayList<>();
                dtoList.add(menuDTO);
                menuMaps.put(restaurantMenu.getRestaurantCode(), dtoList);
            } else {
                List<RestaurantMenuDTO> dtoList = menuMaps.get(restaurantMenu.getRestaurantCode());
                dtoList.add(menuDTO);
                menuMaps.put(restaurantMenu.getRestaurantCode(), dtoList);
            }
        });

        restaurantDTOs.forEach(restaurants -> {
            String imageUrl = mapImage.get(restaurants.getCode());
            if (imageUrl != null) {
                restaurants.setImage(imageUrl);
            }
            restaurants.setRestaurantMenuDTOList(menuMaps.get(restaurants.getCode()));

        });

        Map<String, Integer> mapReturn = new HashMap<>();
        mapReturn.put("total", restaurantCodes.size());
        return BaseResponse.ok(restaurantDTOs, mapReturn);
    }

    private void buildImages(RestaurantDetail restaurantDetail) {
        List<RestaurantImage> restaurantImage = restaurantImageRepository.findAllImageByRestaurantCode(restaurantDetail.getCode());
        restaurantDetail.setImages(MappingUtils.map(restaurantImage, ImageDTO.class));
    }

}
