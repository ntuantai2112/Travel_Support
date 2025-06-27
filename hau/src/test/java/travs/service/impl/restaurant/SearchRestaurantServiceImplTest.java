package travs.service.impl.restaurant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.common.RestaurantType;
import travs.common.type.ApproveStatus;
import travs.common.type.ImageType;
import travs.entity.restaurant.Restaurant;
import travs.entity.restaurant.RestaurantImage;
import travs.entity.restaurant.RestaurantMenu;
import travs.repository.FavoriteRepository;
import travs.repository.restaurant.FeatureRepository;
import travs.repository.restaurant.RestaurantImageRepository;
import travs.repository.restaurant.RestaurantMenuRepository;
import travs.repository.restaurant.RestaurantRepository;
import travs.request.SearchRequest;
import travs.request.restaurant.FilterRequestRestaurant;
import travs.response.restaurant.SearchRestaurantResponse;
import travs.utils.AuthenticationUtils;
import travs.utils.EntityManagerUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchRestaurantServiceImplTest {

    @Mock
    private FavoriteRepository mockFavoriteRepository;
    @Mock
    private EntityManager mockEntityManager;
    @Mock
    private RestaurantRepository mockRestaurantRepository;
    @Mock
    private FeatureRepository mockFeatureRepository;
    @Mock
    private RestaurantImageRepository mockRestaurantImageRepository;
    @Mock
    private RestaurantMenuRepository mockRestaurantMenuRepository;
    @InjectMocks
    private SearchRestaurantServiceImpl searchRestaurantServiceImplUnderTest;


    @Test
    void testSearchFilter_Success_Test() {
        // Setup
        try (MockedStatic<EntityManagerUtils> mockedStatic = mockStatic(EntityManagerUtils.class);
             MockedStatic<AuthenticationUtils> authenticationUtilsMockedStatic = mockStatic(AuthenticationUtils.class)) {
            {
                final SearchRequest searchRequest = new SearchRequest("q", 2, 55);
                final FilterRequestRestaurant filterRequestRestaurant = new FilterRequestRestaurant("type");
                final List<String> list = List.of("value", "value1", "value2");
                Query query = mock(Query.class);
                mockedStatic.when(() -> EntityManagerUtils.buildQuery(any(), any())).thenReturn(query);
                authenticationUtilsMockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
                when(query.getResultList()).thenReturn(list);
                // Configure RestaurantRepository.findByCodeInOrderByCreatedAtDesc(...).
                final List<Restaurant> restaurants = List.of(
                        new Restaurant("id", "restaurantCode", "title", "description", "slug", RestaurantType.NHA_HANG, new Integer[]{0},
                                "address", "userId", ApproveStatus.PENDING));
                when(mockRestaurantRepository.findByCodeInOrderByCreatedAtDesc(any(), any())).thenReturn(restaurants);

                // Configure RestaurantImageRepository.findUniqueImage(...).
                final List<RestaurantImage> restaurantImages = List.of(
                        new RestaurantImage(0L, "restaurantCode", "caption", "url", ImageType.ROOM));
                when(mockRestaurantImageRepository.findUniqueImage(any())).thenReturn(restaurantImages);

                // Configure RestaurantMenuRepository.findAllByRestaurantCodeIn(...).
                final List<RestaurantMenu> restaurantMenus = List.of(
                        new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description", "name", false, "image"), new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description", "name", false, "image"));
                when(mockRestaurantMenuRepository.findAllByRestaurantCodeIn(any())).thenReturn(restaurantMenus);


                // Run the test
                final SearchRestaurantResponse result = searchRestaurantServiceImplUnderTest.searchFilter(searchRequest,
                        filterRequestRestaurant);

                // Verify the results
                assertThat(result).isNotNull();
                assertThat(result.getRestaurants()).isNotNull();
                assertThat(result.getRestaurants().size()).isEqualTo(1);
            }
        }
    }

    @Test
    void testSearchFilter_UserIdNull_Success_Test() {
        try (MockedStatic<EntityManagerUtils> mockedStatic = mockStatic(EntityManagerUtils.class)) {
            {
                final SearchRequest searchRequest = new SearchRequest("", 2, 55);
                final FilterRequestRestaurant filterRequestRestaurant = new FilterRequestRestaurant("type");
                final List<String> list = List.of("value", "value1", "value2");
                Query query = mock(Query.class);
                mockedStatic.when(() -> EntityManagerUtils.buildQuery(any(), any())).thenReturn(query);
                when(query.getResultList()).thenReturn(list);
                // Configure RestaurantRepository.findByCodeInOrderByCreatedAtDesc(...).
                final List<Restaurant> restaurants = List.of(
                        new Restaurant("id", "restaurantCode", "title", "description", "slug", RestaurantType.NHA_HANG, new Integer[]{0},
                                "address", "userId", ApproveStatus.PENDING));
                when(mockRestaurantRepository.findByCodeInOrderByCreatedAtDesc(any(), any())).thenReturn(restaurants);

                // Configure RestaurantImageRepository.findUniqueImage(...).
                final List<RestaurantImage> restaurantImages = List.of(
                        new RestaurantImage(0L, "restaurantCode", "caption", "url", ImageType.ROOM));
                when(mockRestaurantImageRepository.findUniqueImage(any())).thenReturn(restaurantImages);

                // Configure RestaurantMenuRepository.findAllByRestaurantCodeIn(...).
                final List<RestaurantMenu> restaurantMenus = List.of(
                        new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description", "name", false, "image"), new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description", "name", false, "image"));
                when(mockRestaurantMenuRepository.findAllByRestaurantCodeIn(any())).thenReturn(restaurantMenus);


                // Run the test
                final SearchRestaurantResponse result = searchRestaurantServiceImplUnderTest.searchFilter(searchRequest,
                        filterRequestRestaurant);

                // Verify the results
                assertThat(result).isNotNull();
                assertThat(result.getRestaurants()).isNotNull();
                assertThat(result.getRestaurants().size()).isEqualTo(1);
            }
        }
    }
}
