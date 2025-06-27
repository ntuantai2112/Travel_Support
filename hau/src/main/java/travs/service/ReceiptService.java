package travs.service;

import travs.common.type.BookingStatus;
import travs.response.BaseResponse;
import travs.request.activities.ActivitiesApproveBookingRequest;
import travs.request.activities.BookingActivitiesRequest;
import travs.request.hotel.BookingRequest;
import travs.request.hotel.HotelApproveBookingRequest;

public interface ReceiptService {

    BaseResponse bookingHotel(BookingRequest bookingRequest);

    void approveHotelBooking(HotelApproveBookingRequest request);

    void approveHotelBookingUserId(HotelApproveBookingRequest request);

    BaseResponse getListHotelReceipt(BookingStatus status, Integer page, Integer perPage);

    BaseResponse getReceiptDetail(String id);

    void approveActivitiesBooking(ActivitiesApproveBookingRequest request);

    void approveActivitiesBookingUserId(ActivitiesApproveBookingRequest request);

    BaseResponse getListActivitiesReceipt(BookingStatus status, Integer page, Integer perPage);

    BaseResponse getListActivitiesReceiptByUserId(BookingStatus status, Integer page, Integer perPage);

    BaseResponse getListHotelReceiptByUserId(BookingStatus status, Integer page, Integer perPage);

    BaseResponse getReceiptDetailActivities(String id);

    BaseResponse bookingActivities(BookingActivitiesRequest bookingRequest);
}
