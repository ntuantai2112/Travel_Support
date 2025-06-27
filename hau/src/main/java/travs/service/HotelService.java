package travs.service;

import travs.response.BaseResponse;
import travs.request.hotel.HotelRoomUploadBody;
import travs.request.hotel.HotelUploadBody;

public interface HotelService {
    BaseResponse get(String slug);

    BaseResponse uploadHotel(HotelUploadBody hotelUploadBody);

    void uploadHotelRoom(HotelRoomUploadBody hotelRoomUploadBody);

    void deleteHotel(String hotelCode);

    void deleteHotelRoom(Long roomId);

    BaseResponse getListHotel(Integer page, Integer perPage);

    BaseResponse getListRoom(String hotelCode, Integer page, Integer perPage);

    BaseResponse updateHotel(HotelUploadBody hotelUploadBody);

    void updateHotelRoom(HotelRoomUploadBody hotelRoomUploadBody);
}
