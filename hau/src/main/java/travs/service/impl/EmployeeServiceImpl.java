package travs.service.impl;

import travs.common.type.ApproveStatus;
import travs.entity.restaurant.Restaurant;
import travs.entity.restaurant.RestaurantImage;
import travs.entity.restaurant.RestaurantMenu;
import travs.repository.AmenitiesRepository;
import travs.repository.HotelImageRepository;
import travs.repository.HotelRepository;
import travs.repository.activities.ActivitiesGameRepository;
import travs.repository.activities.ActivitiesImageRepository;
import travs.repository.activities.ActivitiesRepository;
import travs.repository.restaurant.RestaurantImageRepository;
import travs.repository.restaurant.RestaurantMenuRepository;
import travs.repository.restaurant.RestaurantRepository;
import travs.response.BaseResponse;
import travs.response.activities.ActivitiesDTO;
import travs.response.activities.ActivitiesGameDTO;
import travs.response.hotel.FacilityDTO;
import travs.response.hotel.HotelDTO;
import travs.response.restaurant.RestaurantDTO;
import travs.response.restaurant.RestaurantMenuDTO;
import travs.utils.HelperUtils;
import travs.utils.MappingUtils;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesGame;
import travs.entity.activities.ActivitiesImage;
import travs.entity.hotel.Amenities;
import travs.entity.hotel.Hotel;
import travs.entity.hotel.HotelImage;
import travs.request.activities.ActivitiesApproveRequest;
import travs.request.hotel.HotelApproveRequest;
import travs.request.restaurant.RestaurantApproveRequest;
import travs.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private HotelRepository hotelRepository;
    private AmenitiesRepository amenitiesRepository;
    private HotelImageRepository hotelImageRepository;

    private ActivitiesRepository activitiesRepository;
    private ActivitiesGameRepository activitiesGameRepository;
    private ActivitiesImageRepository activitiesImageRepository;

    private RestaurantRepository restaurantRepository;
    private RestaurantMenuRepository restaurantMenuRepository;
    private RestaurantImageRepository restaurantImageRepository;


    public void approveHotel(HotelApproveRequest hotelApproveRequest) {
        Hotel hotel = hotelRepository.findFirstByCode(hotelApproveRequest.getHotelCode());
        hotel.setApproveStatus(hotelApproveRequest.getStatus());
        hotelRepository.save(hotel);
    }

    public BaseResponse getListHotel(String hotelName, ApproveStatus status, Integer page, Integer perPage) {
        List<Hotel> hotelList;
        if (status == null && hotelName.isEmpty()) {
            hotelList = hotelRepository.findAllHotel();
        } else if (hotelName.isEmpty()) {
            hotelList = hotelRepository.findHotelByApproveStatusOrderByCreatedAtDesc(status);
        } else if (status == null) {
            hotelList = hotelRepository.findAllByTitle((HelperUtils.unAccent(hotelName)));
        } else {
            hotelList = hotelRepository.findAllByTitleAndApproveStatus(HelperUtils.unAccent(hotelName), status.name());
        }
        List<String> productCodes = hotelList.stream().map(Hotel::getCode).collect(Collectors.toList());
        List<Amenities> amenitiesList = amenitiesRepository.findAll();
        Map<Integer, Amenities> map = amenitiesList.stream().collect(Collectors.toMap(Amenities::getId, Function.identity()));

        List<Hotel> hotelListQuery = hotelRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(page, perPage));
        List<HotelDTO> hotels = MappingUtils.map(hotelListQuery, HotelDTO.class);
        hotels.forEach(hotel -> hotel.setFacilityDTOList(hotel.getAmenities()
                .stream().map(amenitiesId -> FacilityDTO.builder()
                        .id(amenitiesId.toString())
                        .name(map.get(amenitiesId).getName())
                        .icon(map.get(amenitiesId).getIcon())
                        .build())
                .collect(Collectors.toList())));

        List<HotelImage> hotelImages = hotelImageRepository.findUniqueImage(productCodes);
        Map<String, String> mapImage = hotelImages.stream().collect(Collectors.toMap(HotelImage::getHotelCode, HotelImage::getUrl));

        hotels.forEach(hotel -> {
            String imageUrl = mapImage.get(hotel.getCode());
            if (imageUrl != null) {
                hotel.setImage(imageUrl);
            }
        });

        Map<String, Integer> mapReturn = new HashMap<>();
        mapReturn.put("total", productCodes.size());
        return BaseResponse.ok(hotels, mapReturn);
    }

    public void approveActivities(ActivitiesApproveRequest activitiesApproveRequest) {
        Activities activities = activitiesRepository.findFirstByCode(activitiesApproveRequest.getActivitiesCode());
        activities.setApproveStatus(activitiesApproveRequest.getStatus());
        activitiesRepository.save(activities);
    }



    @Override
    public void approveRestaurant(RestaurantApproveRequest restaurantApproveRequest) {
        Restaurant restaurant  = restaurantRepository.findFirstByCode(restaurantApproveRequest.getRestaurantCode());
        restaurant.setApproveStatus(restaurantApproveRequest.getStatus());
        restaurantRepository.save(restaurant);
    }


    public BaseResponse getListActivities(String activitiesName, ApproveStatus status, Integer page, Integer perPage) {
        List<Activities> activitiesList;
        if (status == null && activitiesName.isEmpty()) {
            activitiesList = activitiesRepository.findAllHotel();
        } else if (activitiesName.isEmpty()) {
            activitiesList = activitiesRepository.findActivitiesByApproveStatusOrderByCreatedAtDesc(status);
        } else if (status == null) {
            activitiesList = activitiesRepository.findAllByTitle((HelperUtils.unAccent(activitiesName)));
        } else {
            activitiesList = activitiesRepository.findAllByTitleAndApproveStatus(HelperUtils.unAccent(activitiesName), status.name());
        }
        List<String> productCodes = activitiesList.stream().map(Activities::getCode).collect(Collectors.toList());
        List<ActivitiesGame> amenitiesList = activitiesGameRepository.findAll();
        Map<Integer, ActivitiesGame> map = amenitiesList.stream().collect(Collectors.toMap(ActivitiesGame::getId, Function.identity()));

        List<Activities> activitiesListQuery = activitiesRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(page, perPage));

        List<ActivitiesDTO> activitiesDTOList = MappingUtils.map(activitiesListQuery, ActivitiesDTO.class);
        List<ActivitiesImage> activitiesImages = activitiesImageRepository.findUniqueImage(productCodes);
        Map<String, String> mapImage = activitiesImages.stream().collect(Collectors.toMap(ActivitiesImage::getActivitiesCode, ActivitiesImage::getUrl));

        List<ActivitiesGame> activitiesGameList = activitiesGameRepository.findAllByActivitiesCodeIn(activitiesDTOList.stream().map(ActivitiesDTO::getCode).collect(Collectors.toList()));

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

        activitiesDTOList.forEach(activities -> {
            String imageUrl = mapImage.get(activities.getCode());
            if (imageUrl != null) {
                activities.setImage(imageUrl);
            }
            activities.setActivitiesGameDTOS(gameMaps.get(activities.getCode()));
        });

        Map<String, Integer> mapReturn = new HashMap<>();
        mapReturn.put("total", productCodes.size());
        return BaseResponse.ok(activitiesDTOList, mapReturn);
    }
    @Override
    public BaseResponse getListRestaurant(String restaurantName, ApproveStatus status, Integer page, Integer perPage) {
        List<Restaurant> restaurantList;
        if (status == null && restaurantName.isEmpty()) {
            restaurantList = restaurantRepository.findAllRestaurant();
        } else if (restaurantName.isEmpty()) {
            restaurantList = restaurantRepository.findRestaurantByApproveStatusOrderByCreatedAtDesc(status);
        } else if (status == null) {
            restaurantList = restaurantRepository.findAllByTitle((HelperUtils.unAccent(restaurantName)));
        } else {
            restaurantList = restaurantRepository.findAllByTitleAndApproveStatus(HelperUtils.unAccent(restaurantName), status.name());
        }
        List<String> productCodes = restaurantList.stream().map(Restaurant::getCode).collect(Collectors.toList());
        List<RestaurantMenu> menuList = restaurantMenuRepository.findAll();
        Map<Long, RestaurantMenu> map = menuList.stream().collect(Collectors.toMap(RestaurantMenu::getId, Function.identity()));



        List<Restaurant> restaurantListQuery = restaurantRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(page, perPage));
        List<RestaurantDTO> restaurantDTOList = MappingUtils.map(restaurantListQuery, RestaurantDTO.class);

//        List<RestaurantImage> restaurantImages =
        List<RestaurantImage> restaurantImages = restaurantImageRepository.findUniqueImage(productCodes);
        Map<String, String> mapImage = restaurantImages.stream().collect(Collectors.toMap(RestaurantImage::getRestaurantCode, RestaurantImage::getUrl));

        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAllByRestaurantCodeIn(restaurantDTOList.stream().map(RestaurantDTO::getCode).collect(Collectors.toList()));

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

        restaurantDTOList.forEach(restaurants -> {
            String imageUrl = mapImage.get(restaurants.getCode());
            if (imageUrl != null) {
                restaurants.setImage(imageUrl);
            }
            restaurants.setRestaurantMenuDTOList(menuMaps.get(restaurants.getCode()));

        });

        Map<String, Integer> mapReturn = new HashMap<>();
        mapReturn.put("total", productCodes.size());
        return BaseResponse.ok(restaurantDTOList, mapReturn);
    }

}
