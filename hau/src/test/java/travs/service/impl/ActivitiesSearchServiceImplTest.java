package travs.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.common.ActivitiesType;
import travs.common.FavoriteType;
import travs.common.type.ApproveStatus;
import travs.entity.Favorite;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesGame;
import travs.entity.activities.ActivitiesImage;
import travs.repository.FavoriteRepository;
import travs.repository.activities.ActivitiesGameRepository;
import travs.repository.activities.ActivitiesImageRepository;
import travs.repository.activities.ActivitiesRepository;
import travs.request.SearchRequest;
import travs.request.activities.FilterRequestActivities;
import travs.response.activities.ActivitiesSearchResponse;
import travs.utils.AuthenticationUtils;
import travs.utils.EntityManagerUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivitiesSearchServiceImplTest {

    @Mock
    private ActivitiesRepository mockActivitiesRepository;
    @Mock
    private EntityManager mockEntityManager;
    @Mock
    private ActivitiesGameRepository mockActivitiesGameRepository;
    @Mock
    private ActivitiesImageRepository mockActivitiesImageRepository;
    @Mock
    private FavoriteRepository mockFavoriteRepository;
    @InjectMocks
    private ActivitiesSearchServiceImpl activitiesSearchServiceImplUnderTest;

    @Test
    void testSearchFilter_Success_Test() {
        // Setup
        try (MockedStatic<EntityManagerUtils> mockedStatic = mockStatic(EntityManagerUtils.class);
             MockedStatic<AuthenticationUtils> authenticationUtilsMockedStatic = mockStatic(AuthenticationUtils.class)) {
            {
                final SearchRequest searchRequest = new SearchRequest("q", 2, 55);
                final FilterRequestActivities filterRequest = new FilterRequestActivities(0, 0, "type");

                // Configure ActivitiesRepository.findByCodeInOrderByCreatedAtDesc(...).
                final List<Activities> activities = List.of(
                        new Activities("id", "activitiesCode", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0,
                                "address", "userId", ApproveStatus.PENDING));
                final List<String> list = List.of("value", "value1", "value2");
                Query query = mock(Query.class);
                mockedStatic.when(() -> EntityManagerUtils.buildQuery(any(), any())).thenReturn(query);
                authenticationUtilsMockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
                when(query.getResultList()).thenReturn(list);
                when(mockActivitiesRepository.findByCodeInOrderByCreatedAtDesc(any(),any())).thenReturn(activities);

                // Configure ActivitiesImageRepository.findUniqueImage(...).
                final List<ActivitiesImage> activitiesImages = List.of(
                        new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
                when(mockActivitiesImageRepository.findUniqueImage(any())).thenReturn(activitiesImages);

                // Configure ActivitiesGameRepository.findAllByActivitiesCodeIn(...).
                final List<ActivitiesGame> activitiesGames = List.of(
                        new ActivitiesGame(0, "activitiesCode", "name", "image", "description"));
                when(mockActivitiesGameRepository.findAllByActivitiesCodeIn(any())).thenReturn(activitiesGames);

                // Configure FavoriteRepository.findByUserIdAndSlug(...).
                final Optional<Favorite> favorite = Optional.of(
                        new Favorite("id", "userId", FavoriteType.HOTEL, "slug", "imageUrl", "title"));
                when(mockFavoriteRepository.findByUserIdAndSlug(any(),any())).thenReturn(favorite);

                // Run the test
                final ActivitiesSearchResponse result = activitiesSearchServiceImplUnderTest.searchFilter(searchRequest,
                        filterRequest);

                // Verify the results
                assertThat(result).isNotNull();
                assertThat(result.getActivities()).isNotNull();
                assertThat(result.getActivities().size()).isEqualTo(1);
            }}
    }

    @Test
    void testSearchFilter_UserIdNull_Success_Test() {
        // Setup
        try (MockedStatic<EntityManagerUtils> mockedStatic = mockStatic(EntityManagerUtils.class)) {
            {
                final SearchRequest searchRequest = new SearchRequest("", 2, 55);
                final FilterRequestActivities filterRequest = new FilterRequestActivities(0, 0, "type");

                // Configure ActivitiesRepository.findByCodeInOrderByCreatedAtDesc(...).
                final List<Activities> activities = List.of(
                        new Activities("id", "activitiesCode", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0,
                                "address", "userId", ApproveStatus.PENDING));
                final List<String> list = List.of("value", "value1", "value2");
                Query query = mock(Query.class);
                mockedStatic.when(() -> EntityManagerUtils.buildQuery(any(), any())).thenReturn(query);
                when(query.getResultList()).thenReturn(list);
                when(mockActivitiesRepository.findByCodeInOrderByCreatedAtDesc(any(),any())).thenReturn(activities);

                // Configure ActivitiesImageRepository.findUniqueImage(...).
                final List<ActivitiesImage> activitiesImages = List.of(
                        new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
                when(mockActivitiesImageRepository.findUniqueImage(any())).thenReturn(activitiesImages);

                // Configure ActivitiesGameRepository.findAllByActivitiesCodeIn(...).
                final List<ActivitiesGame> activitiesGames = List.of(
                        new ActivitiesGame(0, "activitiesCode", "name", "image", "description"),new ActivitiesGame(0, "activitiesCode", "name", "image", "description"));
                when(mockActivitiesGameRepository.findAllByActivitiesCodeIn(any())).thenReturn(activitiesGames);

                // Run the test
                final ActivitiesSearchResponse result = activitiesSearchServiceImplUnderTest.searchFilter(searchRequest,
                        filterRequest);

                // Verify the results
                assertThat(result).isNotNull();
                assertThat(result.getActivities()).isNotNull();
                assertThat(result.getActivities().size()).isEqualTo(1);
            }}
    }
}
