package travs.controller.partner.hotel;

import travs.common.type.BookingStatus;
import travs.request.hotel.HotelApproveBookingRequest;
import travs.request.hotel.HotelRoomUploadBody;
import travs.request.hotel.HotelUploadBody;
import travs.response.BaseResponse;
import travs.service.HotelService;
import travs.service.ReceiptService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/partner/hotel")
@Log4j2
@AllArgsConstructor
public class HotelPartnerController {
    private HotelService hotelService;
    private ReceiptService receiptService;


    // api thêm hotel bởi partner
    @PostMapping("upload")
    public BaseResponse uploadHotel(@ModelAttribute HotelUploadBody hotelUploadBody) {
        return hotelService.uploadHotel(hotelUploadBody);
    }

    // api cập nhật hotel bởi partner
    @PostMapping("update")
    public BaseResponse updateHotel(@ModelAttribute HotelUploadBody hotelUploadBody) {
        return hotelService.updateHotel(hotelUploadBody);
    }

    // api thêm phòng của  hotel bởi partner
    @PostMapping("room/upload")
    public BaseResponse uploadRoomHotel(@ModelAttribute HotelRoomUploadBody hotelRoomUploadBody) {
        hotelService.uploadHotelRoom(hotelRoomUploadBody);
        return BaseResponse.ok();
    }

    // api cập nhật phòng hotel bởi partner
    @PostMapping("room/update")
    public BaseResponse updateRoomHotel(@ModelAttribute HotelRoomUploadBody hotelRoomUploadBody) {
        hotelService.updateHotelRoom(hotelRoomUploadBody);
        return BaseResponse.ok();
    }

    // api trả về list hotel bởi partner
    @GetMapping("list")
    public BaseResponse getListHotel(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return hotelService.getListHotel(page, perPage);
    }

    // api trả về list room hotel bởi partner
    @GetMapping("list/room")
    public BaseResponse getListRoom(@RequestParam(value = "hotel_code", required = true) String hotelCode,
                                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return hotelService.getListRoom(hotelCode, page, perPage);
    }

    // api thay đổi trạng thái book hotel bởi partner
    @PostMapping("booking/approve")
    public BaseResponse approveHotelBooking(@RequestBody HotelApproveBookingRequest request) {
        receiptService.approveHotelBooking(request);
        return BaseResponse.ok();
    }

    // api trả về list  booking hotel bởi partner
    @GetMapping("booking/list")
    public BaseResponse getListHotelByPartner(@RequestParam(value = "status", required = false) BookingStatus status,
                                              @RequestParam(value = "page", defaultValue = "0") Integer page,
                                              @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return receiptService.getListHotelReceipt(status, page, perPage);
    }

    // xóa  hotel
    @PostMapping("delete/{hotelCode}")
    public BaseResponse deleteHotel(@PathVariable String hotelCode) {
        hotelService.deleteHotel(hotelCode);
        return BaseResponse.ok();
    }

    // xóa phòng hotel
    @PostMapping("room/delete/{roomId}")
    public BaseResponse deleteHotelRoom(@PathVariable Long roomId) {
        hotelService.deleteHotelRoom(roomId);
        return BaseResponse.ok();
    }

}
