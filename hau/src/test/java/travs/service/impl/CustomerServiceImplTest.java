package travs.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.common.ActivitiesType;
import travs.common.HotelType;
import travs.common.RestaurantType;
import travs.common.type.ApproveStatus;
import travs.common.type.ImageType;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesImage;
import travs.entity.hotel.Hotel;
import travs.entity.hotel.HotelImage;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private ActivitiesRepository mockActivitiesRepository;
    @Mock
    private HotelRepository mockHotelRepository;
    @Mock
    private RestaurantRepository mockRestaurantRepository;
    @Mock
    private RestaurantImageRepository mockRestaurantImageRepository;
    @Mock
    private HotelImageRepository mockHotelImageRepository;
    @Mock
    private ActivitiesImageRepository mockActivitiesImageRepository;

    @InjectMocks
    private CustomerServiceImpl customerServiceImplUnderTest;

    @Test
    void testGetListRestaurant_Success_Test() {
        // Setup
        // Configure RestaurantRepository.findByCodeInOrderByCreatedAtDesc(...).
        final List<Restaurant> restaurants = List.of(
                new Restaurant("id", "restaurantCode", "title", "description", "slug", RestaurantType.NHA_HANG, new Integer[]{0},
                        "address", "userId", ApproveStatus.PENDING));
        when(mockRestaurantRepository.findByCodeInOrderByCreatedAtDesc(any())).thenReturn(restaurants);

        // Configure RestaurantImageRepository.findUniqueImage(...).
        final List<RestaurantImage> restaurantImages = List.of(
                new RestaurantImage(0L, "restaurantCode", "caption", "url", ImageType.ROOM));
        when(mockRestaurantImageRepository.findUniqueImage(any())).thenReturn(restaurantImages);
        when(mockRestaurantRepository.findAllByTitleTsSearch(any())).thenReturn(restaurants);

        // Run the test
        final List<RestaurantDTO> result = customerServiceImplUnderTest.getListRestaurant("title");

        // Verify the results
        assertThat(result).isNotNull();
    }

    @Test
    void testGetListRestaurant_IsBlank_Success_Test() {
        // Setup
        // Configure RestaurantRepository.findByCodeInOrderByCreatedAtDesc(...).
        final List<Restaurant> restaurants = List.of(
                new Restaurant("id", "restaurantCode", "title", "description", "slug", RestaurantType.NHA_HANG, new Integer[]{0},
                        "address", "userId", ApproveStatus.PENDING));
        when(mockRestaurantRepository.findByCodeInOrderByCreatedAtDesc(any())).thenReturn(restaurants);

        // Configure RestaurantImageRepository.findUniqueImage(...).
        final List<RestaurantImage> restaurantImages = List.of(
                new RestaurantImage(0L, "restaurantCode", "caption", "url", ImageType.ROOM));
        when(mockRestaurantImageRepository.findUniqueImage(any())).thenReturn(restaurantImages);

        // Run the test
        final List<RestaurantDTO> result = customerServiceImplUnderTest.getListRestaurant("");

        // Verify the results
        assertThat(result).isNotNull();
    }

    @Test
    void testGetListHotel_Success_Test() {
        // Configure HotelRepository.findByCodeInOrderByCreatedAtDesc(...).
        final List<Hotel> hotels = List.of(
                new Hotel("id", "hotelCode", "title", "description", "slug", HotelType.POPULAR, 0.0, 0, new Integer[]{0},
                        0.0, "address", "userId", ApproveStatus.PENDING));
        when(mockHotelRepository.findByCodeInOrderByCreatedAtDesc(any())).thenReturn(hotels);

        // Configure HotelImageRepository.findUniqueImage(...).
        final List<HotelImage> hotelImages = List.of(new HotelImage(0L, "hotelCode", "caption", "url", ImageType.ROOM));
        when(mockHotelImageRepository.findUniqueImage(any())).thenReturn(hotelImages);
        when(mockHotelRepository.findAllByTitleAndTsSearch(any())).thenReturn(hotels);

        // Run the test
        final List<HotelDTO> result = customerServiceImplUnderTest.getListHotel("title");

        // Verify the results
        assertThat(result).isNotNull();
    }

    @Test
    void testGetListHotel_IsBlank_Success_Test() {
        // Configure HotelRepository.findByCodeInOrderByCreatedAtDesc(...).
        final List<Hotel> hotels = List.of(
                new Hotel("id", "hotelCode", "title", "description", "slug", HotelType.POPULAR, 0.0, 0, new Integer[]{0},
                        0.0, "address", "userId", ApproveStatus.PENDING));
        when(mockHotelRepository.findByCodeInOrderByCreatedAtDesc(any())).thenReturn(hotels);

        // Configure HotelImageRepository.findUniqueImage(...).
        final List<HotelImage> hotelImages = List.of(new HotelImage(0L, "hotelCode", "caption", "url", ImageType.ROOM));
        when(mockHotelImageRepository.findUniqueImage(any())).thenReturn(hotelImages);

        // Run the test
        final List<HotelDTO> result = customerServiceImplUnderTest.getListHotel("");

        // Verify the results
        assertThat(result).isNotNull();
    }

    @Test
    void testGetListActivities_Success_Test() {
        // Configure ActivitiesRepository.findAllHotel(...).
        final List<Activities> activities = List.of(
                new Activities("id", "activitiesCode", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0,
                        "address", "userId", ApproveStatus.PENDING));
        when(mockActivitiesRepository.findByCodeInOrderByCreatedAtDesc(any())).thenReturn(activities);

        // Configure ActivitiesImageRepository.findUniqueImage(...).
        final List<ActivitiesImage> activitiesImages = List.of(
                new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
        when(mockActivitiesImageRepository.findUniqueImage(any())).thenReturn(activitiesImages);
        when(mockActivitiesRepository.findAllByTitleTsSearch(any())).thenReturn(activities);

        // Run the test
        final List<ActivitiesDTO> result = customerServiceImplUnderTest.getListActivities("title");

        // Verify the results
        assertThat(result).isNotNull();
    }
    @Test
    void testGetListActivities_IsBlank_Success_Test() {
        // Configure ActivitiesRepository.findAllHotel(...).
        final List<Activities> activities = List.of(
                new Activities("id", "activitiesCode", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0,
                        "address", "userId", ApproveStatus.PENDING));
        when(mockActivitiesRepository.findByCodeInOrderByCreatedAtDesc(any())).thenReturn(activities);

        // Configure ActivitiesImageRepository.findUniqueImage(...).
        final List<ActivitiesImage> activitiesImages = List.of(
                new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
        when(mockActivitiesImageRepository.findUniqueImage(any())).thenReturn(activitiesImages);

        // Run the test
        final List<ActivitiesDTO> result = customerServiceImplUnderTest.getListActivities("");

        // Verify the results
        assertThat(result).isNotNull();
    }
}
