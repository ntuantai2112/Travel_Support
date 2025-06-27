package travs.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.common.FavoriteType;
import travs.common.HotelType;
import travs.common.type.ApproveStatus;
import travs.common.type.ImageType;
import travs.entity.Favorite;
import travs.entity.hotel.Amenities;
import travs.entity.hotel.Hotel;
import travs.entity.hotel.HotelImage;
import travs.repository.AmenitiesRepository;
import travs.repository.FavoriteRepository;
import travs.repository.HotelImageRepository;
import travs.repository.HotelRepository;
import travs.request.FilterRequest;
import travs.request.SearchRequest;
import travs.response.hotel.SearchResponse;
import travs.utils.AuthenticationUtils;
import travs.utils.EntityManagerUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @Mock
    private HotelRepository mockHotelRepository;
    @Mock
    private EntityManager mockEntityManager;
    @Mock
    private AmenitiesRepository mockAmenitiesRepository;
    @Mock
    private HotelImageRepository mockHotelImageRepository;
    @Mock
    private FavoriteRepository mockFavoriteRepository;

    @InjectMocks
    private SearchServiceImpl searchServiceImplUnderTest;

    @Test
    void testSearchFilter_Success_Test() {
        try (MockedStatic<EntityManagerUtils> mockedStatic = mockStatic(EntityManagerUtils.class);
             MockedStatic<AuthenticationUtils> authenticationUtilsMockedStatic = mockStatic(AuthenticationUtils.class)) {
            {
                final SearchRequest searchRequest = new SearchRequest("q", 5, 55);
                final FilterRequest filterRequest = new FilterRequest(0L, "type");
                final List<String> list = List.of("value", "value1", "value2");
                Query query = mock(Query.class);
                mockedStatic.when(() -> EntityManagerUtils.buildQuery(any(), any())).thenReturn(query);
                authenticationUtilsMockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
                when(query.getResultList()).thenReturn(list);
                // Configure AmenitiesRepository.findAll(...).
                final List<Amenities> amenities = List.of(new Amenities(0, "name", "icon"));
                when(mockAmenitiesRepository.findAll()).thenReturn(amenities);

                // Configure HotelRepository.findByCodeInOrderByCreatedAtDesc(...).
                final List<Hotel> hotels = List.of(
                        new Hotel("id", "hotelCode", "title", "description", "slug", HotelType.POPULAR, 0.0, 0, new Integer[]{0},
                                0.0, "address", "userId", ApproveStatus.PENDING));
                when(mockHotelRepository.findByCodeInOrderByCreatedAtDesc(any(), any())).thenReturn(hotels);

                // Configure HotelImageRepository.findUniqueImage(...).
                final List<HotelImage> hotelImages = List.of(new HotelImage(0L, "hotelCode", "caption", "url", ImageType.ROOM));
                when(mockHotelImageRepository.findUniqueImage(any())).thenReturn(hotelImages);

                // Configure FavoriteRepository.findByUserIdAndSlug(...).
                final Optional<Favorite> favorite = Optional.of(
                        new Favorite("id", "userId", FavoriteType.HOTEL, "slug", "imageUrl", "title"));
                when(mockFavoriteRepository.findByUserIdAndSlug(any(), any())).thenReturn(favorite);

                // Run the test
                final SearchResponse result = searchServiceImplUnderTest.searchFilter(searchRequest, filterRequest);

                // Verify the results
                assertThat(result).isNotNull();
                assertThat(result.getHotels()).isNotNull();
                assertThat(result.getHotels().size()).isEqualTo(1);
            }
        }
    }

    @Test
    void testSearchFilter_QNull_Success_Test() {
        try (MockedStatic<EntityManagerUtils> mockedStatic = mockStatic(EntityManagerUtils.class);
             MockedStatic<AuthenticationUtils> authenticationUtilsMockedStatic = mockStatic(AuthenticationUtils.class)) {
            {
                final SearchRequest searchRequest = new SearchRequest("", 5, 55);
                final FilterRequest filterRequest = new FilterRequest(0L, "type");
                final List<String> list = List.of("value", "value1", "value2");
                Query query = mock(Query.class);
                mockedStatic.when(() -> EntityManagerUtils.buildQuery(any(), any())).thenReturn(query);
                authenticationUtilsMockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
                when(query.getResultList()).thenReturn(list);
                // Configure AmenitiesRepository.findAll(...).
                final List<Amenities> amenities = List.of(new Amenities(0, "name", "icon"));
                when(mockAmenitiesRepository.findAll()).thenReturn(amenities);

                // Configure HotelRepository.findByCodeInOrderByCreatedAtDesc(...).
                final List<Hotel> hotels = List.of(
                        new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0, new Integer[]{0},
                                0.0, "address", "userId", ApproveStatus.PENDING));
                when(mockHotelRepository.findByCodeInOrderByCreatedAtDesc(any(), any())).thenReturn(hotels);

                // Configure HotelImageRepository.findUniqueImage(...).
                final List<HotelImage> hotelImages = List.of(new HotelImage(0L, "hotelCode", "caption", "url", ImageType.ROOM));
                when(mockHotelImageRepository.findUniqueImage(any())).thenReturn(hotelImages);

                // Configure FavoriteRepository.findByUserIdAndSlug(...).
                final Optional<Favorite> favorite = Optional.of(
                        new Favorite("id", "userId", FavoriteType.HOTEL, "slug", "imageUrl", "title"));
                when(mockFavoriteRepository.findByUserIdAndSlug(any(), any())).thenReturn(favorite);

                // Run the test
                final SearchResponse result = searchServiceImplUnderTest.searchFilter(searchRequest, filterRequest);

                // Verify the results
                assertThat(result).isNotNull();
                assertThat(result.getHotels()).isNotNull();
                assertThat(result.getHotels().size()).isEqualTo(1);
            }
        }
    }

    @Test
    void testCheckFavoriteHotel_Fail_TesT() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = SearchServiceImpl.class.getDeclaredMethod("checkFavoriteHotel", String.class);
        method.setAccessible(true);
        Object o = method.invoke(searchServiceImplUnderTest, "slug");
        assertThat((Boolean)o).isFalse();
    }
}
