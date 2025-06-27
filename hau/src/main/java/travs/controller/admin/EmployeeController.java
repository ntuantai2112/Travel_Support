package travs.controller.admin;

import travs.common.type.ApproveStatus;
import travs.request.activities.ActivitiesApproveRequest;
import travs.request.hotel.HotelApproveRequest;
import travs.request.restaurant.RestaurantApproveRequest;
import travs.response.BaseResponse;
import travs.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/employee")
@Log4j2
@AllArgsConstructor
public class EmployeeController {
    private EmployeeService employeeService;

    // api thay đổi trạng thái của hotel bởi employee
    @PostMapping("hotel/approve")
    public BaseResponse approveHotel(@RequestBody HotelApproveRequest hotelApproveRequest) {
        employeeService.approveHotel(hotelApproveRequest);
        return BaseResponse.ok();
    }

    // get list hotel bởi employee của employee
    @GetMapping("hotel/list")
    public BaseResponse getListHotel(@RequestParam(value = "hotel_name", required = false ) String hotelName,
                                     @RequestParam(value = "status", required = false ) ApproveStatus status,
                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return employeeService.getListHotel(hotelName, status, page, perPage);
    }

    // api thay đổi trạng thái của activities bởi employee
    @PostMapping("activities/approve")
    public BaseResponse approveActivities(@RequestBody ActivitiesApproveRequest activitiesApproveRequest) {
        employeeService.approveActivities(activitiesApproveRequest);
        return BaseResponse.ok();
    }

    // get list hotel bởi activities của employee
    @GetMapping("activities/list")
    public BaseResponse getListActivities(@RequestParam(value = "activities_name", required = false) String activitiesName,
                                          @RequestParam(value = "status", required = false) ApproveStatus status,
                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                          @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return employeeService.getListActivities(activitiesName, status, page, perPage);
    }

    // api thay đổi trạng thái của restaurant bởi employee
    @PostMapping("restaurant/approve")
    public BaseResponse approveRestaurant(@RequestBody RestaurantApproveRequest restaurantApproveRequest) {
        employeeService.approveRestaurant(restaurantApproveRequest);
        return BaseResponse.ok();
    }

    // get list hotel bởi restaurant của employee
    @GetMapping("restaurant/list")
    public BaseResponse getListRestaurant(@RequestParam(value = "restaurant_name", required = false) String restaurantName,
                                          @RequestParam(value = "status", required = false) ApproveStatus status,
                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                          @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return employeeService.getListRestaurant(restaurantName, status, page, perPage);
    }

}
