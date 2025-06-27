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
import travs.common.type.RoomType;
import travs.entity.Favorite;
import travs.entity.hotel.Amenities;
import travs.entity.hotel.Hotel;
import travs.entity.hotel.HotelBookableItem;
import travs.entity.hotel.HotelImage;
import travs.exception.GeneralException;
import travs.exception.ResourceNotFoundException;
import travs.repository.AmenitiesRepository;
import travs.repository.FavoriteRepository;
import travs.repository.HotelBookItemRepository;
import travs.repository.HotelBookingReceiptRepository;
import travs.repository.HotelImageRepository;
import travs.repository.HotelRepository;
import travs.request.hotel.HotelRoomUploadBody;
import travs.request.hotel.HotelUploadBody;
import travs.response.BaseResponse;
import travs.utils.AuthenticationUtils;
import travs.utils.FileStore;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository mockHotelRepository;
    @Mock
    private HotelBookItemRepository mockHotelBookItemRepository;
    @Mock
    private HotelImageRepository mockHotelImageRepository;
    @Mock
    private FavoriteRepository mockFavoriteRepository;
    @Mock
    private HotelBookingReceiptRepository mockHotelBookingReceiptRepository;
    @Mock
    private AmenitiesRepository mockAmenitiesRepository;

    @InjectMocks
    private HotelServiceImpl hotelServiceImplUnderTest;

    @Test
    void testGet_Success_Test() {
        try (MockedStatic<AuthenticationUtils> authenticationUtilsMockedStatic = mockStatic(AuthenticationUtils.class);
            MockedStatic<FileStore> fileStoreMockedStatic = mockStatic(FileStore.class)
        ){
            final Optional<Hotel> hotel = Optional.of(
                    new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0, new Integer[]{0},
                            0.0, "address", "userId", ApproveStatus.PENDING));
            when(mockHotelRepository.findFirstBySlug(any())).thenReturn(hotel);
            authenticationUtilsMockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            when(mockFavoriteRepository.findByUserIdAndSlug(any(), any())).thenReturn(Optional.empty());
            List<String> images = List.of("image1", "image2");
            fileStoreMockedStatic.when(() -> FileStore.getFilePaths(any(), any())).thenReturn(images);

            // Configure AmenitiesRepository.findAll(...).
            final List<Amenities> amenities = List.of(new Amenities(0, "name", "icon"));
            when(mockAmenitiesRepository.findAll()).thenReturn(amenities);

            // Configure FavoriteRepository.findByUserIdAndSlug(...).
            final Optional<Favorite> favorite = Optional.of(
                    new Favorite("id", "userId", FavoriteType.HOTEL, "slug", "imageUrl", "title"));
            when(mockFavoriteRepository.findByUserIdAndSlug(any(),any())).thenReturn(favorite);

            // Configure HotelImageRepository.findAllByHotelCode(...).
            final List<HotelImage> hotelImages = List.of(new HotelImage(0L, "hotelCode", "caption", "url", ImageType.ROOM));
            when(mockHotelImageRepository.findAllByHotelCode(any())).thenReturn(hotelImages);

            // Configure HotelBookItemRepository.findAllByHotelCodeOrderByPriceAsc(...).
            final List<HotelBookableItem> hotelBookableItems = List.of(
                    new HotelBookableItem(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", false, RoomType.SINGLE,
                            "image", new Integer[]{0}));
            when(mockHotelBookItemRepository.findAllByHotelCodeOrderByPriceAsc(any())).thenReturn(hotelBookableItems);

            // Run the test
            final BaseResponse result = hotelServiceImplUnderTest.get("slug");
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }

    }

    @Test
    void testGet_HotelRepositoryReturnsAbsent() {
        // Setup
        when(mockHotelRepository.findFirstBySlug("slug")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> hotelServiceImplUnderTest.get("slug")).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testUploadHotel_Success_Test() {
        try (MockedStatic<AuthenticationUtils> authenticationUtilsMockedStatic = mockStatic(AuthenticationUtils.class);
             MockedStatic<FileStore> fileStoreMockedStatic = mockStatic(FileStore.class)
        ){
            final HotelUploadBody hotelUploadBody = new HotelUploadBody("hotelCode", new Integer[]{0}, "description", 0,
                    "FAMILY", 0, 0, "address", "title", List.of("value"), List.of());
            // Configure HotelImageRepository.saveAll(...).
            final List<HotelImage> hotelImages = List.of(new HotelImage(0L, "hotelCode", "caption", "url", ImageType.ROOM));
            authenticationUtilsMockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            List<String> images = List.of("image1", "image2");
            fileStoreMockedStatic.when(() -> FileStore.getFilePaths(any(), any())).thenReturn(images);
            when(mockHotelImageRepository.saveAll(any())).thenReturn(hotelImages);

            // Configure HotelRepository.save(...).
            final Hotel hotel = new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0,
                    new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING);
            when(mockHotelRepository.save(any(Hotel.class))).thenReturn(hotel);

            // Run the test
            final BaseResponse result = hotelServiceImplUnderTest.uploadHotel(hotelUploadBody);

            // Verify the results
            verify(mockHotelImageRepository).saveAll(any());
            verify(mockHotelRepository).updateSearchVector("code");
            assertThat(result).isNotNull();
        }

    }

    @Test
    void testUpdateHotel_Success_Test() {
        try (MockedStatic<FileStore> fileStoreMockedStatic = mockStatic(FileStore.class)){
            final HotelUploadBody hotelUploadBody = new HotelUploadBody("hotelCode", new Integer[]{0}, "description", 0,
                    "type", 0, 0, "address", "title", List.of("value"), List.of());

            // Configure HotelRepository.findFirstByCode(...).
            final Hotel hotel = new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0,
                    new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING);
            List<String> images = List.of("image1", "image2");
            fileStoreMockedStatic.when(() -> FileStore.getFilePaths(any(), any())).thenReturn(images);
            when(mockHotelRepository.findFirstByCode(any())).thenReturn(hotel);

            // Configure HotelImageRepository.saveAll(...).
            final List<HotelImage> hotelImages = List.of(new HotelImage(0L, "hotelCode", "caption", "url", ImageType.ROOM));
            when(mockHotelImageRepository.saveAll(any())).thenReturn(hotelImages);

            // Configure HotelRepository.save(...).
            final Hotel hotel1 = new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0,
                    new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING);
            when(mockHotelRepository.save(any(Hotel.class))).thenReturn(hotel1);

            // Run the test
            final BaseResponse result = hotelServiceImplUnderTest.updateHotel(hotelUploadBody);

            // Verify the results
            verify(mockHotelImageRepository).saveAll(any());
            verify(mockHotelRepository).updateSearchVector("code");
            assertThat(result).isNotNull();
        }

    }

    @Test
    void testUpdateHotel_HotelRepositoryFindFirstByCodeReturnsNull() {
        // Setup
        final HotelUploadBody hotelUploadBody = new HotelUploadBody("hotelCode", new Integer[]{0}, "description", 0,
                "type", 0, 0, "address", "title", List.of("value"), List.of());
        when(mockHotelRepository.findFirstByCode("hotelCode")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> hotelServiceImplUnderTest.updateHotel(hotelUploadBody))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testUploadHotelRoom_Success_Test() {
        // Setup
        final HotelRoomUploadBody hotelRoomUploadBody = new HotelRoomUploadBody(0L, new Integer[]{0}, "hotelCode",
                "currency", 0.0, "hotelOptionCode", "DOUBLE", "imageRoom", null);

        // Configure HotelRepository.findFirstByCode(...).
        final Hotel hotel = new Hotel("id", "code", "DOUBLE", "description", "slug", HotelType.POPULAR, 0.0, 0,
                new Integer[]{0}, null, "address", "userId", ApproveStatus.PENDING);
        when(mockHotelRepository.findFirstByCode(any())).thenReturn(hotel);

        // Configure HotelRepository.save(...).
        final Hotel hotel1 = new Hotel("id", "code", "DOUBLE", "description", "slug", HotelType.POPULAR, 0.0, 0,
                new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING);
        when(mockHotelRepository.save(any(Hotel.class))).thenReturn(hotel1);

        // Configure HotelBookItemRepository.save(...).
        final HotelBookableItem hotelBookableItem = new HotelBookableItem(0L, "hotelCode", "hotelOptionCode", 0.0,
                "currency", false, RoomType.SINGLE, "image", new Integer[]{0});
        when(mockHotelBookItemRepository.save(any(HotelBookableItem.class))).thenReturn(hotelBookableItem);

        // Run the test
        hotelServiceImplUnderTest.uploadHotelRoom(hotelRoomUploadBody);

        // Verify the results
        verify(mockHotelRepository).save(any(Hotel.class));
        verify(mockHotelBookItemRepository).save(any(HotelBookableItem.class));
    }

    @Test
    void testUploadHotelRoom_Price_Success_Test() {
        // Setup
        final HotelRoomUploadBody hotelRoomUploadBody = new HotelRoomUploadBody(0L, new Integer[]{0}, "hotelCode",
                "currency", 0.0, "hotelOptionCode", "DOUBLE", "imageRoom", null);

        // Configure HotelRepository.findFirstByCode(...).
        final Hotel hotel = new Hotel("id", "code", "DOUBLE", "description", "slug", HotelType.POPULAR, 0.0, 0,
                new Integer[]{0}, 2.2, "address", "userId", ApproveStatus.PENDING);
        when(mockHotelRepository.findFirstByCode(any())).thenReturn(hotel);

        // Configure HotelRepository.save(...).
        final Hotel hotel1 = new Hotel("id", "code", "DOUBLE", "description", "slug", HotelType.POPULAR, 0.0, 0,
                new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING);
        when(mockHotelRepository.save(any(Hotel.class))).thenReturn(hotel1);

        // Configure HotelBookItemRepository.save(...).
        final HotelBookableItem hotelBookableItem = new HotelBookableItem(0L, "hotelCode", "hotelOptionCode", 0.0,
                "currency", false, RoomType.SINGLE, "image", new Integer[]{0});
        when(mockHotelBookItemRepository.save(any(HotelBookableItem.class))).thenReturn(hotelBookableItem);

        // Run the test
        hotelServiceImplUnderTest.uploadHotelRoom(hotelRoomUploadBody);

        // Verify the results
        verify(mockHotelRepository).save(any(Hotel.class));
        verify(mockHotelBookItemRepository).save(any(HotelBookableItem.class));
    }

    @Test
    void testUpdateHotelRoom_PriceNull_Success_Test() {
        // Setup
        final HotelRoomUploadBody hotelRoomUploadBody = new HotelRoomUploadBody(0L, new Integer[]{0}, "hotelCode",
                "currency", 0.0, "hotelOptionCode", "SINGLE", "imageRoom", null);

        // Configure HotelBookItemRepository.findFirstById(...).
        final HotelBookableItem hotelBookableItem = new HotelBookableItem(0L, "hotelCode", "hotelOptionCode", 0.0,
                "currency", false, RoomType.SINGLE, "image", new Integer[]{0});
        when(mockHotelBookItemRepository.findFirstById(any())).thenReturn(hotelBookableItem);

        // Configure HotelRepository.findFirstByCode(...).
        final Hotel hotel = new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0,
                new Integer[]{0}, null, "address", "userId", ApproveStatus.PENDING);
        when(mockHotelRepository.findFirstByCode(any())).thenReturn(hotel);

        // Configure HotelRepository.save(...).
        final Hotel hotel1 = new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0,
                new Integer[]{0}, null, "address", "userId", ApproveStatus.PENDING);
        when(mockHotelRepository.save(any(Hotel.class))).thenReturn(hotel1);

        // Configure HotelBookItemRepository.save(...).
        final HotelBookableItem hotelBookableItem1 = new HotelBookableItem(0L, "hotelCode", "hotelOptionCode", 0.0,
                "currency", false, RoomType.SINGLE, "image", new Integer[]{0});
        when(mockHotelBookItemRepository.save(any(HotelBookableItem.class))).thenReturn(hotelBookableItem1);

        // Run the test
        hotelServiceImplUnderTest.updateHotelRoom(hotelRoomUploadBody);

        // Verify the results
        verify(mockHotelRepository).save(any(Hotel.class));
        verify(mockHotelBookItemRepository).save(any(HotelBookableItem.class));
    }

    @Test
    void testUpdateHotelRoom_Success_Test() {
        // Setup
        final HotelRoomUploadBody hotelRoomUploadBody = new HotelRoomUploadBody(0L, new Integer[]{0}, "hotelCode",
                "currency", 0.0, "hotelOptionCode", "SINGLE", "imageRoom", null);

        // Configure HotelBookItemRepository.findFirstById(...).
        final HotelBookableItem hotelBookableItem = new HotelBookableItem(0L, "hotelCode", "hotelOptionCode", 0.0,
                "currency", false, RoomType.SINGLE, "image", new Integer[]{0});
        when(mockHotelBookItemRepository.findFirstById(any())).thenReturn(hotelBookableItem);

        // Configure HotelRepository.findFirstByCode(...).
        final Hotel hotel = new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0,
                new Integer[]{0}, 2.2, "address", "userId", ApproveStatus.PENDING);
        when(mockHotelRepository.findFirstByCode(any())).thenReturn(hotel);
        when(mockHotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        // Configure HotelBookItemRepository.save(...).
        final HotelBookableItem hotelBookableItem1 = new HotelBookableItem(0L, "hotelCode", "hotelOptionCode", 0.0,
                "currency", false, RoomType.SINGLE, "image", new Integer[]{0});
        when(mockHotelBookItemRepository.save(any(HotelBookableItem.class))).thenReturn(hotelBookableItem1);

        // Run the test
        hotelServiceImplUnderTest.updateHotelRoom(hotelRoomUploadBody);

        // Verify the results
        verify(mockHotelRepository).save(any(Hotel.class));
        verify(mockHotelBookItemRepository).save(any(HotelBookableItem.class));
    }

    @Test
    void testUpdateHotelRoom_HotelBookItemRepositoryFindFirstByIdReturnsNull() {
        // Setup
        final HotelRoomUploadBody hotelRoomUploadBody = new HotelRoomUploadBody(0L, new Integer[]{0}, "hotelCode",
                "currency", 0.0, "hotelOptionCode", "type", "imageRoom", null);
        when(mockHotelBookItemRepository.findFirstById(0L)).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> hotelServiceImplUnderTest.updateHotelRoom(hotelRoomUploadBody))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetListRoom_Success_Test() {
        try (MockedStatic<AuthenticationUtils> authenticationUtilsMockedStatic = mockStatic(AuthenticationUtils.class)
        ){
            final List<Amenities> amenities = List.of(new Amenities(0, "name", "icon"));
            authenticationUtilsMockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            when(mockAmenitiesRepository.findAll()).thenReturn(amenities);

            // Configure HotelBookItemRepository.findAllByHotelCode(...).
            final List<HotelBookableItem> hotelBookableItems = List.of(
                    new HotelBookableItem(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", false, RoomType.SINGLE,
                            "image", new Integer[]{0}));
            when(mockHotelBookItemRepository.findAllByHotelCode(any(),any())).thenReturn(hotelBookableItems);

            when(mockHotelBookItemRepository.countAllByHotelCode(any())).thenReturn(0);

            // Run the test
            final BaseResponse result = hotelServiceImplUnderTest.getListRoom("hotelCode", 2, 55);
            assertThat(result).isNotNull();
        }
    }

    @Test
    void testGetListRoom_Fail_Test() {
        assertThrows(GeneralException.class, () -> hotelServiceImplUnderTest.getListRoom("hotelCode", 2, 55));
    }
    @Test
    void testGetListHotel_Fail_Test() {
        assertThrows(GeneralException.class, () -> hotelServiceImplUnderTest.getListHotel(2, 55));
    }

    @Test
    void testGetListHotel_Success_Test() {
        try (MockedStatic<AuthenticationUtils> authenticationUtilsMockedStatic = mockStatic(AuthenticationUtils.class)
        ){
            final List<Hotel> hotels = List.of(
                    new Hotel("id", "hotelCode", "title", "description", "slug", HotelType.POPULAR, 0.0, 0, new Integer[]{0},
                            0.0, "address", "userId", ApproveStatus.PENDING));
            authenticationUtilsMockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            when(mockHotelRepository.findAllByUserIdOrderByCreatedAtDesc(any())).thenReturn(hotels);

            // Configure AmenitiesRepository.findAll(...).
            final List<Amenities> amenities = List.of(new Amenities(0, "name", "icon"));
            when(mockAmenitiesRepository.findAll()).thenReturn(amenities);
            when(mockHotelRepository.findByCodeInOrderByCreatedAtDesc(any(),any())).thenReturn(hotels);

            // Configure HotelImageRepository.findUniqueImage(...).
            final List<HotelImage> hotelImages = List.of(new HotelImage(0L, "hotelCode", "caption", "url", ImageType.ROOM));
            when(mockHotelImageRepository.findUniqueImage(any())).thenReturn(hotelImages);

            // Run the test
            final BaseResponse result = hotelServiceImplUnderTest.getListHotel(2, 55);
            assertThat(result).isNotNull();
        }
    }

    @Test
    void testDeleteHotel_Success_Test() {
        // Setup
        // Run the test
        hotelServiceImplUnderTest.deleteHotel("hotelCode");

        // Verify the results
        verify(mockHotelImageRepository).deleteAllByHotelCode("hotelCode");
        verify(mockHotelBookItemRepository).deleteAllByHotelCode("hotelCode");
        verify(mockHotelRepository).deleteAllByCode("hotelCode");
    }

    @Test
    void testDeleteHotelRoom_Success_Test() {
        // Setup
        // Run the test
        hotelServiceImplUnderTest.deleteHotelRoom(0L);

        // Verify the results
        verify(mockHotelBookItemRepository).deleteById(0L);
    }
}
