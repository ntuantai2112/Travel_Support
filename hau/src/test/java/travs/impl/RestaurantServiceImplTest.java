package travs.impl;


import travs.common.RestaurantType;
import travs.common.type.ApproveStatus;
import travs.common.type.ImageType;
import travs.entity.BookingContact;
import travs.entity.account.Account;
import travs.entity.account.Role;
import travs.entity.restaurant.Feature;
import travs.entity.restaurant.Restaurant;
import travs.entity.restaurant.RestaurantImage;
import travs.entity.restaurant.RestaurantMenu;
import travs.model.Contact;
import travs.repository.restaurant.FeatureRepository;
import travs.repository.restaurant.RestaurantImageRepository;
import travs.repository.restaurant.RestaurantMenuRepository;
import travs.repository.restaurant.RestaurantRepository;
import travs.request.restaurant.BookingRestaurantRequest;
import travs.request.restaurant.RestaurantUploadRequest;
import travs.response.BaseResponse;
import travs.response.hotel.FacilityDTO;
import travs.response.restaurant.RestaurantDetail;
import travs.response.restaurant.RestaurantMenuDTO;
import travs.service.impl.HelperService;
import travs.service.impl.restaurant.RestaurantServiceImpl;
import travs.utils.AuthenticationUtils;
import travs.utils.HelperUtils;
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
public class RestaurantServiceImplTest {
    @Mock
    private HelperService helperService;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RestaurantImageRepository restaurantImageRepository;
    @Mock
    private RestaurantMenuRepository restaurantMenuRepository;
    @Mock
    private FeatureRepository featureRepository;

    @InjectMocks
    RestaurantServiceImpl restaurantServiceImpl;

    Integer[] feature = {1};

    String userId = "1";

    Restaurant restaurant = new Restaurant("1", "restaurant-code", "nha han"
            , "ngon", "slug-restaurant", RestaurantType.NHA_HANG,
            feature, "a", userId, ApproveStatus.PENDING);

    Account account = new Account("1", "tuan", "travalar@gmail.com"
            , "0983302976", true, "123456", "abc.jpg",
            "05-06-99", true, new Role(1L, "ROLE_ADMIN"), "a");

    Contact contact = new Contact("trabalar", "nguyen",
            "travalar@123", "0983302922");

    BookingContact bookingContact = new BookingContact("1", "tuan", "nguyen",
            "travalar@123", "0983302922", userId);

    BookingRestaurantRequest bookingRestaurantRequest =
            new BookingRestaurantRequest("restaurant-code", contact,
                    21001212, "2200", 2, 3);
    String bookingId = HelperUtils.genBookingID();

    List<String> restaurantImages = Arrays.asList("aaaaaaaaaaaaaaa.jpg");

    Feature feature1 = new Feature(1, "a");

    List<Feature> features = Arrays.asList(feature1);

    RestaurantMenu restaurantMenu = new RestaurantMenu(1L, "restaurant-code", 2000D,
            "VND", "ngon", "ech", true, "a.jpg");

    List<RestaurantMenu> menuList = Arrays.asList(restaurantMenu);

    @Test
    public void testGet() {

//        PowerMockito.mockStatic(AuthenticationUtils.class);
//        Mockito.when(AuthenticationUtils.getUserId()).thenReturn(userId);

        Mockito.when(restaurantRepository.findFirstBySlug(Mockito.anyString())).thenReturn(Optional.ofNullable(restaurant));

        Mockito.when(featureRepository.findAll()).thenReturn(features);

        Map<Integer, Feature> map = features.stream().collect(Collectors.toMap(Feature::getId, Function.identity()));

        List<FacilityDTO> facilityDTOList = buildFavorite(restaurant, map);

        Mockito.when(restaurantMenuRepository.findAllByRestaurantCode(Mockito.anyString())).thenReturn(menuList);


        RestaurantDetail restaurantDetail = RestaurantDetail.builder()
                .id(restaurant.getId())
                .title(restaurant.getTitle())
                .slug(restaurant.getSlug())
                .code(restaurant.getCode())
                .address(restaurant.getAddress())
                .facilityDTOList(facilityDTOList)
                .description(restaurant.getDescription())
                .isFavorite(helperService.checkFavorite(restaurant.getSlug()))
                .restaurantMenuDTO(MappingUtils.map(menuList, RestaurantMenuDTO.class))
                .build();

        BaseResponse.ok(restaurantDetail);
        restaurantServiceImpl.get("slug-restaurant");

    }

    List<String> images = Arrays.asList("aaaaaaaa.jpg");

    RestaurantUploadRequest restaurantUploadRequest = RestaurantUploadRequest.builder()
            .feature(feature)
            .description("hay")
            .restaurantType(RestaurantType.NHA_HANG)
            .address("ha noi")
            .images(images)
            .restaurantCode("restaurant-code")
            .title("nha hang")
            .build();


    RestaurantImage restaurantImage = new RestaurantImage(1L, "restauran-code", "caption", "image", ImageType.FOOD);

    List<RestaurantImage> restaurantImages1 = Arrays.asList(restaurantImage);

    @Test
    public void testUploadRestaurant() {

        //PowerMockito.mockStatic(AuthenticationUtils.class);
        Mockito.when(AuthenticationUtils.getUserId()).thenReturn(userId);

        Mockito.when(restaurantImageRepository.saveAll(restaurantImages1)).thenReturn(restaurantImages1);

        // lưu restaurant vao restaurant
        Restaurant restaurant = Restaurant.builder()
                .slug(HelperUtils.toSlug(restaurantUploadRequest.getTitle() + "restaurant-code"))
                .feature(restaurantUploadRequest.getFeature())
                .description(restaurantUploadRequest.getDescription())
                .title(restaurantUploadRequest.getTitle())
                .address(restaurantUploadRequest.getAddress())
                .restaurantType((restaurantUploadRequest.getRestaurantType()))
                .code("restaurant-code")
                .userId(userId)
                .approveStatus(ApproveStatus.PENDING)
                .build();

        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Mockito.doNothing().when(restaurantRepository).updateSearchVector("restaurant-code");


        BaseResponse.ok(restaurant);

//        restaurantServiceImpl.uploadRestaurant(restaurantUploadRequest);


    }


    private List<FacilityDTO> buildFavorite(Restaurant restaurant, Map<Integer, Feature> map) {
        List<Integer> favorite = Arrays.asList(restaurant.getFeature());
        return favorite.stream().map(favoriteId -> FacilityDTO.builder()
                .id(favoriteId.toString())
                .name(map.get(favoriteId).getName())
                .build()).collect(Collectors.toList());
    }

}
