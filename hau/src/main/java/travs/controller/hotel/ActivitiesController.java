package travs.controller.hotel;

import travs.request.activities.BookingActivitiesRequest;
import travs.response.BaseResponse;
import travs.service.ActivitiesService;
import travs.service.ReceiptService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/activities")
@Log4j2
@AllArgsConstructor
public class ActivitiesController {
    private ReceiptService receiptService;
    private ActivitiesService activitiesService;


    // get chi tiết activities bởi slug (slug : là trường trong DB )
    @GetMapping("/{slug}")
    public BaseResponse get(@PathVariable String slug) {
        return activitiesService.get(slug);
    }


  // api book activities
    @PostMapping("/book")
    public BaseResponse bookingHotel(@Valid @RequestBody BookingActivitiesRequest bookingRequest) {
        return receiptService.bookingActivities(bookingRequest);
    }

    // api get detail  activities by id of the activities_receipt table
    @GetMapping("/book-detail/{id}")
    public BaseResponse bookingHotel(@PathVariable("id") String id) {
        return receiptService.getReceiptDetailActivities(id);
    }
}
