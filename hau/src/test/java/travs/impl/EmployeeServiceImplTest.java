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
import travs.repository.HotelImageRepository;
import travs.repository.HotelRepository;
import travs.repository.activities.ActivitiesGameRepository;
import travs.repository.activities.ActivitiesImageRepository;
import travs.repository.activities.ActivitiesRepository;
import travs.repository.restaurant.RestaurantImageRepository;
import travs.repository.restaurant.RestaurantMenuRepository;
import travs.repository.restaurant.RestaurantRepository;
import travs.request.hotel.HotelApproveRequest;
import travs.response.hotel.HotelDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.service.impl.EmployeeServiceImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {


    @Mock
    HotelRepository hotelRepository;
    @Mock
    AmenitiesRepository amenitiesRepository;
    @Mock
    HotelImageRepository hotelImageRepository;
    @Mock
    ActivitiesRepository activitiesRepository;
    @Mock
    ActivitiesGameRepository activitiesGameRepository;
    @Mock
    ActivitiesImageRepository activitiesImageRepository;
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    RestaurantMenuRepository restaurantMenuRepository;
    @Mock
    RestaurantImageRepository restaurantImageRepository;

    @InjectMocks
    EmployeeServiceImpl employeeServiceImpl;

    String userId = "1";

    Integer[] amenitiesInt  = {1};

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

    String hotelName = "";

    List<Amenities> amenitiesList = Arrays.asList(amenities);

    ApproveStatus status = ApproveStatus.APPROVED;


    @Test
    public void testGetListHotel() {
        List<Hotel> hotelList = Arrays.asList(hotel);

        Mockito.when(hotelRepository.findAllHotel()).thenReturn(hotelList);

        Mockito.when(hotelRepository.findHotelByApproveStatusOrderByCreatedAtDesc(Mockito.any())).thenReturn(hotelList);

        Mockito.when(hotelRepository.findAllByTitle(Mockito.any())).thenReturn(hotelList);

        Mockito.when(hotelRepository.findAllByTitle(Mockito.any())).thenReturn(hotelList);

        Mockito.when(hotelRepository.findAllByTitleAndApproveStatus(Mockito.any(), Mockito.any())).thenReturn(hotelList);

        List<String> productCodes = hotelList.stream().map(Hotel::getCode).collect(Collectors.toList());

        Mockito.when(amenitiesRepository.findAll()).thenReturn(amenitiesList);

        Map<Integer, Amenities> map = amenitiesList.stream().collect(Collectors.toMap(Amenities::getId, Function.identity()));

//        List<Hotel> hotelListQuery = hotelRepository.findByCodeInOrderByCreatedAtDesc(productCodes, PageRequest.of(1, 5));
//        List<HotelDTO> hotels = MappingUtils.map(hotelListQuery, HotelDTO.class);
//        hotels.forEach(hotel -> hotel.setFacilityDTOList(hotel.getAmenities()
//                .stream().map(amenitiesId -> FacilityDTO.builder()
//                        .id(amenitiesId.toString())
//                        .name(map.get(amenitiesId).getName())
//                        .icon(map.get(amenitiesId).getIcon())
//                        .build())
//                .collect(Collectors.toList())));
//        List<HotelImage> hotelImages = hotelImageRepository.findUniqueImage(productCodes);
//        Map<String, String> mapImage = hotelImages.stream().collect(Collectors.toMap(HotelImage::getHotelCode, HotelImage::getUrl));
//
//        hotels.forEach(hotel -> {
//            String imageUrl = mapImage.get(hotel.getCode());
//            if (imageUrl != null) {
//                hotel.setImage(imageUrl);
//            }
//        });

        Map<String, Integer> mapReturn = new HashMap<>();
        mapReturn.put("total", productCodes.size());
//         BaseResponse.ok(hotels, mapReturn);

        employeeServiceImpl.getListHotel("",status,1,5);
        employeeServiceImpl.getListHotel("a",null,1,5);
        employeeServiceImpl.getListHotel("a",status,1,5);

    }

     HotelApproveRequest hotelApproveRequest = new HotelApproveRequest("hotel-code",ApproveStatus.APPROVED );

    @Test
    public void testApproveHotel() {
         Mockito.when(hotelRepository.findFirstByCode(Mockito.any())).thenReturn(hotel);

        hotel.setApproveStatus(hotelApproveRequest.getStatus());

        Mockito.when(hotelRepository.save(hotel)).thenReturn(hotel);

        employeeServiceImpl.approveHotel(hotelApproveRequest);

    }





}
