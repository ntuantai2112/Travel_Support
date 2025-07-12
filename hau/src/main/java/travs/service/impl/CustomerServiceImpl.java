package travs.service.impl;

import travs.entity.restaurant.Restaurant;
import travs.entity.restaurant.RestaurantImage;
import travs.repository.HotelImageRepository;
import travs.repository.HotelRepository;
import travs.repository.activities.ActivitiesImageRepository;
import travs.repository.activities.ActivitiesRepository;
import travs.repository.restaurant.RestaurantImageRepository;
import travs.repository.restaurant.RestaurantRepository;
import travs.response.activities.ActivitiesDTO;
import travs.response.hotel.HotelDTO;
import travs.response.restaurant.RestaurantDTO;
import travs.utils.HelperUtils;
import travs.utils.MappingUtils;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesImage;
import travs.entity.hotel.Hotel;
import travs.entity.hotel.HotelImage;
import travs.service.CustomerService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private ActivitiesRepository activitiesRepository;
    private HotelRepository hotelRepository;
    private RestaurantRepository restaurantRepository;

    private RestaurantImageRepository restaurantImageRepository;
    private HotelImageRepository hotelImageRepository;
    private ActivitiesImageRepository activitiesImageRepository;


    @Override
    public List<RestaurantDTO> getListRestaurant(String title) {
        title = HelperUtils.unAccent(title);
        List<RestaurantDTO> restaurantDTOs;
        if (StringUtils.isBlank(title)) {
            List<Restaurant> restaurantList = restaurantRepository.findAllRestaurant();
            List<String> restaurantCodes = restaurantList.stream().map(Restaurant::getCode).collect(Collectors.toList());

            List<Restaurant> restaurantListQuery = restaurantRepository.findByCodeInOrderByCreatedAtDesc(restaurantCodes);

            restaurantDTOs = MappingUtils.map(restaurantListQuery, RestaurantDTO.class);
            List<RestaurantImage> restaurantImages = restaurantImageRepository.findUniqueImage(restaurantCodes);

            Map<String, String> mapImage = restaurantImages.stream().collect(Collectors.toMap(RestaurantImage::getRestaurantCode, RestaurantImage::getUrl));
            restaurantDTOs.forEach(restaurants -> {
                String imageUrl = mapImage.get(restaurants.getCode());
                if (imageUrl != null) {
                    restaurants.setImage(imageUrl);
                }
            });
        } else {
            List<Restaurant> restaurantList = restaurantRepository.findAllByTitleTsSearch(title);
            List<String> restaurantCodes = restaurantList.stream().map(Restaurant::getCode).collect(Collectors.toList());
            List<Restaurant> restaurantListQuery = restaurantRepository.findByCodeInOrderByCreatedAtDesc(restaurantCodes);
            restaurantDTOs = MappingUtils.map(restaurantListQuery, RestaurantDTO.class);
            List<RestaurantImage> restaurantImages = restaurantImageRepository.findUniqueImage(restaurantCodes);
            Map<String, String> mapImage = restaurantImages.stream().collect(Collectors.toMap(RestaurantImage::getRestaurantCode, RestaurantImage::getUrl));
            restaurantDTOs.forEach(restaurants -> {
                String imageUrl = mapImage.get(restaurants.getCode());
                if (imageUrl != null) {
                    restaurants.setImage(imageUrl);
                }
            });
        }

        return restaurantDTOs;
    }

    @Override
    public List<HotelDTO> getListHotel(String title) {
        title = HelperUtils.unAccent(title);
        List<HotelDTO> hotels;
        if (StringUtils.isBlank(title)) {
            List<Hotel> hotelList = hotelRepository.findAllHotel();
            List<String> productCodes = hotelList.stream().map(Hotel::getCode).collect(Collectors.toList());
            List<Hotel> hotelListQuery = hotelRepository.findByCodeInOrderByCreatedAtDesc(productCodes);
            hotels = MappingUtils.map(hotelListQuery, HotelDTO.class);
            List<HotelImage> hotelImages = hotelImageRepository.findUniqueImage(productCodes);
            Map<String, String> mapImage = hotelImages.stream().collect(Collectors.toMap(HotelImage::getHotelCode, HotelImage::getUrl));
            hotels.forEach(hotel -> {
                String imageUrl = mapImage.get(hotel.getCode());
                if (imageUrl != null) {
                    hotel.setImage(imageUrl);
                }
            });
        } else {
            List<Hotel> hotelList = hotelRepository.findAllByTitleAndTsSearch(title);
            List<String> productCodes = hotelList.stream().map(Hotel::getCode).collect(Collectors.toList());
            List<Hotel> hotelListQuery = hotelRepository.findByCodeInOrderByCreatedAtDesc(productCodes);
            hotels = MappingUtils.map(hotelListQuery, HotelDTO.class);
            List<HotelImage> hotelImages = hotelImageRepository.findUniqueImage(productCodes);
            Map<String, String> mapImage = hotelImages.stream().collect(Collectors.toMap(HotelImage::getHotelCode, HotelImage::getUrl));
            hotels.forEach(hotel -> {
                String imageUrl = mapImage.get(hotel.getCode());
                if (imageUrl != null) {
                    hotel.setImage(imageUrl);
                }
            });
        }
        return hotels;
    }

    @Override
    public List<ActivitiesDTO> getListActivities(String title) {
        title = HelperUtils.unAccent(title);
        List<ActivitiesDTO> activitiesDTOS;

        if (StringUtils.isBlank(title)) {
            List<Activities> activitiesList = activitiesRepository.findAllActivity();
            List<String> productCodes = activitiesList.stream().map(Activities::getCode).collect(Collectors.toList());
            List<Activities> activitiesListQuery = activitiesRepository.findByCodeInOrderByCreatedAtDesc(productCodes);
            activitiesDTOS = MappingUtils.map(activitiesListQuery, ActivitiesDTO.class);
            List<ActivitiesImage> activitiesImages = activitiesImageRepository.findUniqueImage(productCodes);
            Map<String, String> mapImage = activitiesImages.stream().collect(Collectors.toMap(ActivitiesImage::getActivitiesCode, ActivitiesImage::getUrl));

            activitiesDTOS.forEach(activities -> {
                String imageUrl = mapImage.get(activities.getCode());
                if (imageUrl != null) {
                    activities.setImage(imageUrl);
                }
            });
        } else {
            List<Activities> activitiesList = activitiesRepository.findAllByTitleTsSearch(title);
            List<String> productCodes = activitiesList.stream().map(Activities::getCode).collect(Collectors.toList());

            List<Activities> activitiesListQuery = activitiesRepository.findByCodeInOrderByCreatedAtDesc(productCodes);
            activitiesDTOS = MappingUtils.map(activitiesListQuery, ActivitiesDTO.class);

            List<ActivitiesImage> activitiesImages = activitiesImageRepository.findUniqueImage(productCodes);
            Map<String, String> mapImage = activitiesImages.stream().collect(Collectors.toMap(ActivitiesImage::getActivitiesCode, ActivitiesImage::getUrl));
            activitiesDTOS.forEach(activities -> {
                String imageUrl = mapImage.get(activities.getCode());
                if (imageUrl != null) {
                    activities.setImage(imageUrl);
                }
            });
        }
        return activitiesDTOS;
    }
}
