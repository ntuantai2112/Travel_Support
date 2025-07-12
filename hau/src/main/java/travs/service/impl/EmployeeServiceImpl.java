package travs.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import travs.common.type.ApproveStatus;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesGame;
import travs.entity.activities.ActivitiesImage;
import travs.entity.hotel.Amenities;
import travs.entity.hotel.Hotel;
import travs.entity.hotel.HotelImage;
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
import travs.request.activities.ActivitiesApproveRequest;
import travs.request.hotel.HotelApproveRequest;
import travs.request.restaurant.RestaurantApproveRequest;
import travs.response.BaseResponse;
import travs.response.activities.ActivitiesDTO;
import travs.response.activities.ActivitiesGameDTO;
import travs.response.hotel.FacilityDTO;
import travs.response.hotel.HotelDTO;
import travs.response.restaurant.RestaurantDTO;
import travs.response.restaurant.RestaurantMenuDTO;
import travs.service.EmployeeService;
import travs.utils.HelperUtils;
import travs.utils.MappingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
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


    @Override
    public void approveHotel(HotelApproveRequest hotelApproveRequest) {
        Hotel hotel = hotelRepository.findFirstByCode(hotelApproveRequest.getHotelCode());
        hotel.setApproveStatus(hotelApproveRequest.getStatus());
        hotelRepository.save(hotel);
        log.info("Update Hotel status approval successful with code:{}", hotelApproveRequest.getHotelCode());
    }

    @Override
    public BaseResponse<List<HotelDTO>, Map<String, Integer>> getListHotel(String hotelName, ApproveStatus status, Integer page, Integer perPage) {
        List<Hotel> hotelList = getFilteredHotels(hotelName, status);
        List<String> productCodes = extractCodes(hotelList, Hotel::getCode);

        List<Hotel> hotelListQuery = hotelRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(page, perPage));
        List<HotelDTO> hotels = MappingUtils.map(hotelListQuery, HotelDTO.class);

        enrichWithAmenities(hotels);
        enrichWithImage(hotels, productCodes);

        Map<String, Integer> mapReturn = getTotalPage(productCodes);
        log.info("Search List Hotel by name and status of employee successfully!");
        return BaseResponse.ok(hotels, mapReturn);
    }

    @Override
    public void approveActivities(ActivitiesApproveRequest activitiesApproveRequest) {
        Activities activities = activitiesRepository.findFirstByCode(activitiesApproveRequest.getActivitiesCode());
        activities.setApproveStatus(activitiesApproveRequest.getStatus());
        activitiesRepository.save(activities);
        log.info("Update Activity status approval successful with code:{}", activitiesApproveRequest.getActivitiesCode());
    }


    @Override
    public void approveRestaurant(RestaurantApproveRequest restaurantApproveRequest) {
        Restaurant restaurant = restaurantRepository.findFirstByCode(restaurantApproveRequest.getRestaurantCode());
        restaurant.setApproveStatus(restaurantApproveRequest.getStatus());
        restaurantRepository.save(restaurant);
        log.info("Update Restaurant status approval successful with code:{}", restaurantApproveRequest.getRestaurantCode());
    }

    @Override
    public BaseResponse<List<ActivitiesDTO>, Map<String, Integer>> getListActivities(String activitiesName, ApproveStatus status, Integer page, Integer perPage) {
        List<Activities> activitiesList = filterActivitiesByTitleAndStatus(activitiesName, status);
        List<String> productCodes = extractCodes(activitiesList, Activities::getCode);
        List<Activities> activitiesListQuery = activitiesRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(page, perPage));
        List<ActivitiesDTO> activitiesDTOList = MappingUtils.map(activitiesListQuery, ActivitiesDTO.class);

        List<ActivitiesImage> activitiesImages = activitiesImageRepository.findUniqueImage(productCodes);
        Map<String, String> mapImage = activitiesImages.stream().collect(Collectors.toMap(ActivitiesImage::getActivitiesCode, ActivitiesImage::getUrl));
        Map<String, List<ActivitiesGameDTO>> gameMaps = getGameMap(activitiesDTOList);
        attachImageAndGame(activitiesDTOList, mapImage, gameMaps);
        Map<String, Integer> mapReturn = getTotalPage(productCodes);
        log.info("Search List Activities by name and status of employee successfully!");
        return BaseResponse.ok(activitiesDTOList, mapReturn);
    }


    @Override
    public BaseResponse<List<RestaurantDTO>, Map<String, Integer>> getListRestaurant(String restaurantName, ApproveStatus status, Integer page, Integer perPage) {
        List<Restaurant> restaurantList = filterRestaurantByTitleAndStatus(restaurantName, status);
        List<String> productCodes = extractCodes(restaurantList, Restaurant::getCode);
        List<Restaurant> restaurantListQuery = restaurantRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(page, perPage));
        List<RestaurantDTO> restaurantDTOList = MappingUtils.map(restaurantListQuery, RestaurantDTO.class);

        List<RestaurantImage> restaurantImages = restaurantImageRepository.findUniqueImage(productCodes);
        Map<String, String> mapImage = restaurantImages.stream().collect(Collectors.toMap(RestaurantImage::getRestaurantCode, RestaurantImage::getUrl));

        Map<String, List<RestaurantMenuDTO>> menuMaps = groupMenuByRestaurantCode(restaurantDTOList);

        restaurantDTOList.forEach(restaurant -> {
            String imageUrl = mapImage.get(restaurant.getCode());
            if (imageUrl != null) {
                restaurant.setImage(imageUrl);
            }
            restaurant.setRestaurantMenuDTOList(menuMaps.get(restaurant.getCode()));

        });

        Map<String, Integer> mapReturn = getTotalPage(productCodes);
        log.info("Search List Restaurant by name and status of employee successfully!");
        return BaseResponse.ok(restaurantDTOList, mapReturn);
    }

    // Xử lý Hotel
    private List<Hotel> getFilteredHotels(String hotelName, ApproveStatus status) {
        boolean isNameEmpty = (hotelName == null || hotelName.trim().isEmpty());
        if (status == null && isNameEmpty) {
            return hotelRepository.findAllHotel();
        }
        if (isNameEmpty) {
            return hotelRepository.findHotelByApproveStatusOrderByCreatedAtDesc(status);
        }
        String normalizedName = HelperUtils.unAccent(hotelName);
        if (status == null) {
            return hotelRepository.findAllByTitle(normalizedName);
        }
        return hotelRepository.findAllByTitleAndApproveStatus(normalizedName, status.name());
    }


    private void enrichWithAmenities(List<HotelDTO> hotelDTOs) {
        Map<Integer, Amenities> amenitiesMap = amenitiesRepository.findAll()
                .stream().collect(Collectors.toMap(Amenities::getId, Function.identity()));

        for (HotelDTO hotel : hotelDTOs) {
            List<FacilityDTO> facilities = hotel.getAmenities().stream()
                    .map(id -> {
                        Amenities amenities = amenitiesMap.get(id);
                        return FacilityDTO.builder()
                                .id(String.valueOf(id))
                                .name(amenities.getName())
                                .icon(amenities.getIcon())
                                .build();
                    })
                    .collect(Collectors.toList());
            hotel.setFacilityDTOList(facilities);
        }
    }

    private void enrichWithImage(List<HotelDTO> hotelDTOs, List<String> codes) {
        Map<String, String> imageMap = hotelImageRepository.findUniqueImage(codes)
                .stream().collect(Collectors.toMap(HotelImage::getHotelCode, HotelImage::getUrl));

        hotelDTOs.forEach(hotel -> hotel.setImage(imageMap.get(hotel.getCode())));
    }

    private Map<String, Integer> getTotalPage(List<String> productCodes) {
        Map<String, Integer> mapReturn = new HashMap<>();
        mapReturn.put("total", productCodes.size());
        return mapReturn;
    }

    // Hàm lấy ra Code theo từng đối tượng
    private <T> List<String> extractCodes(List<T> list, Function<T, String> codeExtractor) {
        return list.stream().map(codeExtractor).collect(Collectors.toList());
    }

    // lấy danh sách Activities
    private List<Activities> filterActivitiesByTitleAndStatus(String title, ApproveStatus status) {
        if (status == null && title.isEmpty()) return activitiesRepository.findAllActivity();
        if (title.isEmpty()) return activitiesRepository.findActivitiesByApproveStatusOrderByCreatedAtDesc(status);
        String unAccent = HelperUtils.unAccent(title);
        if (status == null) return activitiesRepository.findAllByTitle(unAccent);
        return activitiesRepository.findAllByTitleAndApproveStatus(unAccent, status.name());
    }

    // xử lý ActivitiesGame và trả về kiểu Map theo giá trị tương ứng.
    // Lấy ActivitiesGame theo code:
    private Map<String, List<ActivitiesGameDTO>> getGameMap(List<ActivitiesDTO> activitiesDTOList) {
        List<String> activitiesCodeDTO = activitiesDTOList.stream().map(ActivitiesDTO::getCode).collect(Collectors.toList());
        List<ActivitiesGame> activitiesGameList = activitiesGameRepository.findAllByActivitiesCodeIn(activitiesCodeDTO);

        Map<String, List<ActivitiesGameDTO>> gameMaps = new HashMap<>();

        activitiesGameList.forEach(activitiesGame -> {
            ActivitiesGameDTO gameDTO = MappingUtils.map(activitiesGame, ActivitiesGameDTO.class);
            gameMaps.computeIfAbsent(activitiesGame.getActivitiesCode(), k -> new ArrayList<>()).add(gameDTO);
        });
        return gameMaps;
    }

    // Set giá trị cho ActivitiesDTO
    private void attachImageAndGame(List<ActivitiesDTO> dtoList, Map<String, String> imageMap, Map<String, List<ActivitiesGameDTO>> gameMap) {
        dtoList.forEach(dto -> {
            String imageURL = imageMap.get(dto.getCode());
            if (imageURL != null) {
                dto.setImage(imageURL);

            }
            dto.setActivitiesGameDTOS(gameMap.getOrDefault(dto.getCode(), new ArrayList<>()));
        });
    }

    // Xử lý Filter Restaurant
    private List<Restaurant> filterRestaurantByTitleAndStatus(String title, ApproveStatus status) {
        if (status == null && title.isEmpty()) return restaurantRepository.findAllRestaurant();
        if (title.isEmpty()) return restaurantRepository.findRestaurantByApproveStatusOrderByCreatedAtDesc(status);
        String unAccent = HelperUtils.unAccent(title);
        if (status == null) return restaurantRepository.findAllByTitle(unAccent);
        return restaurantRepository.findAllByTitleAndApproveStatus(unAccent, status.name());
    }

    //   xử lý RestaurantMenu và trả về kiểu Map theo giá trị tương ứng.
    //   Lấy RestaurantMenu theo code:
    private Map<String, List<RestaurantMenuDTO>> groupMenuByRestaurantCode(List<RestaurantDTO> restaurantDTOList) {
        List<String> restaurantDTOCodes =  restaurantDTOList.stream().map(RestaurantDTO::getCode).collect(Collectors.toList());
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAllByRestaurantCodeIn(restaurantDTOCodes);
        Map<String, List<RestaurantMenuDTO>> menuMaps = new HashMap<>();

        restaurantMenuList.forEach(restaurantMenu -> {
            RestaurantMenuDTO menuDTO = MappingUtils.map(restaurantMenu, RestaurantMenuDTO.class);
            menuMaps.computeIfAbsent(restaurantMenu.getRestaurantCode(), k -> new ArrayList<>()).add(menuDTO);
        });

        return menuMaps;
    }


}
