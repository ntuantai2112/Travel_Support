package travs.service.impl;

import org.assertj.core.api.Assertions;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private HotelRepository mockHotelRepository;
    @Mock
    private AmenitiesRepository mockAmenitiesRepository;
    @Mock
    private HotelImageRepository mockHotelImageRepository;
    @Mock
    private ActivitiesRepository mockActivitiesRepository;
    @Mock
    private ActivitiesGameRepository mockActivitiesGameRepository;
    @Mock
    private ActivitiesImageRepository mockActivitiesImageRepository;
    @Mock
    private RestaurantRepository mockRestaurantRepository;
    @Mock
    private RestaurantMenuRepository mockRestaurantMenuRepository;
    @Mock
    private RestaurantImageRepository mockRestaurantImageRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImplUnderTest;

    @Test
    void testApproveHotel_Success_Test() {
        // Setup
        final HotelApproveRequest hotelApproveRequest = new HotelApproveRequest("hotelCode", ApproveStatus.PENDING);

        // Configure HotelRepository.findFirstByCode(...).
        final Hotel hotel = new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0,
                new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING);
        when(mockHotelRepository.findFirstByCode("hotelCode")).thenReturn(hotel);

        // Configure HotelRepository.save(...).
        final Hotel hotel1 = new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0,
                new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING);
        when(mockHotelRepository.save(any(Hotel.class))).thenReturn(hotel1);

        // Run the test
        employeeServiceImplUnderTest.approveHotel(hotelApproveRequest);

        // Verify the results
        verify(mockHotelRepository).save(any(Hotel.class));
    }

    @Test
    void testGetListHotel_Success_Test() {
        // Setup
        // Configure HotelRepository.findAllHotel(...).
        final List<Hotel> hotels = List.of(
                new Hotel("id", "hotelCode", "title", "description", "slug", HotelType.POPULAR, 0.0, 0, new Integer[]{0},
                        0.0, "address", "userId", ApproveStatus.PENDING));
        when(mockHotelRepository.findAllHotel()).thenReturn(hotels);

        // Configure AmenitiesRepository.findAll(...).
        final List<Amenities> amenities = List.of(new Amenities(0, "name", "icon"));
        when(mockAmenitiesRepository.findAll()).thenReturn(amenities);
        when(mockHotelRepository.findByCodeInOrderByCreatedAtDesc(any(), any())).thenReturn(hotels);

        // Configure HotelImageRepository.findUniqueImage(...).
        final List<HotelImage> hotelImages = List.of(new HotelImage(0L, "hotelCode", "caption", "url", ImageType.ROOM));
        when(mockHotelImageRepository.findUniqueImage(any())).thenReturn(hotelImages);

        // Run the test
        final BaseResponse resultStatusAndHotelName = employeeServiceImplUnderTest.getListHotel("", null, 2, 55);
        final BaseResponse resultHotelNameIsEmpty = employeeServiceImplUnderTest.getListHotel("", ApproveStatus.PENDING, 2, 55);
        final BaseResponse resultStatus = employeeServiceImplUnderTest.getListHotel("hotelName", null, 2, 55);
        final BaseResponse result = employeeServiceImplUnderTest.getListHotel("hotelName", ApproveStatus.PENDING, 2, 55);
        Assertions.assertThat(resultStatusAndHotelName).isNotNull();
        Assertions.assertThat(resultHotelNameIsEmpty).isNotNull();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(resultStatus).isNotNull();
    }

    @Test
    void testApproveActivities_Success_Test() {
        // Setup
        final ActivitiesApproveRequest activitiesApproveRequest = new ActivitiesApproveRequest("activitiesCode",
                ApproveStatus.PENDING);

        // Configure ActivitiesRepository.findFirstByCode(...).
        final Activities activities = new Activities("id", "code", "title", "description", "slug",
                ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0, "address", "userId", ApproveStatus.PENDING);
        when(mockActivitiesRepository.findFirstByCode("activitiesCode")).thenReturn(activities);
        when(mockActivitiesRepository.save(any(Activities.class))).thenReturn(activities);

        // Run the test
        employeeServiceImplUnderTest.approveActivities(activitiesApproveRequest);

        // Verify the results
        verify(mockActivitiesRepository).save(any(Activities.class));
    }

    @Test
    void testApproveRestaurant_Success_Test() {
        // Setup
        final RestaurantApproveRequest restaurantApproveRequest = new RestaurantApproveRequest("restaurantCode",
                ApproveStatus.PENDING);
        // Configure RestaurantRepository.findFirstByCode(...).
        final Restaurant restaurant = new Restaurant("id", "code", "title", "description", "slug",
                RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING);
        when(mockRestaurantRepository.findFirstByCode("restaurantCode")).thenReturn(restaurant);
        when(mockRestaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Run the test
        employeeServiceImplUnderTest.approveRestaurant(restaurantApproveRequest);

        // Verify the results
        verify(mockRestaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testGetListActivities_Success_Test() {
        // Setup
        // Configure ActivitiesRepository.findAllHotel(...).
        final List<Activities> activities = List.of(
                new Activities("id", "activitiesCode", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0,
                        "address", "userId", ApproveStatus.PENDING));
        when(mockActivitiesRepository.findAllByTitleAndApproveStatus(any(), any())).thenReturn(activities);

        // Configure ActivitiesGameRepository.findAll(...).
        final List<ActivitiesGame> activitiesGames = List.of(
                new ActivitiesGame(0, "activitiesCode", "name", "image", "description"));
        when(mockActivitiesGameRepository.findAll()).thenReturn(activitiesGames);
        when(mockActivitiesRepository.findByCodeInOrderByCreatedAtDesc(any(),any())).thenReturn(activities);

        // Configure ActivitiesImageRepository.findUniqueImage(...).
        final List<ActivitiesImage> activitiesImages = List.of(
                new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
        when(mockActivitiesImageRepository.findUniqueImage(any())).thenReturn(activitiesImages);
        when(mockActivitiesGameRepository.findAllByActivitiesCodeIn(any())).thenReturn(activitiesGames);

        // Run the test
        final BaseResponse result = employeeServiceImplUnderTest.getListActivities("activitiesName", ApproveStatus.PENDING, 2, 55);
        final BaseResponse resultStatus = employeeServiceImplUnderTest.getListActivities("activitiesName", null, 2, 55);
        final BaseResponse resulTactivitiesName = employeeServiceImplUnderTest.getListActivities("", ApproveStatus.PENDING, 2, 55);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(resultStatus).isNotNull();
        Assertions.assertThat(resulTactivitiesName).isNotNull();
    }

    @Test
    void testGetListActivities_Status_Success_Test() {
        // Setup
        // Configure ActivitiesRepository.findAllHotel(...).
        final List<Activities> activities = List.of(
                new Activities("id", "activitiesCode", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0,
                        "address", "userId", ApproveStatus.PENDING));

        // Configure ActivitiesGameRepository.findAll(...).
        final List<ActivitiesGame> activitiesGames = List.of(
                new ActivitiesGame(0, "activitiesCode", "name", "image", "description"),
                new ActivitiesGame(1, "activitiesCode", "name", "image", "description"));
        when(mockActivitiesGameRepository.findAll()).thenReturn(activitiesGames);
        when(mockActivitiesRepository.findByCodeInOrderByCreatedAtDesc(any(),any())).thenReturn(activities);

        // Configure ActivitiesImageRepository.findUniqueImage(...).
        final List<ActivitiesImage> activitiesImages = List.of(
                new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
        when(mockActivitiesImageRepository.findUniqueImage(any())).thenReturn(activitiesImages);
        when(mockActivitiesGameRepository.findAllByActivitiesCodeIn(any())).thenReturn(activitiesGames);

        // Run the test
        final BaseResponse result = employeeServiceImplUnderTest.getListActivities("", null, 2, 55);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void testGetListRestaurant_Success_Test() {
        // Setup
        // Configure RestaurantRepository.findAllRestaurant(...).
        final List<Restaurant> restaurants = List.of(
                new Restaurant("id", "restaurantCode", "title", "description", "slug", RestaurantType.NHA_HANG, new Integer[]{0},
                        "address", "userId", ApproveStatus.PENDING));

        when(mockRestaurantRepository.findRestaurantByApproveStatusOrderByCreatedAtDesc(
                ApproveStatus.PENDING)).thenReturn(restaurants);
        when(mockRestaurantRepository.findAllByTitle(any())).thenReturn(restaurants);
        when(mockRestaurantRepository.findAllByTitleAndApproveStatus(any(), any())).thenReturn(restaurants);

        // Configure RestaurantMenuRepository.findAll(...).
        final List<RestaurantMenu> restaurantMenus = List.of(
                new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description", "name", false, "image"),
                new RestaurantMenu(1L, "restaurantCode", 0.0, "currency", "description", "name", false, "image"));
        when(mockRestaurantMenuRepository.findAll()).thenReturn(restaurantMenus);
        when(mockRestaurantRepository.findByCodeInOrderByCreatedAtDesc(any(),any())).thenReturn(restaurants);

        // Configure RestaurantImageRepository.findUniqueImage(...).
        final List<RestaurantImage> restaurantImages = List.of(
                new RestaurantImage(0L, "restaurantCode", "caption", "url", ImageType.ROOM));
        when(mockRestaurantImageRepository.findUniqueImage(any())).thenReturn(restaurantImages);
        when(mockRestaurantMenuRepository.findAllByRestaurantCodeIn(any())).thenReturn(restaurantMenus);

        // Run the test
        final BaseResponse result = employeeServiceImplUnderTest.getListRestaurant("restaurantName", ApproveStatus.PENDING, 2, 55);
        final BaseResponse resultStatus = employeeServiceImplUnderTest.getListRestaurant("restaurantName", null, 2, 55);
        final BaseResponse resulTrestaurantName = employeeServiceImplUnderTest.getListRestaurant("", ApproveStatus.PENDING, 2, 55);
        final BaseResponse resulTrestaurantNameAndStatus = employeeServiceImplUnderTest.getListRestaurant("", null, 2, 55);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(resulTrestaurantNameAndStatus).isNotNull();
        Assertions.assertThat(resultStatus).isNotNull();
        Assertions.assertThat(resulTrestaurantName).isNotNull();
    }
}
