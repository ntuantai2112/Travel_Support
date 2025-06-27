package travs.service.impl.restaurant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
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
import travs.exception.GeneralException;
import travs.exception.ResourceNotFoundException;
import travs.model.Contact;
import travs.repository.FavoriteRepository;
import travs.repository.restaurant.RestaurantBookingReceiptRepository;
import travs.repository.restaurant.RestaurantImageRepository;
import travs.repository.restaurant.RestaurantMenuRepository;
import travs.repository.restaurant.RestaurantRepository;
import travs.request.restaurant.BookingRestaurantRequest;
import travs.request.restaurant.RestaurantApproveBookingRequest;
import travs.response.BaseResponse;
import travs.response.restaurant.RestaurantInfoDTO;
import travs.response.restaurant.RestaurantMenuDTO;
import travs.service.EmailService;
import travs.utils.AuthenticationUtils;

import javax.mail.MessagingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptRestaurantServiceImplTest {

    @Mock
    private RestaurantRepository mockRestaurantRepository;
    @Mock
    private RestaurantImageRepository mockRestaurantImageRepository;
    @Mock
    private RestaurantMenuRepository mockRestaurantMenuRepository;
    @Mock
    private RestaurantBookingReceiptRepository mockRestaurantBookingReceiptRepository;
    @Mock
    private FavoriteRepository mockFavoriteRepository;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private AccountDAO mockAccountDAO;
    @InjectMocks
    private ReceiptRestaurantServiceImpl receiptRestaurantServiceImplUnderTest;
    private MockedStatic<AuthenticationUtils> mockedStatic;

    @BeforeEach
    void setUp() {
        mockedStatic = mockStatic(AuthenticationUtils.class);
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @Test
    void testBookingRestaurant_Success_Test() {
        // Setup
        final BookingRestaurantRequest bookingRestaurantRequest = new BookingRestaurantRequest("restaurantCode",
                new Contact("firstName", "lastName", "email", "phone"), 0, "checkinTime", 0, 0);

        // Configure RestaurantRepository.findFirstByCode(...).
        final Restaurant restaurant = new Restaurant("id", "code", "title", "description", "slug",
                RestaurantType.NHA_HANG, new Integer[]{0}, "address", "partnerId", ApproveStatus.PENDING);
        when(mockRestaurantRepository.findFirstByCode("restaurantCode")).thenReturn(restaurant);

        // Configure AccountDAO.getAccountById(...).
        final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false,
                new Role(0L, "name"), "resetPasswordToken");
        when(mockAccountDAO.getAccountById("partnerId")).thenReturn(account);
        this.mockedStatic.when(() -> AuthenticationUtils.getUserId()).thenReturn("userId");
        when(mockRestaurantImageRepository.findAllByRestaurantCode("code")).thenReturn(List.of("value"));

        // Configure RestaurantMenuRepository.findAllByRestaurantCode(...).
        final List<RestaurantMenu> restaurantMenus = List.of(
                new RestaurantMenu(0L, "restaurantCode", 0.0, "currency", "description", "name", false, "image"));
        when(mockRestaurantMenuRepository.findAllByRestaurantCode("code")).thenReturn(restaurantMenus);

        // Configure RestaurantBookingReceiptRepository.save(...).
        final RestaurantBookingReceipt restaurantBookingReceipt = new RestaurantBookingReceipt("id", "bookingId",
                new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                        RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                        List.of("value"),
                        List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))), "phone",
                "partnerId", "userId");
        when(mockRestaurantBookingReceiptRepository.save(any(RestaurantBookingReceipt.class)))
                .thenReturn(restaurantBookingReceipt);

        // Run the test
        final BaseResponse result = receiptRestaurantServiceImplUnderTest.bookingRestaurant(bookingRestaurantRequest);

        // Verify the results
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isNotNull();
    }

    @Test
    void testBookingRestaurant_Fail_Test() {
        // Setup
        final BookingRestaurantRequest bookingRestaurantRequest = new BookingRestaurantRequest("restaurantCode",
                new Contact("firstName", "lastName", "email", "phone"), 0, "checkinTime", 0, 0);

        this.mockedStatic.when(() -> AuthenticationUtils.getUserId()).thenReturn(null);
        Assertions.assertThrows(GeneralException.class, () -> {
            receiptRestaurantServiceImplUnderTest.bookingRestaurant(bookingRestaurantRequest);
        });
    }

    @Test
    void testGetListRestaurantReceipt_SuccessTest() {
        // Setup
        this.mockedStatic.when(() -> AuthenticationUtils.getUserId()).thenReturn("userId");

        // Configure RestaurantBookingReceiptRepository.findAllByPartnerIdAndStatusOrderByCreatedAtDesc(...).
        final List<RestaurantBookingReceipt> restaurantBookingReceipts1 = List.of(
                new RestaurantBookingReceipt("id", "bookingId",
                        new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                        BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                        new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                                RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                                List.of("value"),
                                List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))),
                        "phone", "partnerId", "userId"));
        when(mockRestaurantBookingReceiptRepository.findAllByPartnerIdAndStatusOrderByCreatedAtDesc(any(), any(), any())).thenReturn(restaurantBookingReceipts1);

        when(mockRestaurantBookingReceiptRepository.countAllByPartnerIdAndStatus(any(), any())).thenReturn(0);

        // Run the test
        final BaseResponse result = receiptRestaurantServiceImplUnderTest.getListRestaurantReceipt(
                BookingStatus.BOOKING_PENDING, 1, 2);
        // Verify the results
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getError()).isNull();
    }

    @Test
    void testGetListRestaurantReceipt_StatusNull_SuccessTest() {
        this.mockedStatic.when(() -> AuthenticationUtils.getUserId()).thenReturn("userId");

        // Configure RestaurantBookingReceiptRepository.findAllByPartnerIdAndStatusOrderByCreatedAtDesc(...).
        final List<RestaurantBookingReceipt> restaurantBookingReceipts1 = List.of(
                new RestaurantBookingReceipt("id", "bookingId",
                        new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                        BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                        new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                                RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                                List.of("value"),
                                List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))),
                        "phone", "partnerId", "userId"));
        // Run the test
        final BaseResponse result = receiptRestaurantServiceImplUnderTest.getListRestaurantReceipt(
                null, 1, 2);
        // Verify the results
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getError()).isNull();
    }

    @Test
    void testGetListRestaurantReceipt_Fail_Test() {
        this.mockedStatic.when(() -> AuthenticationUtils.getUserId()).thenReturn(null);
        // Run the test
        Assertions.assertThrows(GeneralException.class, () -> {
            receiptRestaurantServiceImplUnderTest.getListRestaurantReceipt(
                    null, 1, 2);
        });
    }

    @Test
    void testGetListRestaurantReceiptByUserId_Success_Test() {
        // Configure RestaurantBookingReceiptRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(...).
        final List<RestaurantBookingReceipt> restaurantBookingReceipts = List.of(
                new RestaurantBookingReceipt("id", "bookingId",
                        new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                        BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                        new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                                RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                                List.of("value"),
                                List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))),
                        "phone", "partnerId", "userId"));
        when(mockRestaurantBookingReceiptRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(any(), any(), any())).thenReturn(restaurantBookingReceipts);
        this.mockedStatic.when(() -> AuthenticationUtils.getUserId()).thenReturn("userId");
        when(mockRestaurantBookingReceiptRepository.countAllByUserIdAndStatus("userId",
                BookingStatus.BOOKING_PENDING)).thenReturn(0);

        // Run the test
        final BaseResponse result = receiptRestaurantServiceImplUnderTest.getListRestaurantReceiptByUserId(
                BookingStatus.BOOKING_PENDING, 1, 2);

        // Verify the results
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void testGetListRestaurantReceiptByUserId_Fail_Test() {
        this.mockedStatic.when(() -> AuthenticationUtils.getUserId()).thenReturn(null);

        Assertions.assertThrows(GeneralException.class, () -> {
            receiptRestaurantServiceImplUnderTest.getListRestaurantReceiptByUserId(
                    BookingStatus.BOOKING_PENDING, 1, 2);
        });
    }

    @Test
    void testGetListRestaurantReceiptByUserId_StatusNull_Success_Test() {
        // Configure RestaurantBookingReceiptRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(...).
        final List<RestaurantBookingReceipt> restaurantBookingReceipts = List.of(
                new RestaurantBookingReceipt("id", "bookingId",
                        new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                        BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                        new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                                RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                                List.of("value"),
                                List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))),
                        "phone", "partnerId", "userId"));
        this.mockedStatic.when(() -> AuthenticationUtils.getUserId()).thenReturn("userId");

        // Run the test
        final BaseResponse result = receiptRestaurantServiceImplUnderTest.getListRestaurantReceiptByUserId(
                null, 1, 2);

        // Verify the results
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void testGetReceiptRestaurantDetail() {
        // Setup
        // Configure RestaurantBookingReceiptRepository.findFirstById(...).
        final RestaurantBookingReceipt restaurantBookingReceipt = new RestaurantBookingReceipt("id", "bookingId",
                new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                        RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                        List.of("value"),
                        List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))), "phone",
                "partnerId", "userId");
        when(mockRestaurantBookingReceiptRepository.findFirstById("id")).thenReturn(restaurantBookingReceipt);

        // Run the test
        final BaseResponse result = receiptRestaurantServiceImplUnderTest.getReceiptRestaurantDetail("id");

        // Verify the results
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void testRestaurantApproStatusRepository() throws Exception {
        // Setup
        final RestaurantApproveBookingRequest restaurantApproveBookingRequest = new RestaurantApproveBookingRequest(
                "bookingId", BookingStatus.BOOKING_PENDING);

        // Configure RestaurantBookingReceiptRepository.findFirstByBookingId(...).
        final RestaurantBookingReceipt restaurantBookingReceipt = new RestaurantBookingReceipt("id", "bookingId",
                new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                        RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                        List.of("value"),
                        List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))), "phone",
                "partnerId", "userId");
        when(mockRestaurantBookingReceiptRepository.findFirstByBookingId("bookingId"))
                .thenReturn(restaurantBookingReceipt);

        // Configure RestaurantBookingReceiptRepository.save(...).
        final RestaurantBookingReceipt restaurantBookingReceipt1 = new RestaurantBookingReceipt("id", "bookingId",
                new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                        RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                        List.of("value"),
                        List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))), "phone",
                "partnerId", "userId");
        when(mockRestaurantBookingReceiptRepository.save(any(RestaurantBookingReceipt.class)))
                .thenReturn(restaurantBookingReceipt1);

        // Run the test
        receiptRestaurantServiceImplUnderTest.restaurantApproStatusRepository(restaurantApproveBookingRequest);

        // Verify the results
        verify(mockEmailService).sendSimpleMessage(any(), any(), any());
        verify(mockRestaurantBookingReceiptRepository).save(any(RestaurantBookingReceipt.class));
    }

    @Test
    void testRestaurantApproStatusRepository_Success_Test() throws Exception {
        // Setup
        final RestaurantApproveBookingRequest restaurantApproveBookingRequest = new RestaurantApproveBookingRequest(
                "bookingId", BookingStatus.BOOKING_APPROVED);

        // Configure RestaurantBookingReceiptRepository.findFirstByBookingId(...).
        final RestaurantBookingReceipt restaurantBookingReceipt = new RestaurantBookingReceipt("id", "bookingId",
                new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                BookingStatus.BOOKING_PENDING, 20020110, "checkinTime", 0, 0,
                new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                        RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                        List.of("value"),
                        List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))), "phone",
                "partnerId", "userId");
        when(mockRestaurantBookingReceiptRepository.findFirstByBookingId("bookingId"))
                .thenReturn(restaurantBookingReceipt);

        // Configure RestaurantBookingReceiptRepository.save(...).
        final RestaurantBookingReceipt restaurantBookingReceipt1 = new RestaurantBookingReceipt("id", "bookingId",
                new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                        RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                        List.of("value"),
                        List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))), "phone",
                "partnerId", "userId");
        when(mockRestaurantBookingReceiptRepository.save(any(RestaurantBookingReceipt.class)))
                .thenReturn(restaurantBookingReceipt1);

        // Run the test
        receiptRestaurantServiceImplUnderTest.restaurantApproStatusRepository(restaurantApproveBookingRequest);

        // Verify the results
        verify(mockEmailService).sendSimpleMessage(any(), any(), any());
        verify(mockRestaurantBookingReceiptRepository).save(any(RestaurantBookingReceipt.class));
    }

    @Test
    void testRestaurantApproStatusRepository_RestaurantBookingReceiptRepositoryFindFirstByBookingIdReturnsNull() {
        // Setup
        final RestaurantApproveBookingRequest restaurantApproveBookingRequest = new RestaurantApproveBookingRequest(
                "bookingId", BookingStatus.BOOKING_PENDING);
        when(mockRestaurantBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> receiptRestaurantServiceImplUnderTest.restaurantApproStatusRepository(
                restaurantApproveBookingRequest)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testRestaurantApproStatusRepository_EmailServiceThrowsMessagingException() throws Exception {
        // Setup
        final RestaurantApproveBookingRequest restaurantApproveBookingRequest = new RestaurantApproveBookingRequest(
                "bookingId", BookingStatus.BOOKING_PENDING);

        // Configure RestaurantBookingReceiptRepository.findFirstByBookingId(...).
        final RestaurantBookingReceipt restaurantBookingReceipt = new RestaurantBookingReceipt("id", "bookingId",
                new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                        RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                        List.of("value"),
                        List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))), "phone",
                "partnerId", "userId");
        when(mockRestaurantBookingReceiptRepository.findFirstByBookingId("bookingId"))
                .thenReturn(restaurantBookingReceipt);

        doThrow(MessagingException.class).when(mockEmailService).sendSimpleMessage("email", "FPT ---THÔNG TIN DỊCH VỤ",
                "text");

        // Configure RestaurantBookingReceiptRepository.save(...).
        final RestaurantBookingReceipt restaurantBookingReceipt1 = new RestaurantBookingReceipt("id", "bookingId",
                new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                        RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                        List.of("value"),
                        List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))), "phone",
                "partnerId", "userId");
        when(mockRestaurantBookingReceiptRepository.save(any(RestaurantBookingReceipt.class)))
                .thenReturn(restaurantBookingReceipt1);

        // Run the test
        receiptRestaurantServiceImplUnderTest.restaurantApproStatusRepository(restaurantApproveBookingRequest);

        // Verify the results
        verify(mockRestaurantBookingReceiptRepository).save(any(RestaurantBookingReceipt.class));
    }

    @Test
    void testRestaurantApproStatusRepositoryUserId() {
        // Setup
        final RestaurantApproveBookingRequest restaurantApproveBookingRequest = new RestaurantApproveBookingRequest(
                "bookingId", BookingStatus.BOOKING_PENDING);

        // Configure RestaurantBookingReceiptRepository.findFirstByBookingId(...).
        final RestaurantBookingReceipt restaurantBookingReceipt = new RestaurantBookingReceipt("id", "bookingId",
                new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                        RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                        List.of("value"),
                        List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))), "phone",
                "partnerId", "userId");
        when(mockRestaurantBookingReceiptRepository.findFirstByBookingId("bookingId"))
                .thenReturn(restaurantBookingReceipt);

        // Configure RestaurantBookingReceiptRepository.save(...).
        final RestaurantBookingReceipt restaurantBookingReceipt1 = new RestaurantBookingReceipt("id", "bookingId",
                new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                BookingStatus.BOOKING_PENDING, 1, "checkinTime", 0, 0,
                new RestaurantInfoDTO("id", "code", "title", "description", "phonePartner", "slug",
                        RestaurantType.NHA_HANG, new Integer[]{0}, "address", "userId", ApproveStatus.PENDING,
                        List.of("value"),
                        List.of(new RestaurantMenuDTO(0L, 0.0, "currency", "image", "description", "name"))), "phone",
                "partnerId", "userId");
        when(mockRestaurantBookingReceiptRepository.save(any(RestaurantBookingReceipt.class)))
                .thenReturn(restaurantBookingReceipt1);

        // Run the test
        receiptRestaurantServiceImplUnderTest.restaurantApproStatusRepositoryUserId(restaurantApproveBookingRequest);

        // Verify the results
        verify(mockRestaurantBookingReceiptRepository).save(any(RestaurantBookingReceipt.class));
    }

    @Test
    void testRestaurantApproStatusRepositoryUserId_RestaurantBookingReceiptRepositoryFindFirstByBookingIdReturnsNull() {
        // Setup
        final RestaurantApproveBookingRequest restaurantApproveBookingRequest = new RestaurantApproveBookingRequest(
                "bookingId", BookingStatus.BOOKING_PENDING);
        when(mockRestaurantBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> receiptRestaurantServiceImplUnderTest.restaurantApproStatusRepositoryUserId(
                restaurantApproveBookingRequest)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testFormatDate() throws Exception {
        assertThat(ReceiptRestaurantServiceImpl.formatDate(10012002)).isEqualTo("1002-00-02 ");
    }

    @Test
    void testMain() throws Exception {
        // Setup
        // Run the test
        ReceiptRestaurantServiceImpl.main(new String[]{"args"});

        // Verify the results
    }
}
