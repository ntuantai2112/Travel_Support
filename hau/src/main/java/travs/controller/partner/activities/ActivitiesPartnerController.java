package travs.controller.partner.activities;

import travs.common.type.BookingStatus;
import travs.request.activities.ActivitiesApproveBookingRequest;
import travs.request.activities.ActivitiesGameUploadBody;
import travs.request.activities.ActivitiesUploadBody;
import travs.response.BaseResponse;
import travs.service.ActivitiesService;
import travs.service.ReceiptService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/partner/activities")
@Log4j2
@AllArgsConstructor
public class ActivitiesPartnerController {
    private ReceiptService receiptService;
    private ActivitiesService activitiesService;


 // api thêm activities bởi partner
    @PostMapping("/upload")
    public BaseResponse uploadActivities(@ModelAttribute ActivitiesUploadBody activitiesUploadBody) {
        return activitiesService.uploadActivities(activitiesUploadBody);
    }

    // api cập nhật activities bởi partner
    @PostMapping("/update")
    public BaseResponse updateActivities(@ModelAttribute ActivitiesUploadBody activitiesUploadBody) {
        return activitiesService.updateActivities(activitiesUploadBody);
    }

    // api thêm trò chơi của  activities bởi partner
    @PostMapping("game/upload")
    public BaseResponse uploadGameActivities(@ModelAttribute ActivitiesGameUploadBody activitiesGameUploadBody) {
        return activitiesService.uploadGameActivities(activitiesGameUploadBody);
    }

    // api cập nhật trò chơi activities bởi partner
    @PostMapping("game/update")
    public BaseResponse updateGameActivities(@ModelAttribute ActivitiesGameUploadBody activitiesGameUploadBody) {
        return activitiesService.updateGameActivities(activitiesGameUploadBody);
    }

    // api trả về list activities bởi partner
    @GetMapping("list")
    public BaseResponse getListActivities(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                          @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return activitiesService.getListActivities(page, perPage);
    }

    // api thay đổi trạng thái book activities bởi partner
    @PostMapping("booking/approve")
    public BaseResponse approveActivitiesBooking(@RequestBody ActivitiesApproveBookingRequest request) {
        receiptService.approveActivitiesBooking(request);
        return BaseResponse.ok();
    }

    // api trả về list  booking activities bởi partner
    @GetMapping("booking/list")
    public BaseResponse getListActivitiesByPartner(@RequestParam(value = "status", required = false) BookingStatus status,
                                                   @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "perPage", defaultValue = "5") Integer perPage) {
        return receiptService.getListActivitiesReceipt(status, page, perPage);
    }


    // xóa  activities
    @PostMapping("delete/{activitiesCode}")
    public BaseResponse deleteActivities(@PathVariable String activitiesCode) {
        activitiesService.deleteActivities(activitiesCode);
        return BaseResponse.ok();
    }

    // xóa  trò chơi activities
    @PostMapping("game/delete/{id}")
    public BaseResponse deleteGameActivities(@PathVariable Integer id) {
        activitiesService.deleteGameActivities(id);
        return BaseResponse.ok();
    }

}
