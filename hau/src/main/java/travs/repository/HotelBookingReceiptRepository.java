package travs.repository;

import travs.common.type.BookingStatus;
import travs.entity.hotel.HotelBookingReceipt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelBookingReceiptRepository extends JpaRepository<HotelBookingReceipt, String> {

    HotelBookingReceipt findFirstByBookingId(String bookingId);

    List<HotelBookingReceipt> findAllByPartnerId(String partnerId, Pageable pageable);

    List<HotelBookingReceipt> findAllByPartnerIdAndStatus(String partnerId, BookingStatus status, Pageable pageable);

    int countAllByPartnerId(String partnerId);

    int countAllByPartnerIdAndStatus(String partnerId, BookingStatus status);

    HotelBookingReceipt findFirstById(String receiptId);



    List<HotelBookingReceipt> findAllByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    List<HotelBookingReceipt> findAllByUserIdAndStatusOrderByCreatedAtDesc(String userId, BookingStatus status, Pageable pageable);

    int countAllByUserId(String userId);

    int countAllByUserIdAndStatus(String userId, BookingStatus status);

}
