package travs.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.common.ActivitiesType;
import travs.common.type.ApproveStatus;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesGame;
import travs.entity.activities.ActivitiesImage;
import travs.exception.GeneralException;
import travs.exception.ResourceNotFoundException;
import travs.repository.FavoriteRepository;
import travs.repository.activities.ActivitiesGameRepository;
import travs.repository.activities.ActivitiesImageRepository;
import travs.repository.activities.ActivitiesRepository;
import travs.request.activities.ActivitiesGameUploadBody;
import travs.request.activities.ActivitiesUploadBody;
import travs.response.BaseResponse;
import travs.utils.AuthenticationUtils;
import travs.utils.FileStore;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivitiesServiceImplTest {

    @Mock
    private ActivitiesRepository mockActivitiesRepository;
    @Mock
    private ActivitiesImageRepository mockActivitiesImageRepository;
    @Mock
    private FavoriteRepository mockFavoriteRepository;
    @Mock
    private ActivitiesGameRepository mockActivitiesGameRepository;
    @InjectMocks
    private ActivitiesServiceImpl activitiesServiceImplUnderTest;

    @Test
    void testGet_Success_Test() {
        // Setup
        // Configure ActivitiesRepository.findFirstBySlug(...).
        try (MockedStatic<AuthenticationUtils> authenticationUtilsMockedStatic = mockStatic(AuthenticationUtils.class)) {
            final Optional<Activities> activities = Optional.of(
                    new Activities("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0,
                            "address", "userId", ApproveStatus.PENDING));
            when(mockActivitiesRepository.findFirstBySlug(any())).thenReturn(activities);
            authenticationUtilsMockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            // Configure ActivitiesGameRepository.findAllByActivitiesCode(...).
            final List<ActivitiesGame> activitiesGames = List.of(
                    new ActivitiesGame(0, "activitiesCode", "name", "image", "description"));
            when(mockActivitiesGameRepository.findAllByActivitiesCode(any())).thenReturn(activitiesGames);

            // Configure ActivitiesImageRepository.findAllByActivitiesCode(...).
            final List<ActivitiesImage> activitiesImages = List.of(
                    new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
            when(mockActivitiesImageRepository.findAllByActivitiesCode(any())).thenReturn(activitiesImages);

            // Run the test
            final BaseResponse result = activitiesServiceImplUnderTest.get("slug");
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testGet_UserIdNull_Success_Test() {
        // Setup
        // Configure ActivitiesRepository.findFirstBySlug(...).
        final Optional<Activities> activities = Optional.of(
                new Activities("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0,
                        "address", "userId", ApproveStatus.PENDING));
        when(mockActivitiesRepository.findFirstBySlug(any())).thenReturn(activities);
        // Configure ActivitiesGameRepository.findAllByActivitiesCode(...).
        final List<ActivitiesGame> activitiesGames = List.of(
                new ActivitiesGame(0, "activitiesCode", "name", "image", "description"));
        when(mockActivitiesGameRepository.findAllByActivitiesCode(any())).thenReturn(activitiesGames);

        // Configure ActivitiesImageRepository.findAllByActivitiesCode(...).
        final List<ActivitiesImage> activitiesImages = List.of(
                new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
        when(mockActivitiesImageRepository.findAllByActivitiesCode(any())).thenReturn(activitiesImages);

        // Run the test
        final BaseResponse result = activitiesServiceImplUnderTest.get("slug");
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void testGet_ActivitiesRepositoryReturnsAbsent() {
        // Setup
        when(mockActivitiesRepository.findFirstBySlug("slug")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> activitiesServiceImplUnderTest.get("slug"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testUploadActivities_Success_Test() {
        // Setup
        try (MockedStatic<FileStore> mockedStatic = mockStatic(FileStore.class)) {
            final ActivitiesUploadBody activitiesUploadBody = new ActivitiesUploadBody("description", "OUT_DOOR", 0, "address",
                    "title", List.of("value"), 0.0, 0.0, "activitiesType", 0, "activitiesCode", List.of());

            // Configure ActivitiesImageRepository.saveAll(...).
            final List<ActivitiesImage> activitiesImages = List.of(
                    new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
            final List<String> images = List.of("value", "value1", "value2");
            mockedStatic.when(() -> FileStore.getFilePaths(any(), any())).thenReturn(images);
            when(mockActivitiesImageRepository.saveAll(any())).thenReturn(activitiesImages);

            // Configure ActivitiesRepository.save(...).
            final Activities activities = new Activities("id", "code", "title", "description", "slug",
                    ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0, "address", "userId", ApproveStatus.PENDING);
            when(mockActivitiesRepository.save(any())).thenReturn(activities);

            // Run the test
            final BaseResponse result = activitiesServiceImplUnderTest.uploadActivities(activitiesUploadBody);

            // Verify the results
            verify(mockActivitiesImageRepository).saveAll(any());
            verify(mockActivitiesRepository).updateSearchVector("code");
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testUpdateActivities_Success_Test() {
        // Setup
        try (MockedStatic<FileStore> mockedStatic = mockStatic(FileStore.class)) {
            final ActivitiesUploadBody activitiesUploadBody = new ActivitiesUploadBody("description", "OUT_DOOR", 0, "address",
                    "title", List.of("value"), 0.0, 0.0, "activitiesType", 0, "activitiesCode", List.of());

            // Configure ActivitiesRepository.findFirstByCode(...).
            final Activities activities = new Activities("id", "code", "title", "description", "slug",
                    ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0, "address", "userId", ApproveStatus.PENDING);
            final List<String> images = List.of("value", "value1", "value2");
            mockedStatic.when(() -> FileStore.getFilePaths(any(), any())).thenReturn(images);
            when(mockActivitiesRepository.findFirstByCode(any())).thenReturn(activities);

            // Configure ActivitiesImageRepository.saveAll(...).
            final List<ActivitiesImage> activitiesImages = List.of(
                    new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
            when(mockActivitiesImageRepository.saveAll(any())).thenReturn(activitiesImages);

            // Configure ActivitiesRepository.save(...).
            final Activities activities1 = new Activities("id", "code", "title", "description", "slug",
                    ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0, "address", "userId", ApproveStatus.PENDING);
            when(mockActivitiesRepository.save(any(Activities.class))).thenReturn(activities1);

            // Run the test
            final BaseResponse result = activitiesServiceImplUnderTest.updateActivities(activitiesUploadBody);

            // Verify the results
            verify(mockActivitiesImageRepository).saveAll(any());
            verify(mockActivitiesRepository).updateSearchVector("code");
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }

    }

    @Test
    void testUpdateActivities_ActivitiesRepositoryFindFirstByCodeReturnsNull() {
        // Setup
        final ActivitiesUploadBody activitiesUploadBody = new ActivitiesUploadBody("description", "type", 0, "address",
                "title", List.of("value"), 0.0, 0.0, "activitiesType", 0, "activitiesCode", List.of());
        when(mockActivitiesRepository.findFirstByCode("activitiesCode")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> activitiesServiceImplUnderTest.updateActivities(activitiesUploadBody))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testUploadGameActivities() {
        // Setup
        final ActivitiesGameUploadBody activitiesGameUploadBody = new ActivitiesGameUploadBody(0, "activitiesCode",
                "description", "name", null);

        // Configure ActivitiesGameRepository.save(...).
        final ActivitiesGame activitiesGame = new ActivitiesGame(0, "activitiesCode", "name", "image", "description");
        when(mockActivitiesGameRepository.save(any(ActivitiesGame.class))).thenReturn(activitiesGame);

        // Run the test
        final BaseResponse result = activitiesServiceImplUnderTest.uploadGameActivities(activitiesGameUploadBody);
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void testUpdateGameActivities() {
        // Setup
        final ActivitiesGameUploadBody activitiesGameUploadBody = new ActivitiesGameUploadBody(0, "activitiesCode",
                "description", "name", null);

        // Configure ActivitiesGameRepository.findFirstById(...).
        final ActivitiesGame activitiesGame = new ActivitiesGame(0, "activitiesCode", "name", "image", "description");
        when(mockActivitiesGameRepository.findFirstById(0)).thenReturn(activitiesGame);

        // Configure ActivitiesImageRepository.save(...).
        final ActivitiesImage activitiesImage = new ActivitiesImage(0L, "activitiesCode", "caption", "url");
        when(mockActivitiesImageRepository.save(any(ActivitiesImage.class))).thenReturn(activitiesImage);

        // Configure ActivitiesGameRepository.save(...).
        final ActivitiesGame activitiesGame1 = new ActivitiesGame(0, "activitiesCode", "name", "image", "description");
        when(mockActivitiesGameRepository.save(any(ActivitiesGame.class))).thenReturn(activitiesGame1);

        // Run the test
        final BaseResponse result = activitiesServiceImplUnderTest.updateGameActivities(activitiesGameUploadBody);

        // Verify the results
        verify(mockActivitiesImageRepository).save(any(ActivitiesImage.class));
    }

    @Test
    void testUpdateGameActivities_ActivitiesGameRepositoryFindFirstByIdReturnsNull() {
        // Setup
        final ActivitiesGameUploadBody activitiesGameUploadBody = new ActivitiesGameUploadBody(0, "activitiesCode",
                "description", "name", null);
        when(mockActivitiesGameRepository.findFirstById(0)).thenReturn(null);

        // Run the test
        assertThatThrownBy(
                () -> activitiesServiceImplUnderTest.updateGameActivities(activitiesGameUploadBody))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetListActivities_Success_Test() {
        // Setup
        // Configure ActivitiesRepository.findAllByUserIdOrderByCreatedAtDesc(...).
        try (MockedStatic<AuthenticationUtils> authenticationUtilsMockedStatic = mockStatic(AuthenticationUtils.class)) {
            final List<Activities> activities = List.of(
                    new Activities("id", "activitiesCode", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0,
                            "address", "userId", ApproveStatus.PENDING));
            when(mockActivitiesRepository.findAllByUserIdOrderByCreatedAtDesc(any())).thenReturn(activities);
            authenticationUtilsMockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            // Configure ActivitiesRepository.findByCodeInOrderByCreatedAtDesc(...).
            final List<Activities> activities1 = List.of(
                    new Activities("id", "activitiesCode", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0,
                            "address", "userId", ApproveStatus.PENDING));
            when(mockActivitiesRepository.findByCodeInOrderByCreatedAtDesc(any(), any())).thenReturn(activities1);

            // Configure ActivitiesImageRepository.findUniqueImage(...).
            final List<ActivitiesImage> activitiesImages = List.of(
                    new ActivitiesImage(0L, "activitiesCode", "caption", "url"));
            when(mockActivitiesImageRepository.findUniqueImage(any())).thenReturn(activitiesImages);

            // Configure ActivitiesGameRepository.findAllByActivitiesCodeIn(...).
            final List<ActivitiesGame> activitiesGames = List.of(
                    new ActivitiesGame(0, "activitiesCode", "name", "image", "description"), new ActivitiesGame(0, "activitiesCode", "name", "image", "description"));
            when(mockActivitiesGameRepository.findAllByActivitiesCodeIn(any())).thenReturn(activitiesGames);

            // Run the test
            final BaseResponse result = activitiesServiceImplUnderTest.getListActivities(2, 55);

            // Verify the results
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testGetListActivities_Fail_Test() {
        Assertions.assertThrows(GeneralException.class, () -> {
            activitiesServiceImplUnderTest.getListActivities(2, 55);
        });

    }

    @Test
    void testDeleteActivities() {
        // Setup
        // Run the test
        activitiesServiceImplUnderTest.deleteActivities("activitiesCode");

        // Verify the results
        verify(mockActivitiesImageRepository).deleteAllByActivitiesCode("activitiesCode");
        verify(mockActivitiesRepository).deleteAllByCode("activitiesCode");
        verify(mockActivitiesGameRepository).deleteAllByActivitiesCode("activitiesCode");
    }

    @Test
    void testDeleteGameActivities() {
        // Setup
        // Run the test
        activitiesServiceImplUnderTest.deleteGameActivities(0);

        // Verify the results
        verify(mockActivitiesGameRepository).deleteById(0);
    }
}
