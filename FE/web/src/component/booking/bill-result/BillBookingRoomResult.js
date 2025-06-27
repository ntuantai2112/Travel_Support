import React from 'react';
import '../../../style/booking/share/bill-booking-room.scss';
import '../../../style/booking/share/bill-booking-room-result.scss';
import moment from 'moment';

export default function BillBookingRoomResult({data}) {
    return (
        <>
            <div className="wrapper__bill-booking-room container" id="bill__room--result">
                <div className="main__bill">
                    <h5 className="title">Chi tiết hóa đơn</h5>
                    <div className="top__content">
                        <p className="time__booking">
                            <span>Ngày đặt</span>
                            <span className="time">
                                {moment(data?.checkin,"YYYYMMDD").format("DD-MM-YYYY")}
                            </span>
                        </p>
                        <p className="time__booking">
                            <span>Đến ngày</span>
                            <span className="time">
                                {moment(data?.checkout,"YYYYMMDD").format("DD-MM-YYYY")}
                            </span>
                        </p>
                        <p className="payment__method">
                            <span>Phương thức thanh toán</span>
                            <span className="method">Tiền mặt</span>
                        </p>
                    </div>

                    <div className="middle__content">
                        <p className="price-per-night">
                            <span>Số ngày ở</span>
                            <span id="price__fixed"> {data?.totalNights} </span>
                        </p>
                        {/* <p className="payment__method">
                            <span className="listed">1 phòng x 1 đêm</span>
                            <span id="price__listed">  &#8363;851,214</span>
                        </p>
                        <p className="tax__include">
                            <span>Bao gồm thuế</span>
                            <span className="tax"></span>
                        </p>
                        <p className="promotion">
                            <span>bùng nổ mùa hè</span>
                            <span className="discount">-&#8363;151,214</span>
                        </p> */}
                    </div>
                    <div className="total__price">
                        <span>Tổng giá</span>
                        <span id="total">{(data?.price)} VNĐ</span>
                    </div>
                    
                </div>
                
            </div>
        </>
    )
}