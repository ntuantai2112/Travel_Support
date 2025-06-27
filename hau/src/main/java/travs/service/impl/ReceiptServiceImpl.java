package travs.service.impl;

import travs.common.type.BookingStatus;
import travs.dao.AccountDAO;
import travs.entity.BookingContact;
import travs.entity.account.Account;
import travs.exception.ErrorCode;
import travs.exception.GeneralException;
import travs.exception.ResourceNotFoundException;
import travs.repository.FavoriteHotelRepository;
import travs.repository.HotelBookItemRepository;
import travs.repository.HotelBookingReceiptRepository;
import travs.repository.HotelImageRepository;
import travs.repository.HotelRepository;
import travs.repository.activities.ActivitiesBookingReceiptRepository;
import travs.repository.activities.ActivitiesGameRepository;
import travs.repository.activities.ActivitiesImageRepository;
import travs.repository.activities.ActivitiesRepository;
import travs.response.BaseResponse;
import travs.response.activities.ActivitiesBookingReceiptDTO;
import travs.response.activities.ActivitiesGameDTO;
import travs.response.activities.ActivitiesInfoDTO;
import travs.response.hotel.HotelBookingReceiptDTO;
import travs.response.hotel.HotelInfoDTO;
import travs.response.hotel.PackageDTO;
import travs.service.AccountService;
import travs.service.EmailService;
import travs.utils.AuthenticationUtils;
import travs.utils.HelperUtils;
import travs.utils.MappingUtils;
import travs.entity.activities.Activities;
import travs.entity.activities.ActivitiesBookingReceipt;
import travs.entity.activities.ActivitiesGame;
import travs.entity.hotel.Hotel;
import travs.entity.hotel.HotelBookableItem;
import travs.entity.hotel.HotelBookingReceipt;
import travs.request.activities.ActivitiesApproveBookingRequest;
import travs.request.activities.BookingActivitiesRequest;
import travs.request.hotel.BookingRequest;
import travs.request.hotel.HotelApproveBookingRequest;
import travs.service.ReceiptService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
@AllArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {


    private HotelRepository hotelRepository;
    private HotelBookItemRepository hotelBookItemRepository;
    private HotelImageRepository hotelImageRepository;
    private FavoriteHotelRepository favoriteHotelRepository;
    private HotelBookingReceiptRepository hotelBookingReceiptRepository;
    private ActivitiesBookingReceiptRepository activitiesBookingReceiptRepository;
    private ActivitiesRepository activitiesRepository;
    private ActivitiesImageRepository activitiesImageRepository;
    private ActivitiesGameRepository activitiesGameRepository;
    private EmailService emailService;

    private AccountService accountService;

    private AccountDAO accountDAO;


    @Override
    public BaseResponse bookingHotel(BookingRequest bookingRequest) {
        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }
        try {

            String bookingId = HelperUtils.genBookingID();
            BookingContact contact = BookingContact.builder()
                    .customerId(userId)
                    .email(bookingRequest.getContact().getEmail())
                    .firstName(bookingRequest.getContact().getFirstName())
                    .lastName(bookingRequest.getContact().getLastName())
                    .phone(bookingRequest.getContact().getPhone())
                    .build();

            Hotel hotel = hotelRepository.findFirstByCode(bookingRequest.getHotelCode());

            Account account  = accountDAO.getAccountById(hotel.getUserId());

            List<String> hotelImages = hotelImageRepository.findAllHotelByCode(hotel.getCode());
            HotelInfoDTO hotelInfoDTO = MappingUtils.map(hotel, HotelInfoDTO.class);
            hotelInfoDTO.setImagesList(hotelImages);

            String packageId = bookingRequest.getPackageId();

            HotelBookableItem hotelPackage = hotelBookItemRepository.findFirstById(Long.parseLong(packageId));
            HotelBookingReceipt receipt = HotelBookingReceipt.builder()
                    .hotelInfoDTO(hotelInfoDTO)
                    .packageInfo(MappingUtils.map(hotelPackage, PackageDTO.class))
                    .contact(contact)
                    .userId(userId)
                    .phonePartner(account.getPhone())
                    .checkin(bookingRequest.getCheckinDate())
                    .checkout(bookingRequest.getCheckoutDate())
                    .bookingId(bookingId)
                    .totalNights(calculateNumberNight(bookingRequest.getCheckinDate(), bookingRequest.getCheckoutDate()))
                    .status(BookingStatus.BOOKING_PENDING)
                    .partnerId(hotel.getUserId())
                    .build();
            receipt.setPrice(receipt.getTotalNights() * hotelPackage.getPrice());
            receipt = hotelBookingReceiptRepository.save(receipt);
            return BaseResponse.ok(MappingUtils.map(receipt, HotelBookingReceiptDTO.class));

        } catch (Exception ex) {
            log.error("IOException:" + ex.getMessage(), ex);
            throw new GeneralException();
        }
    }

    @Override
    public BaseResponse bookingActivities(BookingActivitiesRequest bookingRequest) {
        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }
        try {

            String bookingId = "A-" + HelperUtils.genBookingID();
            BookingContact contact = BookingContact.builder()
                    .customerId(userId)
                    .email(bookingRequest.getContact().getEmail())
                    .firstName(bookingRequest.getContact().getFirstName())
                    .lastName(bookingRequest.getContact().getLastName())
                    .phone(bookingRequest.getContact().getPhone())
                    .build();



            Activities activities = activitiesRepository.findFirstByCode(bookingRequest.getActivitiesCode());

            Account account  = accountDAO.getAccountById(activities.getUserId());

            List<String> activitiesImageList = activitiesImageRepository.findAllActivitiesCode(activities.getCode());

            List<ActivitiesGame> activitiesGameList = activitiesGameRepository.findAllByActivitiesCode(activities.getCode());
            ActivitiesInfoDTO activitiesInfoDTO = MappingUtils.map(activities, ActivitiesInfoDTO.class);
            activitiesInfoDTO.setImagesList(activitiesImageList);
            activitiesInfoDTO.setGameDTOList(MappingUtils.map(activitiesGameList, ActivitiesGameDTO.class));

            ActivitiesBookingReceipt receipt = ActivitiesBookingReceipt.builder()
                    .activitiesInfoDTO(activitiesInfoDTO)
                    .contact(contact)
                    .userId(userId)
                    .phonePartner(account.getPhone())
                    .travelDate(bookingRequest.getTravelDate())
                    .numberTicketChild(bookingRequest.getNumberTicketChild())
                    .numberTicketAdult(bookingRequest.getNumberTicketAdult())
                    .bookingId(bookingId)
                    .status(BookingStatus.BOOKING_PENDING)
                    .partnerId(activities.getUserId())
                    .price(activities.getAdultTicketPrice() * bookingRequest.getNumberTicketAdult() + activities.getChildTicketPrice() * bookingRequest.getNumberTicketChild())
                    .build();
            receipt = activitiesBookingReceiptRepository.save(receipt);
            return BaseResponse.ok(MappingUtils.map(receipt, ActivitiesBookingReceiptDTO.class));

        } catch (Exception ex) {
            log.error("IOException:" + ex.getMessage(), ex);
            throw new GeneralException();
        }
    }


    public Integer calculateNumberNight(Integer from, Integer to) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date fromDate = sdf.parse(from.toString());
            Date toDate = sdf.parse(to.toString());
            return dateDiff(fromDate, toDate);
        } catch (ParseException e) {
            log.error("ERR when calcuate number nights ", e);
            return null;
        }
    }

    public int dateDiff(Date from, Date to) {
        long diffInMilisecs = Math.abs(to.getTime() - from.getTime());
        return (int) TimeUnit.DAYS.convert(diffInMilisecs, TimeUnit.MILLISECONDS);
    }




    public BaseResponse getListHotelReceipt(BookingStatus status, Integer page, Integer perPage) {
        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }
        List<HotelBookingReceipt> receiptList;

        Map<String, Integer> mapReturn = new HashMap<>();
        if (status == null) {
            receiptList = hotelBookingReceiptRepository.findAllByPartnerId(userId, PageRequest.of(page, perPage));
            mapReturn.put("total", hotelBookingReceiptRepository.countAllByPartnerId(userId));
        } else {
            receiptList = hotelBookingReceiptRepository.findAllByPartnerIdAndStatus(userId, status, PageRequest.of(page, perPage));
            mapReturn.put("total", hotelBookingReceiptRepository.countAllByPartnerIdAndStatus(userId, status));
        }

        return BaseResponse.ok(MappingUtils.map(receiptList, HotelBookingReceiptDTO.class), mapReturn);
    }

    public BaseResponse getReceiptDetail(String id) {
        HotelBookingReceipt receipt = hotelBookingReceiptRepository.findFirstById(id);

        return BaseResponse.ok(MappingUtils.map(receipt, HotelBookingReceiptDTO.class));
    }

    public BaseResponse getReceiptDetailActivities(String id) {
        ActivitiesBookingReceipt receipt = activitiesBookingReceiptRepository.findFirstById(id);

        return BaseResponse.ok(MappingUtils.map(receipt, ActivitiesBookingReceiptDTO.class));
    }



    public BaseResponse getListActivitiesReceipt(BookingStatus status, Integer page, Integer perPage) {
        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }
        List<ActivitiesBookingReceipt> receiptList;
        Map<String, Integer> mapReturn = new HashMap<>();
        if (status == null) {
            receiptList = activitiesBookingReceiptRepository.findAllByPartnerIdOrderByCreatedAtDesc(userId, PageRequest.of(page, perPage));
            mapReturn.put("total", activitiesBookingReceiptRepository.countAllByPartnerId(userId));
        } else {
            receiptList = activitiesBookingReceiptRepository.findAllByPartnerIdAndStatusOrderByCreatedAtDesc(userId, status, PageRequest.of(page, perPage));
            mapReturn.put("total", activitiesBookingReceiptRepository.countAllByPartnerIdAndStatus(userId, status));
        }
        return BaseResponse.ok(MappingUtils.map(receiptList, ActivitiesBookingReceiptDTO.class), mapReturn);
    }

    @Override
    public BaseResponse getListActivitiesReceiptByUserId(BookingStatus status, Integer page, Integer perPage) {

        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }
        List<ActivitiesBookingReceipt> receiptList;
        Map<String, Integer> mapReturn = new HashMap<>();
        if (status == null) {
            receiptList = activitiesBookingReceiptRepository.findAllByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, perPage));
            mapReturn.put("total", activitiesBookingReceiptRepository.countAllByUserId((userId)));
        } else {
            receiptList = activitiesBookingReceiptRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(userId, status, PageRequest.of(page, perPage));
            mapReturn.put("total", activitiesBookingReceiptRepository.countAllByUserIdAndStatus(userId, status));
        }
        return BaseResponse.ok(MappingUtils.map(receiptList, ActivitiesBookingReceiptDTO.class), mapReturn);
    }

    @Override
    public BaseResponse getListHotelReceiptByUserId(BookingStatus status, Integer page, Integer perPage) {

        String userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED);
        }
        List<HotelBookingReceipt> receiptList;

        Map<String, Integer> mapReturn = new HashMap<>();
        if (status == null) {
            receiptList = hotelBookingReceiptRepository.findAllByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, perPage));
            mapReturn.put("total", hotelBookingReceiptRepository.countAllByUserId(userId));
        } else {
            receiptList = hotelBookingReceiptRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(userId, status, PageRequest.of(page, perPage));
            mapReturn.put("total", hotelBookingReceiptRepository.countAllByUserIdAndStatus(userId, status));
        }

        return BaseResponse.ok(MappingUtils.map(receiptList, HotelBookingReceiptDTO.class), mapReturn);
    }

    @Override
    public void approveActivitiesBooking(ActivitiesApproveBookingRequest request) {
        ActivitiesBookingReceipt receipt = activitiesBookingReceiptRepository.findFirstByBookingId(request.getBookingId());
        if (receipt == null) {
            throw new ResourceNotFoundException();
        }
        if(request.getStatus().equals(BookingStatus.BOOKING_APPROVED)){
            receipt.setStatus(request.getStatus());
            sendMailApprovedActivities(request , receipt);
        }else{
            receipt.setStatus(request.getStatus());
            sendMailRejectActivities(request , receipt);
        }

        receipt.setStatus(request.getStatus());
        activitiesBookingReceiptRepository.save(receipt);
    }

    @Override
    public void approveActivitiesBookingUserId(ActivitiesApproveBookingRequest request) {
        ActivitiesBookingReceipt receipt = activitiesBookingReceiptRepository.findFirstByBookingId(request.getBookingId());
        if (receipt == null) {
            throw new ResourceNotFoundException();
        }

        receipt.setStatus(request.getStatus());
        activitiesBookingReceiptRepository.save(receipt);
    }



    private void sendMailApprovedActivities(ActivitiesApproveBookingRequest request  , ActivitiesBookingReceipt receipt) {
        try {
            StringBuilder content = new StringBuilder();
            content.append("Xin Chào Bạn ");
            content.append("<p> Cảm ơn bản đã sử dụng dịch vụ của chúng tôi  </p>");
            content.append("<p> Yêu cầu của bạn đã đc chúng tối chấp nhận   </p>");
            content.append("<p> Làm ơn kiểm tra , thông tin đặt dịch vụ của bạn ở bên dưới </p>");
            content.append("<p> Ngày  đặt  :  \"" + formatDate(receipt.getTravelDate()) + "\"   </p>");
            content.append("<p> Số Lượng Người Lớn    :  \"" + receipt.getNumberTicketAdult() + "\"   </p>");
            content.append("<p> Số Lượng Trẻ Em   :  \"" + receipt.getNumberTicketChild() + "\"   </p>");
            content.append("<p> Tên Khu vui chơi   :  \"" + receipt.getActivitiesInfoDTO().getTitle() + "\"   </p>");
            content.append("<p> Địa Chỉ Khu vui chơi   :  \"" + receipt.getActivitiesInfoDTO().getAddress() + "\"   </p>");
            String subject = "FPT ---THÔNG TIN DỊCH VỤ";

            emailService.sendSimpleMessage(receipt.getContact().getEmail(), subject, content.toString());
        } catch (Exception e) {
            log.debug(e);
        }
    }


    private void sendMailRejectActivities(ActivitiesApproveBookingRequest request  ,  ActivitiesBookingReceipt receipt) {
        try {
            StringBuilder content = new StringBuilder();
            content.append("Xin Chào Bạn ");
            content.append("<p> Cảm ơn bản đã sử dụng dịch vụ của chúng tôi  </p>");
            content.append("<p> Yêu cầu của bạn đã bị hủy  , do một số lý do nên yêu cầu của bạn k được chấp nhận  </p>");
            content.append("<p  Có điều gì thắc mắc xin liên hệ với SĐT của quản lý khu vui chơi : \"" + receipt.getActivitiesInfoDTO().getPhonePartner() + "\"     </p>");
            String subject = "FPT ---THÔNG TIN DỊCH VỤ";

            emailService.sendSimpleMessage(receipt.getContact().getEmail(), subject, content.toString());
        } catch (Exception e) {
            log.debug(e);
        }
    }


    public void approveHotelBookingUserId(HotelApproveBookingRequest request) {
        HotelBookingReceipt receipt = hotelBookingReceiptRepository.findFirstByBookingId(request.getBookingId());
        if (receipt == null) {
            throw new ResourceNotFoundException();
        }

        receipt.setStatus(request.getStatus());
        hotelBookingReceiptRepository.save(receipt);
    }


    @Override
    public void approveHotelBooking(HotelApproveBookingRequest request) {
        HotelBookingReceipt receipt = hotelBookingReceiptRepository.findFirstByBookingId(request.getBookingId());
        if (receipt == null) {
            throw new ResourceNotFoundException();
        }

        if(request.getStatus().equals(BookingStatus.BOOKING_APPROVED)){
            receipt.setStatus(request.getStatus());
            sendMailApprovedHotel(request , receipt);
        }else{
            receipt.setStatus(request.getStatus());
            sendMailRejectHotel(request , receipt);
        }


        receipt.setStatus(request.getStatus());
        hotelBookingReceiptRepository.save(receipt);
    }



    private void sendMailApprovedHotel(HotelApproveBookingRequest restaurantApproveBookingRequest  , HotelBookingReceipt receipt) {
        try {
            StringBuilder content = new StringBuilder();
            content.append("Xin Chào Bạn ");
            content.append("<p> Cảm ơn bản đã sử dụng dịch vụ của chúng tôi  </p>");
            content.append("<p> Yêu cầu của bạn đã đc chúng tối chấp nhận   </p>");
            content.append("<p> Làm ơn kiểm tra , thông tin đặt dịch vụ của bạn ở bên dưới </p>");
            content.append("<p> Từ Ngày    :  \"" + formatDate(receipt.getCheckin()) + "\"   </p>");
            content.append("<p> Đến Ngày  :  \"" + formatDate(receipt.getCheckout()) + "\"   </p>");
            content.append("<p> Tên Hotel  :  \"" + receipt.getHotelInfoDTO().getTitle() + "\"   </p>");
            content.append("<p> Địa Chỉ Hotel   :  \"" + receipt.getHotelInfoDTO().getAddress() + "\"   </p>");
            String subject = "FPT ---THÔNG TIN DỊCH VỤ";

            emailService.sendSimpleMessage(receipt.getContact().getEmail(), subject, content.toString());
        } catch (Exception e) {
            log.debug(e);
        }
    }

    private void sendMailRejectHotel(HotelApproveBookingRequest restaurantApproveBookingRequest  ,  HotelBookingReceipt receipt) {
        try {
            StringBuilder content = new StringBuilder();
            content.append("Xin Chào Bạn ");
            content.append("<p> Cảm ơn bản đã sử dụng dịch vụ của chúng tôi  </p>");
            content.append("<p> Yêu cầu của bạn đã bị hủy  , do một số lý do nên yêu cầu của bạn k được chấp nhận  </p>");
            content.append("<p  Có điều gì thắc mắc xin liên hệ với SĐT của hotel : \"" + receipt.getHotelInfoDTO().getPhonePartner() + "\"     </p>");
            String subject = "FPT ---THÔNG TIN DỊCH VỤ";

            emailService.sendSimpleMessage(receipt.getContact().getEmail(), subject, content.toString());
        } catch (Exception e) {
            log.debug(e);
        }
    }


    public static String formatDate(Integer checkinDay) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(checkinDay.toString());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        return strDate;
    }







}
