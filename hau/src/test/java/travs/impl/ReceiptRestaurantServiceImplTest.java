package travs.impl;

import org.junit.jupiter.api.Test;
import travs.common.RestaurantType;
import travs.common.type.ApproveStatus;
import travs.common.type.BookingStatus;
import travs.dao.AccountDAO;
import travs.entity.BookingContact;
import travs.entity.account.Account;
import travs.entity.account.Role;
import travs.entity.restaurant.Restaurant;
import travs.entity.restaurant.RestaurantBookingReceipt;
import travs.entity.restaurant.RestaurantMenu;
import travs.model.Contact;
import travs.repository.FavoriteRepository;
import travs.repository.restaurant.RestaurantBookingReceiptRepository;
import travs.repository.restaurant.RestaurantImageRepository;
import travs.repository.restaurant.RestaurantMenuRepository;
import travs.repository.restaurant.RestaurantRepository;
import travs.request.restaurant.BookingRestaurantRequest;
import travs.request.restaurant.RestaurantApproveBookingRequest;
import travs.response.BaseResponse;
import travs.response.restaurant.RestaurantBookingReceiptDTO;
import travs.response.restaurant.RestaurantInfoDTO;
import travs.response.restaurant.RestaurantMenuDTO;
import travs.service.EmailService;
import travs.service.impl.restaurant.ReceiptRestaurantServiceImpl;
import travs.utils.AuthenticationUtils;
import travs.utils.HelperUtils;
import travs.utils.MappingUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
public class ReceiptRestaurantServiceImplTest {
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    RestaurantImageRepository restaurantImageRepository;
    @Mock
    RestaurantMenuRepository restaurantMenuRepository;
    @Mock
    RestaurantBookingReceiptRepository restaurantBookingReceiptRepository;
    @Mock
    FavoriteRepository favoriteRepository;
    @Mock
    EmailService emailService;
    @Mock
    AccountDAO accountDAO;

    @InjectMocks
    ReceiptRestaurantServiceImpl receiptRestaurantServiceImpl;

    Integer[] feature = {1, 2};

    String userId = "1";

    Restaurant restaurant = new Restaurant("1", "restaurant-code", "nha han"
            , "ngon", "slug-restaurant", RestaurantType.NHA_HANG,
            feature, "a", userId, ApproveStatus.PENDING);

    Account account = new Account("1", "tuan", "tuan@gmail.com"
            , "0983302976", true, "123456", "abc.jpg",
            "05-06-99", true, new Role(1L, "ROLE_ADMIN"), "a");

    Contact contact = new Contact("tuan", "nguyen",
            "tuan@123", "0983302922");

    BookingContact bookingContact = new BookingContact("1", "tuan", "nguyen",
            "tuan@123", "0983302922", userId);

    BookingRestaurantRequest bookingRestaurantRequest =
            new BookingRestaurantRequest("restaurant-code", contact,
                    21001212, "2200", 2, 3);
    String bookingId = HelperUtils.genBookingID();

    List<String> restaurantImages = Arrays.asList("aaaaaaaaaaaaaaa.jpg");

    @Test
    public void testBookingRestaurant() {
        //PowerMockito.mockStatic(AuthenticationUtils.class);
        Mockito.when(AuthenticationUtils.getUserId()).thenReturn(userId);

        Mockito.when(restaurantRepository.findFirstByCode(Mockito.anyString())).thenReturn(restaurant);

        Mockito.when(accountDAO.getAccountById(Mockito.anyString())).thenReturn(account);

        Mockito.when(restaurantImageRepository.findAllByRestaurantCode(Mockito.anyString())).thenReturn(restaurantImages);

        RestaurantInfoDTO restaurantInfoDTO = MappingUtils.map(restaurant, RestaurantInfoDTO.class);
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAllByRestaurantCode(restaurant.getCode());

        restaurantInfoDTO.setImagesList(restaurantImages);
        restaurantInfoDTO.setMenuDTOList(MappingUtils.map(restaurantMenuList, RestaurantMenuDTO.class));

        RestaurantBookingReceipt receipt = RestaurantBookingReceipt.builder()
                .bookingId(bookingId)
                .restaurantInfoDTO(restaurantInfoDTO)
                .contact(bookingContact)
                .userId(userId)
                .phonePartner(account.getPhone())
                .partnerId(restaurant.getUserId())
                .numberChild(bookingRestaurantRequest.getNumberChild())
                .numberAdult(bookingRestaurantRequest.getNumberAdult())
                .checkinDay(bookingRestaurantRequest.getCheckinDate())
                .checkinTime(bookingRestaurantRequest.getCheckinTime())
                .status(BookingStatus.BOOKING_PENDING)
                .build();

        receipt = restaurantBookingReceiptRepository.save(receipt);
        BaseResponse.ok(MappingUtils.map(receipt, RestaurantBookingReceiptDTO.class));

        receiptRestaurantServiceImpl.bookingRestaurant(bookingRestaurantRequest);

    }

    RestaurantInfoDTO restaurantInfoDTO = MappingUtils.map(restaurant, RestaurantInfoDTO.class);

    RestaurantBookingReceipt restaurantBookingReceipt = new
            RestaurantBookingReceipt("1", bookingId, bookingContact, BookingStatus.BOOKING_APPROVED,
            20211212, "2200", 2, 3, restaurantInfoDTO,
            account.getPhone(), "1", userId);

    List<RestaurantBookingReceipt> receiptList = Arrays.asList(restaurantBookingReceipt);
    int total = 0;

    //    BookingStatus status, Integer page, Integer perPage

    String partnerId = "2";

    @Test
    public void testGetListRestaurantReceipt() {
        //PowerMockito.mockStatic(AuthenticationUtils.class);
        Mockito.when(AuthenticationUtils.getUserId()).thenReturn(userId);

        Mockito.when(restaurantBookingReceiptRepository.findAllByPartnerIdOrderByCreatedAtDesc(userId, PageRequest.of(1, 5))).thenReturn(receiptList);
        Mockito.when(restaurantBookingReceiptRepository.countAllByPartnerId(Mockito.anyString())).thenReturn(total);

        Map<String, Integer> mapReturn = new HashMap<>();
        BaseResponse.ok(MappingUtils.map(receiptList, RestaurantBookingReceiptDTO.class), mapReturn);

        receiptRestaurantServiceImpl.getListRestaurantReceipt(null,1,5);
        receiptRestaurantServiceImpl.getListRestaurantReceipt(BookingStatus.BOOKING_APPROVED,1,5);

    }

    RestaurantApproveBookingRequest restaurantApproveBookingRequest = new
            RestaurantApproveBookingRequest(bookingId,BookingStatus.BOOKING_APPROVED);

    RestaurantBookingReceipt receipt = RestaurantBookingReceipt.builder()
            .bookingId(bookingId)
            .restaurantInfoDTO(restaurantInfoDTO)
            .contact(bookingContact)
            .userId(userId)
            .phonePartner(account.getPhone())
            .partnerId(restaurant.getUserId())
            .numberChild(bookingRestaurantRequest.getNumberChild())
            .numberAdult(bookingRestaurantRequest.getNumberAdult())
            .checkinDay(bookingRestaurantRequest.getCheckinDate())
            .checkinTime(bookingRestaurantRequest.getCheckinTime())
            .status(BookingStatus.BOOKING_PENDING)
            .build();

    @Test
    public void testRestaurantApproStatusRepositoryUserId() {


        Mockito.when(restaurantBookingReceiptRepository.findFirstByBookingId(Mockito.anyString())).thenReturn(receipt);
        Mockito.when(restaurantBookingReceiptRepository.save(receipt)).thenReturn(receipt);

         receiptRestaurantServiceImpl.restaurantApproStatusRepositoryUserId(restaurantApproveBookingRequest);

    }


    @Test
    public void testGetReceiptRestaurantDetail() {

        Mockito.when(restaurantBookingReceiptRepository.findFirstById(Mockito.anyString())).thenReturn(receipt);
         BaseResponse.ok(MappingUtils.map(receipt, RestaurantBookingReceiptDTO.class));

        receiptRestaurantServiceImpl.getReceiptRestaurantDetail("1");

    }

}
