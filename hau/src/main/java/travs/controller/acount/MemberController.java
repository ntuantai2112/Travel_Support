package travs.controller.acount;


import travs.common.type.BookingStatus;
import travs.service.impl.restaurant.ReceiptRestaurantService;
import travs.request.activities.ActivitiesApproveBookingRequest;
import travs.request.hotel.HotelApproveBookingRequest;
import travs.request.restaurant.RestaurantApproveBookingRequest;
import travs.response.BaseResponse;
import travs.service.ReceiptService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
@Log4j2
public class MemberController {

    private ReceiptService receiptService;
    private ReceiptRestaurantService receiptRestaurantService;

    // api trả về list book khu vui chơi của member
    @GetMapping("/activities/booking/list")
    public BaseResponse getListActivitiesByUserId(@RequestParam(value = "status", required = false) BookingStatus status,
                                                  @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return receiptService.getListActivitiesReceiptByUserId(status, page, perPage);
    }


    // api trả về list book hotel của member
    @GetMapping("/hotel/booking/list")
    public BaseResponse getListHotelByPartner(@RequestParam(value = "status", required = false) BookingStatus status,
                                              @RequestParam(value = "page", defaultValue = "0") Integer page,
                                              @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return receiptService.getListHotelReceiptByUserId(status, page, perPage);
    }

    // api trả về list book nhà hàng của member
    @GetMapping("/restaurant/booking/list")
    public BaseResponse getListRestaurantByPartner(@RequestParam(value = "status", required = false) BookingStatus status,
                                                   @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return receiptRestaurantService.getListRestaurantReceiptByUserId(status, page, perPage);
    }

    // api member thay đổi trạng thái  book của hotel
    @PostMapping("booking/approve/hotel")
    public BaseResponse approveHotelBooking(@RequestBody HotelApproveBookingRequest request) {
        receiptService.approveHotelBooking(request);
        return BaseResponse.ok();
    }

    // api member thay đổi trạng thái  book của activities
    @PostMapping("booking/approve/activities")
    public BaseResponse approveActivitiesBooking(@RequestBody ActivitiesApproveBookingRequest request) {
        receiptService.approveActivitiesBookingUserId(request);
        return BaseResponse.ok();
    }

    // api member thay đổi trạng thái  book của restaurant
    @PostMapping("booking/approve/restaurant")
    public BaseResponse approveRestaurantBooking(@RequestBody RestaurantApproveBookingRequest restaurantApproveBookingRequest) {
        receiptRestaurantService.restaurantApproStatusRepositoryUserId(restaurantApproveBookingRequest);
        return BaseResponse.ok();
    }


}
