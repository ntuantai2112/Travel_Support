package travs.impl;

import travs.common.HotelType;
import travs.common.type.ApproveStatus;
import travs.common.type.ImageType;
import travs.common.type.RoomType;
import travs.entity.hotel.Amenities;
import travs.entity.hotel.Hotel;
import travs.entity.hotel.HotelBookableItem;
import travs.entity.hotel.HotelImage;
import travs.repository.AmenitiesRepository;
import travs.repository.FavoriteRepository;
import travs.repository.HotelBookItemRepository;
import travs.repository.HotelImageRepository;
import travs.repository.HotelRepository;
import travs.response.BaseResponse;
import travs.response.hotel.FacilityDTO;
import travs.response.hotel.HotelDTO;
import travs.response.hotel.HotelDetail;
import travs.response.hotel.HotelRoomDTO;
import travs.response.hotel.ImageDTO;
import travs.service.impl.HotelServiceImpl;
import travs.utils.AuthenticationUtils;
import travs.utils.MappingUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class HotelServiceImplTest {

    @Mock
    HotelRepository hotelRepository;
    @Mock
    HotelBookItemRepository hotelBookItemRepository;
    @Mock
    HotelImageRepository hotelImageRepository;
    @Mock
    FavoriteRepository favoriteRepository;
    @Mock
    AmenitiesRepository amenitiesRepository;

    @InjectMocks
    HotelServiceImpl hotelServiceImpl;

    String userId = "1";

    Integer[] amenitiesInt = {1};

    List<Integer> listamenitiesInt = Arrays.asList(1);


    Hotel hotel = Hotel.builder()
            .hotelType(HotelType.FAMILY)
            .id("1")
            .title("nh? h?ng")
            .address("huhu")
            .description("aaaaa")
            .code("hotel-code")
            .approveStatus(ApproveStatus.APPROVED)
            .userId(userId)
            .amenities(amenitiesInt)
            .slug("hotel-slug")
            .build();

    HotelDTO hotelDTO = HotelDTO.builder()
            .address("ha noi")
            .approveStatus(ApproveStatus.APPROVED)
            .code("hotel-code")
            .description("description")
            .title("oki")
            .description("aaaaa")
            .image("image.jpg")
            .hotelType(HotelType.FAMILY)
            .slug("hotel-slug")
            .build();

    Amenities amenities = Amenities.builder()
            .icon("aaaaavvvv")
            .id(1)
            .name("aaaa")
            .build();

    HotelBookableItem hotelBookableItem = HotelBookableItem.builder()
            .price(1111D)
            .currency("VND")
            .hotelCode("hotel-code")
            .roomType(RoomType.SINGLE)
            .available(true)
            .amenities(amenitiesInt)
            .hotelOptionCode("aaaaaaaaa")
            .image("aaaaaaaaa.jpg")
            .build();

    HotelImage hotelImage = HotelImage.builder()
            .hotelCode("hotel-code")
            .caption("aaaaaaa")
            .imageType(ImageType.FOOD)
            .url("aaaaaaaaaaa")
            .build();


    FacilityDTO facilityDTO = FacilityDTO.builder()
            .name("aaaa")
            .id("1")
            .icon("aaaaavvvv")
            .build();

    List<FacilityDTO> facilityDTOList = Arrays.asList(facilityDTO);

    HotelRoomDTO hotelRoomDTO = HotelRoomDTO.builder()
            .id(1L)
            .price(1111D)
            .currency("VND")
            .hotelCode("hotel-code")
            .roomType(RoomType.SINGLE)
            .available(true)
            .facilityDTOList(facilityDTOList)
            .amenities(listamenitiesInt)
            .hotelOptionCode("aaaaaaaaa")
            .image("aaaaaaaaa.jpg")
            .build();

    List<HotelDTO> hotelListDTOs = Arrays.asList(hotelDTO);
    List<Hotel> hotelList = Arrays.asList(hotel);
    List<Amenities> amenitiesList = Arrays.asList(amenities);

    List<HotelBookableItem> bookableItems = Arrays.asList(hotelBookableItem);

    List<HotelImage> hotelImages = Arrays.asList(hotelImage);

    List<HotelRoomDTO> roomDTOList = Arrays.asList(hotelRoomDTO);


    @Test
    public void testGet() {

        Mockito.when(hotelRepository.findFirstBySlug(Mockito.any())).thenReturn(Optional.ofNullable(hotel));
        Mockito.when(amenitiesRepository.findAll()).thenReturn(amenitiesList);
        Map<Integer, Amenities> map = amenitiesList.stream().collect(Collectors.toMap(Amenities::getId, Function.identity()));
        List<FacilityDTO> facilityDTOList = buildAmenities(hotel, map);

        HotelDetail hotelDetail = HotelDetail.builder()
                .id(hotel.getId())
                .title(hotel.getTitle())
                .slug(hotel.getSlug())
                .code(hotel.getCode())
                .address(hotel.getAddress())
                .price(hotel.getMinPrice())
                .description(hotel.getDescription())
                .price(hotel.getMinPrice())
                .isFavorite(this.checkFavoriteHotel(hotel.getSlug()))
                .facilityDTOList(facilityDTOList)
                .build();
        buildImages(hotelDetail);

         Mockito.when(hotelBookItemRepository.findAllByHotelCodeOrderByPriceAsc(Mockito.anyString())).thenReturn(bookableItems);

        roomDTOList.forEach(room -> room.setFacilityDTOList(room.getAmenities()
                .stream().map(amenitiesId -> FacilityDTO.builder()
                        .id(amenitiesId.toString())
                        .name(map.get(amenitiesId).getName())
                        .icon(map.get(amenitiesId).getIcon())
                        .build())
                .collect(Collectors.toList())));

        BaseResponse.ok(hotelDetail, roomDTOList);
        hotelServiceImpl.get("hotel-slug");
    }


    private List<FacilityDTO> buildAmenities(Hotel hotel, Map<Integer, Amenities> map) {
        List<Integer> amenitiesInt = Arrays.asList(hotel.getAmenities());

        return amenitiesInt.stream().map(amenitiesId -> FacilityDTO.builder()
                .id(amenitiesId.toString())
                .name(map.get(amenitiesId).getName())
                .icon(map.get(amenitiesId).getIcon())
                .build()).collect(Collectors.toList());
    }

    //
    private void buildImages(HotelDetail hotelDetail) {

        Mockito.when(hotelImageRepository.findAllByHotelCode(Mockito.anyString())).thenReturn(hotelImages);

        hotelDetail.setImages(MappingUtils.map(hotelImages, ImageDTO.class));
    }

    //
    private boolean checkFavoriteHotel(String slug) {
        String userId = AuthenticationUtils.getUserId();
        if (userId != null) {
            return favoriteRepository.findByUserIdAndSlug(userId, slug).isPresent();
        }
        return false;
    }


}
