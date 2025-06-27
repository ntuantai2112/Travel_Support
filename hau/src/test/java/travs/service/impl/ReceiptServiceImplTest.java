package travs.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import travs.common.ActivitiesType;
import travs.common.HotelType;
import travs.common.type.ApproveStatus;
import travs.common.type.BookingStatus;
import travs.common.type.RoomType;
import travs.dao.AccountDAO;
import travs.entity.BookingContact;
import travs.entity.account.Account;
import travs.entity.account.Role;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesBookingReceipt;
import travs.entity.activities.ActivitiesGame;
import travs.entity.hotel.Hotel;
import travs.entity.hotel.HotelBookableItem;
import travs.entity.hotel.HotelBookingReceipt;
import travs.exception.GeneralException;
import travs.exception.ResourceNotFoundException;
import travs.model.Contact;
import travs.repository.FavoriteHotelRepository;
import travs.repository.HotelBookItemRepository;
import travs.repository.HotelBookingReceiptRepository;
import travs.repository.HotelImageRepository;
import travs.repository.HotelRepository;
import travs.repository.activities.ActivitiesBookingReceiptRepository;
import travs.repository.activities.ActivitiesGameRepository;
import travs.repository.activities.ActivitiesImageRepository;
import travs.repository.activities.ActivitiesRepository;
import travs.request.activities.ActivitiesApproveBookingRequest;
import travs.request.activities.BookingActivitiesRequest;
import travs.request.hotel.BookingRequest;
import travs.request.hotel.HotelApproveBookingRequest;
import travs.response.BaseResponse;
import travs.response.activities.ActivitiesGameDTO;
import travs.response.activities.ActivitiesInfoDTO;
import travs.response.hotel.HotelInfoDTO;
import travs.response.hotel.PackageDTO;
import travs.service.AccountService;
import travs.service.EmailService;
import travs.utils.AuthenticationUtils;

import javax.mail.MessagingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceImplTest {

    @Mock
    private HotelRepository mockHotelRepository;
    @Mock
    private HotelBookItemRepository mockHotelBookItemRepository;
    @Mock
    private HotelImageRepository mockHotelImageRepository;
    @Mock
    private FavoriteHotelRepository mockFavoriteHotelRepository;
    @Mock
    private HotelBookingReceiptRepository mockHotelBookingReceiptRepository;
    @Mock
    private ActivitiesBookingReceiptRepository mockActivitiesBookingReceiptRepository;
    @Mock
    private ActivitiesRepository mockActivitiesRepository;
    @Mock
    private ActivitiesImageRepository mockActivitiesImageRepository;
    @Mock
    private ActivitiesGameRepository mockActivitiesGameRepository;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private AccountService mockAccountService;
    @Mock
    private AccountDAO mockAccountDAO;

    @InjectMocks
    private ReceiptServiceImpl receiptServiceImplUnderTest;

    @Test
    void testBookingHotel_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final BookingRequest bookingRequest = new BookingRequest("hotelCode", "1", new Contact("firstName", "lastName", "email", "phone"), 20020210, 20020210);

            // Configure HotelRepository.findFirstByCode(...).
            final Hotel hotel = new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0, new Integer[]{0}, 0.0, "address", "partnerId", ApproveStatus.PENDING);
            when(mockHotelRepository.findFirstByCode(any())).thenReturn(hotel);

            // Configure AccountDAO.getAccountById(...).
            final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false, new Role(0L, "name"), "resetPasswordToken");
            when(mockAccountDAO.getAccountById(any())).thenReturn(account);

            when(mockHotelImageRepository.findAllHotelByCode(any())).thenReturn(List.of("value"));

            // Configure HotelBookItemRepository.findFirstById(...).
            final HotelBookableItem hotelBookableItem = new HotelBookableItem(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", false, RoomType.SINGLE, "image", new Integer[]{0});
            when(mockHotelBookItemRepository.findFirstById(any())).thenReturn(hotelBookableItem);

            // Configure HotelBookingReceiptRepository.save(...).
            final HotelBookingReceipt hotelBookingReceipt = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
            when(mockHotelBookingReceiptRepository.save(any(HotelBookingReceipt.class))).thenReturn(hotelBookingReceipt);

            // Run the test
            final BaseResponse result = receiptServiceImplUnderTest.bookingHotel(bookingRequest);
            assertThat(result).isNotNull();
        }
    }

    @Test
    void testBookingHotel_Fail_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final BookingRequest bookingRequest = new BookingRequest("hotelCode", "1", new Contact("firstName", "lastName", "email", "phone"), 0, 0);

            // Configure HotelRepository.findFirstByCode(...).
            final Hotel hotel = new Hotel("id", "code", "title", "description", "slug", HotelType.POPULAR, 0.0, 0, new Integer[]{0}, 0.0, "address", "partnerId", ApproveStatus.PENDING);
            when(mockHotelRepository.findFirstByCode(any())).thenReturn(hotel);

            // Configure AccountDAO.getAccountById(...).
            final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false, new Role(0L, "name"), "resetPasswordToken");
            when(mockAccountDAO.getAccountById(any())).thenReturn(account);

            when(mockHotelImageRepository.findAllHotelByCode(any())).thenReturn(List.of("value"));

            // Configure HotelBookItemRepository.findFirstById(...).
            final HotelBookableItem hotelBookableItem = new HotelBookableItem(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", false, RoomType.SINGLE, "image", new Integer[]{0});
            when(mockHotelBookItemRepository.findFirstById(any())).thenReturn(hotelBookableItem);

            // Run the test
            Assertions.assertThrows(GeneralException.class, () -> receiptServiceImplUnderTest.bookingHotel(bookingRequest));
        }
    }

    @Test
    void testBookingHotel_UserIdNull_Fail_Test() {
        final BookingRequest bookingRequest = new BookingRequest("hotelCode", "1", new Contact("firstName", "lastName", "email", "phone"), 0, 0);
        Assertions.assertThrows(GeneralException.class, () -> receiptServiceImplUnderTest.bookingHotel(bookingRequest));
    }

    @Test
    void testBookingActivities_UserIdNull_Fail_Test() {
        final BookingActivitiesRequest bookingRequest = new BookingActivitiesRequest("activitiesCode", new Contact("firstName", "lastName", "email", "phone"), 0, 0, 0);
        Assertions.assertThrows(GeneralException.class, () -> receiptServiceImplUnderTest.bookingActivities(bookingRequest));
    }

    @Test
    void testBookingActivities_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final BookingActivitiesRequest bookingRequest = new BookingActivitiesRequest("activitiesCode", new Contact("firstName", "lastName", "email", "phone"), 0, 0, 0);

            // Configure ActivitiesRepository.findFirstByCode(...).
            final Activities activities = new Activities("id", "activitiesCode", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, 0.0, "address", "partnerId", ApproveStatus.PENDING);
            when(mockActivitiesRepository.findFirstByCode(any())).thenReturn(activities);

            // Configure AccountDAO.getAccountById(...).
            final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false, new Role(0L, "name"), "resetPasswordToken");
            when(mockAccountDAO.getAccountById(any())).thenReturn(account);

            when(mockActivitiesImageRepository.findAllActivitiesCode(any())).thenReturn(List.of("value"));

            // Configure ActivitiesGameRepository.findAllByActivitiesCode(...).
            final List<ActivitiesGame> activitiesGames = List.of(new ActivitiesGame(0, "activitiesCode", "name", "image", "description"));
            when(mockActivitiesGameRepository.findAllByActivitiesCode(any())).thenReturn(activitiesGames);

            // Configure ActivitiesBookingReceiptRepository.save(...).
            final ActivitiesBookingReceipt activitiesBookingReceipt = new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
            when(mockActivitiesBookingReceiptRepository.save(any(ActivitiesBookingReceipt.class))).thenReturn(activitiesBookingReceipt);

            // Run the test
            final BaseResponse result = receiptServiceImplUnderTest.bookingActivities(bookingRequest);
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testBookingActivities_Fail_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final BookingActivitiesRequest bookingRequest = new BookingActivitiesRequest("activitiesCode", new Contact("firstName", "lastName", "email", "phone"), 0, 0, 0);

            // Configure ActivitiesRepository.findFirstByCode(...).
            final Activities activities = new Activities("id", "activitiesCode", "title", "description", "slug", ActivitiesType.IN_DOOR, 0, 0, 0.0, null, "address", "partnerId", ApproveStatus.PENDING);
            when(mockActivitiesRepository.findFirstByCode(any())).thenReturn(activities);

            // Configure AccountDAO.getAccountById(...).
            final Account account = new Account("id", "name", "email", "phone", false, "password", "image", "dob", false, new Role(0L, "name"), "resetPasswordToken");
            when(mockAccountDAO.getAccountById(any())).thenReturn(account);

            when(mockActivitiesImageRepository.findAllActivitiesCode(any())).thenReturn(List.of("value"));

            // Configure ActivitiesGameRepository.findAllByActivitiesCode(...).
            final List<ActivitiesGame> activitiesGames = List.of(new ActivitiesGame(0, "activitiesCode", "name", "image", "description"));
            when(mockActivitiesGameRepository.findAllByActivitiesCode(any())).thenReturn(activitiesGames);

            // Run the test
            Assertions.assertThrows(GeneralException.class, () -> receiptServiceImplUnderTest.bookingActivities(bookingRequest));
        }
    }

    @Test
    void testCalculateNumberNight_Success_Test() {
        assertThat(receiptServiceImplUnderTest.calculateNumberNight(20220212, 20220213)).isEqualTo(1);
    }

    @Test
    void testDateDiff_Success_Test() {
        assertThat(receiptServiceImplUnderTest.dateDiff(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())).isEqualTo(0);
    }

    @Test
    void testGetListHotelReceipt_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final List<HotelBookingReceipt> hotelBookingReceipts = List.of(new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId"));
            when(mockHotelBookingReceiptRepository.findAllByPartnerIdAndStatus(any(), any(), any())).thenReturn(hotelBookingReceipts);
            when(mockHotelBookingReceiptRepository.countAllByPartnerIdAndStatus(any(), any())).thenReturn(0);
            // Run the test
            final BaseResponse result = receiptServiceImplUnderTest.getListHotelReceipt(BookingStatus.BOOKING_PENDING, 2, 55);
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testGetListHotelReceipt_StatusNull_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            // Run the test
            final BaseResponse result = receiptServiceImplUnderTest.getListHotelReceipt(null, 2, 55);
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testGetListHotelReceipt_Fail_Test() {
            Assertions.assertThrows(GeneralException.class, () -> receiptServiceImplUnderTest.getListHotelReceipt(BookingStatus.BOOKING_PENDING, 2, 55));
    }

    @Test
    void testGetReceiptDetail_Success_Test() {
        // Setup
        // Configure HotelBookingReceiptRepository.findFirstById(...).
        final HotelBookingReceipt hotelBookingReceipt = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
        when(mockHotelBookingReceiptRepository.findFirstById(any())).thenReturn(hotelBookingReceipt);

        // Run the test
        final BaseResponse result = receiptServiceImplUnderTest.getReceiptDetail("id");
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void testGetReceiptDetailActivities_Success_Test() {
        // Setup
        // Configure ActivitiesBookingReceiptRepository.findFirstById(...).
        final ActivitiesBookingReceipt activitiesBookingReceipt = new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
        when(mockActivitiesBookingReceiptRepository.findFirstById(any())).thenReturn(activitiesBookingReceipt);

        // Run the test
        final BaseResponse result = receiptServiceImplUnderTest.getReceiptDetailActivities("id");
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void testGetListActivitiesReceipt_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final List<ActivitiesBookingReceipt> activitiesBookingReceipts = List.of(new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId"));
            when(mockActivitiesBookingReceiptRepository.findAllByPartnerIdAndStatusOrderByCreatedAtDesc(any(), any(), any())).thenReturn(activitiesBookingReceipts);
            when(mockActivitiesBookingReceiptRepository.countAllByPartnerIdAndStatus(any(), any())).thenReturn(0);
            // Run the test
            final BaseResponse result = receiptServiceImplUnderTest.getListActivitiesReceipt(BookingStatus.BOOKING_PENDING, 2, 55);
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testGetListActivitiesReceipt_StatusNull_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final List<ActivitiesBookingReceipt> activitiesBookingReceipts = List.of(new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId"));
            when(mockActivitiesBookingReceiptRepository.findAllByPartnerIdOrderByCreatedAtDesc(any(), any())).thenReturn(activitiesBookingReceipts);
            when(mockActivitiesBookingReceiptRepository.countAllByPartnerId(any())).thenReturn(0);
            // Run the test
            final BaseResponse result = receiptServiceImplUnderTest.getListActivitiesReceipt(null, 2, 55);
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testGetListActivitiesReceipt_Fail_Test() {
        // Run the test
        Assertions.assertThrows(GeneralException.class, () -> receiptServiceImplUnderTest.getListActivitiesReceipt(null, 2, 55));
    }

    @Test
    void testGetListActivitiesReceiptByUserId_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final List<ActivitiesBookingReceipt> activitiesBookingReceipts = List.of(new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId"));
            when(mockActivitiesBookingReceiptRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(any(), any(), any())).thenReturn(activitiesBookingReceipts);
            when(mockActivitiesBookingReceiptRepository.countAllByUserIdAndStatus(any(), any())).thenReturn(0);
            // Run the test
            final BaseResponse result = receiptServiceImplUnderTest.getListActivitiesReceiptByUserId(BookingStatus.BOOKING_PENDING, 2, 55);
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testGetListActivitiesReceiptByUserId_StatusNull_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final List<ActivitiesBookingReceipt> activitiesBookingReceipts = List.of(new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId"));
            when(mockActivitiesBookingReceiptRepository.findAllByUserIdOrderByCreatedAtDesc(any(), any())).thenReturn(activitiesBookingReceipts);
            when(mockActivitiesBookingReceiptRepository.countAllByUserId(any())).thenReturn(0);
            // Run the test
            final BaseResponse result = receiptServiceImplUnderTest.getListActivitiesReceiptByUserId(null, 2, 55);
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testGetListActivitiesReceiptByUserId_Fail_Test() {
        // Run the test
        Assertions.assertThrows(GeneralException.class, () -> receiptServiceImplUnderTest.getListActivitiesReceiptByUserId(null, 2, 55));
    }

    @Test
    void testGetListHotelReceiptByUserId_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final List<HotelBookingReceipt> hotelBookingReceipts = List.of(new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId"));
            when(mockHotelBookingReceiptRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(any(), any(), any())).thenReturn(hotelBookingReceipts);
            when(mockHotelBookingReceiptRepository.countAllByUserIdAndStatus(any(), any())).thenReturn(0);

            // Run the test
            final BaseResponse result = receiptServiceImplUnderTest.getListHotelReceiptByUserId(BookingStatus.BOOKING_PENDING, 2, 55);
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testGetListHotelReceiptByUserId_StatusNull_Success_Test() {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final List<HotelBookingReceipt> hotelBookingReceipts = List.of(new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId"));
            when(mockHotelBookingReceiptRepository.findAllByUserIdOrderByCreatedAtDesc(any(), any())).thenReturn(hotelBookingReceipts);
            when(mockHotelBookingReceiptRepository.countAllByUserId("userId")).thenReturn(0);

            // Run the test
            final BaseResponse result = receiptServiceImplUnderTest.getListHotelReceiptByUserId(null, 2, 55);
            assertThat(result).isNotNull();
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Test
    void testGetListHotelReceiptByUserId_Fail_Test() {
        // Run the test
        Assertions.assertThrows(GeneralException.class, () -> receiptServiceImplUnderTest.getListHotelReceiptByUserId(BookingStatus.BOOKING_PENDING, 2, 55));
    }

    @Test
    void testApproveActivitiesBooking_Success_Test() throws Exception {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final ActivitiesApproveBookingRequest request = new ActivitiesApproveBookingRequest("bookingId", BookingStatus.BOOKING_PENDING);

            // Configure ActivitiesBookingReceiptRepository.findFirstByBookingId(...).
            final ActivitiesBookingReceipt activitiesBookingReceipt = new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
            when(mockActivitiesBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(activitiesBookingReceipt);

            // Configure ActivitiesBookingReceiptRepository.save(...).
            final ActivitiesBookingReceipt activitiesBookingReceipt1 = new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
            when(mockActivitiesBookingReceiptRepository.save(any(ActivitiesBookingReceipt.class))).thenReturn(activitiesBookingReceipt1);

            // Run the test
            receiptServiceImplUnderTest.approveActivitiesBooking(request);

            // Verify the results
            verify(mockEmailService).sendSimpleMessage("email", "FPT ---THÔNG TIN DỊCH VỤ", "Xin Chào Bạn <p> Cảm ơn bản đã sử dụng dịch vụ của chúng tôi  </p><p> Yêu cầu của bạn đã bị hủy  , do một số lý do nên yêu cầu của bạn k được chấp nhận  </p><p  Có điều gì thắc mắc xin liên hệ với SĐT của quản lý khu vui chơi : \"phonePartner\"     </p>");
            verify(mockActivitiesBookingReceiptRepository).save(any(ActivitiesBookingReceipt.class));
        }
    }

    @Test
    void testApproveActivitiesBooking_BookingStatus_Success_Test() throws Exception {
        try (MockedStatic<AuthenticationUtils> mockedStatic = mockStatic(AuthenticationUtils.class)) {
            mockedStatic.when(AuthenticationUtils::getUserId).thenReturn("userId");
            final ActivitiesApproveBookingRequest request = new ActivitiesApproveBookingRequest("bookingId", BookingStatus.BOOKING_APPROVED);

            // Configure ActivitiesBookingReceiptRepository.findFirstByBookingId(...).
            final ActivitiesBookingReceipt activitiesBookingReceipt = new ActivitiesBookingReceipt("id", "bookingId",
                    new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"),
                    BookingStatus.BOOKING_PENDING, 20221012, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(
                    new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
            when(mockActivitiesBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(activitiesBookingReceipt);

            // Configure ActivitiesBookingReceiptRepository.save(...).
            final ActivitiesBookingReceipt activitiesBookingReceipt1 = new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 20221012, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
            when(mockActivitiesBookingReceiptRepository.save(any(ActivitiesBookingReceipt.class))).thenReturn(activitiesBookingReceipt1);

            // Run the test
            receiptServiceImplUnderTest.approveActivitiesBooking(request);

            // Verify the results
            verify(mockActivitiesBookingReceiptRepository).save(any(ActivitiesBookingReceipt.class));
        }
    }

    @Test
    void testApproveActivitiesBooking_ActivitiesBookingReceiptRepositoryFindFirstByBookingIdReturnsNull() {
        // Setup
        final ActivitiesApproveBookingRequest request = new ActivitiesApproveBookingRequest("bookingId", BookingStatus.BOOKING_PENDING);
        when(mockActivitiesBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> receiptServiceImplUnderTest.approveActivitiesBooking(request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testApproveActivitiesBooking_EmailServiceThrowsMessagingException() throws Exception {
        // Setup
        final ActivitiesApproveBookingRequest request = new ActivitiesApproveBookingRequest("bookingId", BookingStatus.BOOKING_PENDING);

        // Configure ActivitiesBookingReceiptRepository.findFirstByBookingId(...).
        final ActivitiesBookingReceipt activitiesBookingReceipt = new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
        when(mockActivitiesBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(activitiesBookingReceipt);

        doThrow(MessagingException.class).when(mockEmailService).sendSimpleMessage("email", "FPT ---THÔNG TIN DỊCH VỤ", "text");

        // Configure ActivitiesBookingReceiptRepository.save(...).
        final ActivitiesBookingReceipt activitiesBookingReceipt1 = new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
        when(mockActivitiesBookingReceiptRepository.save(any(ActivitiesBookingReceipt.class))).thenReturn(activitiesBookingReceipt1);

        // Run the test
        receiptServiceImplUnderTest.approveActivitiesBooking(request);

        // Verify the results
        verify(mockActivitiesBookingReceiptRepository).save(any(ActivitiesBookingReceipt.class));
    }

    @Test
    void testApproveActivitiesBookingUserId_Success_Test() {
        // Setup
        final ActivitiesApproveBookingRequest request = new ActivitiesApproveBookingRequest("bookingId", BookingStatus.BOOKING_PENDING);

        // Configure ActivitiesBookingReceiptRepository.findFirstByBookingId(...).
        final ActivitiesBookingReceipt activitiesBookingReceipt = new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
        when(mockActivitiesBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(activitiesBookingReceipt);

        // Configure ActivitiesBookingReceiptRepository.save(...).
        final ActivitiesBookingReceipt activitiesBookingReceipt1 = new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
        when(mockActivitiesBookingReceiptRepository.save(any(ActivitiesBookingReceipt.class))).thenReturn(activitiesBookingReceipt1);

        // Run the test
        receiptServiceImplUnderTest.approveActivitiesBookingUserId(request);

        // Verify the results
        verify(mockActivitiesBookingReceiptRepository).save(any(ActivitiesBookingReceipt.class));
    }

    @Test
    void testApproveActivitiesBookingUserId_ActivitiesBookingReceiptRepositoryFindFirstByBookingIdReturnsNull() {
        // Setup
        final ActivitiesApproveBookingRequest request = new ActivitiesApproveBookingRequest("bookingId", BookingStatus.BOOKING_PENDING);
        when(mockActivitiesBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> receiptServiceImplUnderTest.approveActivitiesBookingUserId(request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testApproveHotelBookingUserId_Success_Test() {
        // Setup
        final HotelApproveBookingRequest request = new HotelApproveBookingRequest("bookingId", BookingStatus.BOOKING_PENDING);

        // Configure HotelBookingReceiptRepository.findFirstByBookingId(...).
        final HotelBookingReceipt hotelBookingReceipt = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
        when(mockHotelBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(hotelBookingReceipt);

        // Configure HotelBookingReceiptRepository.save(...).
        final HotelBookingReceipt hotelBookingReceipt1 = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
        when(mockHotelBookingReceiptRepository.save(any(HotelBookingReceipt.class))).thenReturn(hotelBookingReceipt1);

        // Run the test
        receiptServiceImplUnderTest.approveHotelBookingUserId(request);

        // Verify the results
        verify(mockHotelBookingReceiptRepository).save(any(HotelBookingReceipt.class));
    }

    @Test
    void testApproveHotelBookingUserId_HotelBookingReceiptRepositoryFindFirstByBookingIdReturnsNull() {
        // Setup
        final HotelApproveBookingRequest request = new HotelApproveBookingRequest("bookingId", BookingStatus.BOOKING_PENDING);
        when(mockHotelBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> receiptServiceImplUnderTest.approveHotelBookingUserId(request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testApproveHotelBooking_Success_Test() throws Exception {
        // Setup
        final HotelApproveBookingRequest request = new HotelApproveBookingRequest("bookingId", BookingStatus.BOOKING_PENDING);

        // Configure HotelBookingReceiptRepository.findFirstByBookingId(...).
        final HotelBookingReceipt hotelBookingReceipt = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
        when(mockHotelBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(hotelBookingReceipt);

        // Configure HotelBookingReceiptRepository.save(...).
        final HotelBookingReceipt hotelBookingReceipt1 = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
        when(mockHotelBookingReceiptRepository.save(any(HotelBookingReceipt.class))).thenReturn(hotelBookingReceipt1);

        // Run the test
        receiptServiceImplUnderTest.approveHotelBooking(request);

        // Verify the results
        verify(mockEmailService).sendSimpleMessage("email", "FPT ---THÔNG TIN DỊCH VỤ", "Xin Chào Bạn <p> Cảm ơn bản đã sử dụng dịch vụ của chúng tôi  </p><p> Yêu cầu của bạn đã bị hủy  , do một số lý do nên yêu cầu của bạn k được chấp nhận  </p><p  Có điều gì thắc mắc xin liên hệ với SĐT của hotel : \"phonePartner\"     </p>");
        verify(mockHotelBookingReceiptRepository).save(any(HotelBookingReceipt.class));
    }


    @Test
    void testApproveHotelBooking_BOOKING_APPROVED_Success_Test() throws Exception {
        // Setup
        final HotelApproveBookingRequest request = new HotelApproveBookingRequest("bookingId", BookingStatus.BOOKING_APPROVED);

        // Configure HotelBookingReceiptRepository.findFirstByBookingId(...).
        final HotelBookingReceipt hotelBookingReceipt = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 20221011, 20221012, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
        when(mockHotelBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(hotelBookingReceipt);

        // Configure HotelBookingReceiptRepository.save(...).
        final HotelBookingReceipt hotelBookingReceipt1 = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 20221011, 20221013, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
        when(mockHotelBookingReceiptRepository.save(any(HotelBookingReceipt.class))).thenReturn(hotelBookingReceipt1);

        // Run the test
        receiptServiceImplUnderTest.approveHotelBooking(request);

        // Verify the results
        verify(mockEmailService).sendSimpleMessage("email", "FPT ---THÔNG TIN DỊCH VỤ", "Xin Chào Bạn <p> Cảm ơn bản đã sử dụng dịch vụ của chúng tôi  </p><p> Yêu cầu của bạn đã đc chúng tối chấp nhận   </p><p> Làm ơn kiểm tra , thông tin đặt dịch vụ của bạn ở bên dưới </p><p> Từ Ngày    :  \"2022-10-11\"   </p><p> Đến Ngày  :  \"2022-10-12\"   </p><p> Tên Hotel  :  \"title\"   </p><p> Địa Chỉ Hotel   :  \"address\"   </p>");
        verify(mockHotelBookingReceiptRepository).save(any(HotelBookingReceipt.class));
    }

    @Test
    void testApproveHotelBooking_HotelBookingReceiptRepositoryFindFirstByBookingIdReturnsNull() {
        // Setup
        final HotelApproveBookingRequest request = new HotelApproveBookingRequest("bookingId", BookingStatus.BOOKING_PENDING);
        when(mockHotelBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> receiptServiceImplUnderTest.approveHotelBooking(request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testApproveHotelBooking_EmailServiceThrowsMessagingException() throws Exception {
        // Setup
        final HotelApproveBookingRequest request = new HotelApproveBookingRequest("bookingId", BookingStatus.BOOKING_PENDING);

        // Configure HotelBookingReceiptRepository.findFirstByBookingId(...).
        final HotelBookingReceipt hotelBookingReceipt = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
        when(mockHotelBookingReceiptRepository.findFirstByBookingId("bookingId")).thenReturn(hotelBookingReceipt);

        doThrow(MessagingException.class).when(mockEmailService).sendSimpleMessage("email", "FPT ---THÔNG TIN DỊCH VỤ", "text");

        // Configure HotelBookingReceiptRepository.save(...).
        final HotelBookingReceipt hotelBookingReceipt1 = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
        when(mockHotelBookingReceiptRepository.save(any(HotelBookingReceipt.class))).thenReturn(hotelBookingReceipt1);

        // Run the test
        receiptServiceImplUnderTest.approveHotelBooking(request);

        // Verify the results
        verify(mockHotelBookingReceiptRepository).save(any(HotelBookingReceipt.class));
    }

    @Test
    void testFormatDate_Success_Test() throws Exception {
        assertThat(ReceiptServiceImpl.formatDate(20221110)).isEqualTo("2022-11-10");
        assertThatThrownBy(() -> ReceiptServiceImpl.formatDate(1)).isInstanceOf(ParseException.class);
    }

    @Test
    void testSendMailApprovedActivities_Success_Test() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = ReceiptServiceImpl.class.getDeclaredMethod("sendMailApprovedActivities", ActivitiesApproveBookingRequest.class, ActivitiesBookingReceipt.class);
        method.setAccessible(true);
        final ActivitiesBookingReceipt activitiesBookingReceipt = new ActivitiesBookingReceipt("id", "bookingId", new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0, "phone", 0.0, new ActivitiesInfoDTO("id", "code", "title", "description", "slug", ActivitiesType.IN_DOOR, 0.0, 0.0, 0.0, "phonePartner", "address", "userId", ApproveStatus.PENDING, List.of("value"), List.of(new ActivitiesGameDTO("id", "name", "image", "description"))), "partnerId", "userId");
        method.invoke(receiptServiceImplUnderTest, null, activitiesBookingReceipt);
        verifyNoInteractions(mockEmailService);
    }

    @Test
    void testSendMailApprovedHotel_Success_Test() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = ReceiptServiceImpl.class.getDeclaredMethod("sendMailApprovedHotel", HotelApproveBookingRequest.class, HotelBookingReceipt.class);
        method.setAccessible(true);
        final HotelBookingReceipt hotelBookingReceipt = new HotelBookingReceipt("id", "bookingId", 0, new BookingContact("id", "firstName", "lastName", "email", "phone", "customerId"), BookingStatus.BOOKING_PENDING, 0, 0, 0.0, new HotelInfoDTO("id", "code", "title", "description", "slug", HotelType.POPULAR, "phonePartner", 0.0, 0, new Integer[]{0}, 0.0, "address", "userId", ApproveStatus.PENDING, List.of("value")), "phone", new PackageDTO(0L, "hotelCode", "hotelOptionCode", 0.0, "currency", 0L, 0L, false, RoomType.SINGLE, "image"), "partnerId", "userId");
        method.invoke(receiptServiceImplUnderTest, null, hotelBookingReceipt);
        verifyNoInteractions(mockEmailService);
    }
}
