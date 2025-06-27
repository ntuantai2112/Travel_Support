package travs.controller.restaurant;

import travs.request.restaurant.BookingRestaurantRequest;
import travs.service.impl.restaurant.ReceiptRestaurantService;
import travs.response.BaseResponse;
import travs.service.ReceiptService;
import travs.service.impl.restaurant.RestaurantService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/restaurant")
@Log4j2
@AllArgsConstructor
public class RestaurantController {
    private ReceiptService receiptService;
    private ReceiptRestaurantService receiptRestaurantService;
    private RestaurantService restaurantService;



    // get chi tiết bai slug
    @GetMapping("/{slug}")
    public BaseResponse get(@PathVariable String slug) {
        return restaurantService.get(slug);
    }

    // book restaurant
    @PostMapping("/book")
    public BaseResponse bookingRestaurant(@Valid @RequestBody BookingRestaurantRequest bookingRestaurantRequest) {
        return receiptRestaurantService.bookingRestaurant(bookingRestaurantRequest);
    }

    // get chi tiết  book
    @GetMapping("/book-detail/{id}")
    public BaseResponse bookingRestaurantDetail(@PathVariable("id") String id) {
        return receiptRestaurantService.getReceiptRestaurantDetail(id);
    }

}
