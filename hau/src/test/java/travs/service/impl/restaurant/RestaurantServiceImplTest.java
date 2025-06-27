package travs.service.impl.restaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.common.RestaurantType;
import travs.common.type.ApproveStatus;
import travs.common.type.ImageType;
import travs.entity.restaurant.Feature;
import travs.entity.restaurant.Restaurant;
import travs.entity.restaurant.RestaurantImage;
import travs.entity.restaurant.RestaurantMenu;
import travs.exception.GeneralException;
import travs.exception.ResourceNotFoundException;
import travs.repository.restaurant.FeatureRepository;
import travs.repository.restaurant.RestaurantImageRepository;
import travs.repository.restaurant.RestaurantMenuRepository;
import travs.repository.restaurant.RestaurantRepository;
import travs.request.restaurant.RestaurantMenuUploadBody;
import travs.request.restaurant.RestaurantUploadRequest;
import travs.response.BaseResponse;
import travs.service.impl.HelperService;
import travs.utils.AuthenticationUtils;
import travs.utils.FileStore;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private HelperService mockHelperService;
    @Mock
    private RestaurantRepository mockRestaurantRepository;
    @Mock
    private RestaurantImageRepository mockRestaurantImageRepository;
    @Mock
    private RestaurantMenuRepository mockRestaurantMenuRepository;
    @Mock
    private FeatureRepository mockFeatureRepository;
    @InjectMocks
    private RestaurantServiceImpl restaurantServiceImplUnderTest;

    @Test
    void testGet_Success_Test() {
        // Setup
        // Configure RestaurantRepository.findFirstBySlug(...).
        final Optional<Restaurant> restaurant = Optional.of(
                new Restaurant("id", "code", "title", "description", "slug", RestaurantType.NHA_HANG, new Integer[]{0},
                        "address", "userId", ApproveStatus.PENDING));
        when(mockRestaurantRepository.findFirstBySlug("slug")).thenReturn(restaurant);

        when(mockFeatureRepository.findAll()).thenReturn(List.of(new Feature(0, "name")));

        // Configure RestaurantMenuRepository.findAllByRestaurantCode(...).
        final List<RestaurantMenu> restaurantMenus = List.of(
                new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description", "name", false, "image"));
        when(mockRestaurantMenuRepository.findAllByRestaurantCode("code")).thenReturn(restaurantMenus);

        when(mockHelperService.checkFavorite("slug")).thenReturn(false);

        // Configure RestaurantImageRepository.findAllImageByRestaurantCode(...).
        final List<RestaurantImage> restaurantImages = List.of(
                new RestaurantImage(0L, "restaurantCode", "caption", "url", ImageType.ROOM));
        when(mockRestaurantImageRepository.findAllImageByRestaurantCode("code")).thenReturn(restaurantImages);

        // Run the test
        final BaseResponse result = restaurantServiceImplUnderTest.get("slug");
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void testGet_RestaurantRepositoryReturnsAbsent() {
        // Setup
        when(mockRestaurantRepository.findFirstBySlug("slug")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> restaurantServiceImplUnderTest.get("slug"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testUploadRestaurant_Success_Test() {
        // Setup
        try (MockedStatic<FileStore> mockedStatic = mockStatic(FileStore.class)){
            final RestaurantUploadRequest restaurantUploadRequest = new RestaurantUploadRequest(new Integer[]{0},
                    "description", RestaurantType.NHA_HANG, "address", "title", List.of("value"), "restaurantCode",
                    List.of());

            // Configure RestaurantImageRepository.saveAll(...).
            final List<RestaurantImage> restaurantImages = List.of(
                    new RestaurantImage(0L, "restaurantCode", "caption", "url", ImageType.ROOM));
            final  List<String> images = List.of("value","value1","value2");
            mockedStatic.when(() -> FileStore.getFilePaths(any(), any())).thenReturn(images);
            when(mockRestaurantImageRepository.saveAll(any())).thenReturn(restaurantImages);

            // Configure RestaurantRepository.save(...).
            final Restaurant restaurant = new Restaurant("id", "code", "title", "description", "slug",
                    RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING);
            when(mockRestaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

            // Run the test
            final BaseResponse result = restaurantServiceImplUnderTest.uploadRestaurant(restaurantUploadRequest);

            // Verify the results
            verify(mockRestaurantImageRepository).saveAll(any());
            verify(mockRestaurantRepository).updateSearchVector("code");
            assertThat(result).isNotNull();
        }

    }

    @Test
    void testUpdateRestaurant_Success_Test() {
        // Setup
        try (MockedStatic<FileStore> mockedStatic = mockStatic(FileStore.class)){
            final RestaurantUploadRequest restaurantUploadRequest = new RestaurantUploadRequest(new Integer[]{0},
                    "description", RestaurantType.NHA_HANG, "address", "title", List.of("value"), "restaurantCode",
                    List.of());

            // Configure RestaurantRepository.findFirstByCode(...).
            final Restaurant restaurant = new Restaurant("id", "code", "title", "description", "slug",
                    RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING);
            final  List<String> images = List.of("value","value1","value2");
            mockedStatic.when(() -> FileStore.getFilePaths(any(), any())).thenReturn(images);
            when(mockRestaurantRepository.findFirstByCode("restaurantCode")).thenReturn(restaurant);

            // Configure RestaurantImageRepository.saveAll(...).
            final List<RestaurantImage> restaurantImages = List.of(
                    new RestaurantImage(0L, "restaurantCode", "caption", "url", ImageType.ROOM));
            when(mockRestaurantImageRepository.saveAll(any())).thenReturn(restaurantImages);

            // Configure RestaurantRepository.save(...).
            final Restaurant restaurant1 = new Restaurant("id", "code", "title", "description", "slug",
                    RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING);
            when(mockRestaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant1);

            // Run the test
            final BaseResponse result = restaurantServiceImplUnderTest.updateRestaurant(restaurantUploadRequest);

            // Verify the results
            verify(mockRestaurantImageRepository).saveAll(any());
            verify(mockRestaurantRepository).updateSearchVector("code");
            assertThat(result).isNotNull();
        }


    }

    @Test
    void testUpdateRestaurant_RestaurantRepositoryFindFirstByCodeReturnsNull() {
        // Setup
        final RestaurantUploadRequest restaurantUploadRequest = new RestaurantUploadRequest(new Integer[]{0},
                "description", RestaurantType.NHA_HANG, "address", "title", List.of("value"), "restaurantCode",
                List.of());
        when(mockRestaurantRepository.findFirstByCode("restaurantCode")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> restaurantServiceImplUnderTest.updateRestaurant(restaurantUploadRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testUploadRestaurantMenu() {
        // Setup
        final RestaurantMenuUploadBody restaurantMenuUploadBody = new RestaurantMenuUploadBody(0L, "restaurantCode",
                "currency", 0.0, "image", "name", "description", null);

        // Configure RestaurantMenuRepository.save(...).
        final RestaurantMenu restaurantMenu = new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description",
                "name", false, "image");
        when(mockRestaurantMenuRepository.save(any(RestaurantMenu.class))).thenReturn(restaurantMenu);

        // Run the test
        restaurantServiceImplUnderTest.uploadRestaurantMenu(restaurantMenuUploadBody);

        // Verify the results
        verify(mockRestaurantMenuRepository).save(any(RestaurantMenu.class));
    }

    @Test
    void testUpdateRestaurantMenu() {
        // Setup
        final RestaurantMenuUploadBody restaurantMenuUploadBody = new RestaurantMenuUploadBody(0L, "restaurantCode",
                "currency", 0.0, "image", "name", "description", null);

        // Configure RestaurantMenuRepository.findFirstById(...).
        final RestaurantMenu restaurantMenu = new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description",
                "name", false, "image");
        when(mockRestaurantMenuRepository.findFirstById(0L)).thenReturn(restaurantMenu);

        // Configure RestaurantImageRepository.save(...).
        final RestaurantImage restaurantImage = new RestaurantImage(0L, "restaurantCode", "caption", "url",
                ImageType.ROOM);
        when(mockRestaurantImageRepository.save(any(RestaurantImage.class))).thenReturn(restaurantImage);

        // Configure RestaurantMenuRepository.save(...).
        final RestaurantMenu restaurantMenu1 = new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description",
                "name", false, "image");
        when(mockRestaurantMenuRepository.save(any(RestaurantMenu.class))).thenReturn(restaurantMenu1);

        // Run the test
        restaurantServiceImplUnderTest.updateRestaurantMenu(restaurantMenuUploadBody);

        // Verify the results
        verify(mockRestaurantImageRepository).save(any(RestaurantImage.class));
        verify(mockRestaurantMenuRepository).save(any(RestaurantMenu.class));
    }

    @Test
    void testUpdateRestaurantMenu_RestaurantMenuRepositoryFindFirstByIdReturnsNull() {
        // Setup
        final RestaurantMenuUploadBody restaurantMenuUploadBody = new RestaurantMenuUploadBody(0L, "restaurantCode",
                "currency", 0.0, "image", "name", "description", null);
        when(mockRestaurantMenuRepository.findFirstById(0L)).thenReturn(null);

        // Run the test
        assertThatThrownBy(
                () -> restaurantServiceImplUnderTest.updateRestaurantMenu(restaurantMenuUploadBody))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testDeleteRestaurant() {
        // Setup
        // Run the test
        restaurantServiceImplUnderTest.deleteRestaurant("restaurantCode");

        // Verify the results
        verify(mockRestaurantRepository).deleteAllByCode("restaurantCode");
        verify(mockRestaurantImageRepository).deleteAllByRestaurantCode("restaurantCode");
        verify(mockRestaurantMenuRepository).deleteAllByRestaurantCode("restaurantCode");
    }

    @Test
    void testDeleteRestaurantMenu() {
        // Setup
        // Run the test
        restaurantServiceImplUnderTest.deleteRestaurantMenu(0L);

        // Verify the results
        verify(mockRestaurantMenuRepository).deleteAllById(0L);
    }

    @Test
    void testGetListRestaurant_Fail_Test() {
        // Run the test
        Assertions.assertThrows(GeneralException.class, () -> {
            restaurantServiceImplUnderTest.getListRestaurant(0,  1);
        });
    }

    @Test
    void testGetListRestaurant_Success_Test() {
        // Setup
        // Configure RestaurantRepository.findAllByUserIdOrderByCreatedAtDesc(...).
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)){
            final List<Restaurant> restaurants = List.of(
                    new Restaurant("id", "code", "title", "description", "slug", RestaurantType.NHA_HANG, new Integer[]{0},
                            "address", "userId", ApproveStatus.PENDING));
            mockedStatic.when(() -> AuthenticationUtils.getUserId()).thenReturn("userId");
            when(mockRestaurantRepository.findAllByUserIdOrderByCreatedAtDesc("userId")).thenReturn(restaurants);
            // Configure RestaurantRepository.findByCodeInOrderByCreatedAtDesc(...).
            final List<Restaurant> restaurants1 = List.of(
                    new Restaurant("id", "code", "title", "description", "slug", RestaurantType.NHA_HANG, new Integer[]{0},
                            "address", "userId", ApproveStatus.PENDING));
            when(mockRestaurantRepository.findByCodeInOrderByCreatedAtDesc(any(), any())).thenReturn(restaurants1);

            when(mockFeatureRepository.findAll()).thenReturn(List.of(new Feature(0, "name")));
            when(mockRestaurantImageRepository.findUniqueImage(any())).thenReturn(Collections.emptyList());

            // Configure RestaurantMenuRepository.findAllByRestaurantCodeIn(...).
            final List<RestaurantMenu> restaurantMenus = List.of(
                    new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description", "name", false, "image"));
            when(mockRestaurantMenuRepository.findAllByRestaurantCodeIn(any())).thenReturn(restaurantMenus);

            // Run the test
            final BaseResponse result = restaurantServiceImplUnderTest.getListRestaurant(2, 2);
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }
}
