package travs.repository.restaurant;

import travs.common.type.BookingStatus;
import travs.entity.restaurant.RestaurantBookingReceipt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantBookingReceiptRepository extends JpaRepository<RestaurantBookingReceipt, String> {

    RestaurantBookingReceipt findFirstByBookingId(String bookingId);

    List<RestaurantBookingReceipt> findAllByPartnerIdOrderByCreatedAtDesc(String partnerId, Pageable pageable);

    List<RestaurantBookingReceipt> findAllByPartnerIdAndStatusOrderByCreatedAtDesc(String partnerId , BookingStatus status, Pageable pageable);

    int countAllByPartnerId(String partnerId);

    int countAllByPartnerIdAndStatus(String partnerId  ,BookingStatus status);

    RestaurantBookingReceipt findFirstById(String receiptId);


    List<RestaurantBookingReceipt> findAllByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    List<RestaurantBookingReceipt> findAllByUserIdAndStatusOrderByCreatedAtDesc(String userId , BookingStatus status, Pageable pageable);

    int countAllByUserId(String userId);

    int countAllByUserIdAndStatus(String userId  ,BookingStatus status);

}
