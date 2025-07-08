package travs.controller.acount;


import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import travs.common.type.BookingStatus;
import travs.request.activities.ActivitiesApproveBookingRequest;
import travs.request.hotel.HotelApproveBookingRequest;
import travs.request.restaurant.RestaurantApproveBookingRequest;
import travs.response.BaseResponse;
import travs.response.activities.ActivitiesBookingReceiptDTO;
import travs.response.hotel.HotelBookingReceiptDTO;
import travs.response.restaurant.RestaurantBookingReceiptDTO;
import travs.service.ReceiptService;
import travs.service.impl.restaurant.ReceiptRestaurantService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Log4j2
public class MemberController {

    private ReceiptService receiptService;
    private ReceiptRestaurantService receiptRestaurantService;

    // api trả về list book khu vui chơi của member
    @GetMapping("/activities/booking/list")
    public BaseResponse<List<ActivitiesBookingReceiptDTO>, Map<String, Integer>> getListActivitiesByUserId(@RequestParam(value = "status", required = false) BookingStatus status,
                                                                                                           @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                                                                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return receiptService.getListActivitiesReceiptByUserId(status, pageNo, pageSize);
    }


    // api trả về list book hotel của member
    @GetMapping("/hotel/booking/list")
    public BaseResponse<List<HotelBookingReceiptDTO>, Map<String, Integer>> getListHotelByPartner(@RequestParam(value = "status", required = false) BookingStatus status,
                                                                                                  @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                                  @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return receiptService.getListHotelReceiptByUserId(status, page, perPage);
    }

    // api trả về list book nhà hàng của member
    @GetMapping("/restaurant/booking/list")
    public BaseResponse<List<RestaurantBookingReceiptDTO>, Map<String, Integer>> getListRestaurantByPartner(@RequestParam(value = "status", required = false) BookingStatus status,
                                                                                                                @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                                                @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return receiptRestaurantService.getListRestaurantReceiptByUserId(status, page, perPage);
    }

    // api member thay đổi trạng thái  book của hotel
    @PostMapping("/booking/approve/hotel")
    public BaseResponse approveHotelBooking(@RequestBody HotelApproveBookingRequest request) {
        receiptService.approveHotelBookingUserId(request);
        return BaseResponse.ok();
    }

    // api member thay đổi trạng thái  book của activities
    @PostMapping("/booking/approve/activities")
    public BaseResponse approveActivitiesBooking(@RequestBody ActivitiesApproveBookingRequest request) {
        receiptService.approveActivitiesBookingUserId(request);
        return BaseResponse.ok();
    }

    // api member thay đổi trạng thái  book của restaurant
    @PostMapping("/booking/approve/restaurant")
    public BaseResponse approveRestaurantBooking(@RequestBody RestaurantApproveBookingRequest restaurantApproveBookingRequest) {
        receiptRestaurantService.restaurantApproStatusRepositoryUserId(restaurantApproveBookingRequest);
        return BaseResponse.ok();
    }


}
