import React from 'react';
import '../../../style/booking/share/bill-booking-room.scss';
import img1 from '../../../image/unique/item4.png'

export default function BillBookingRoom() {
    return (
        <>
            <div className="wrapper__bill-booking-room container" id="bill__room">
                <div className="main__bill">
                    <div className="name__content">
                        <div className="wrapper__image">
                            <img src={img1} alt="img" />
                        </div>
                        <div className="name__hotel">
                            <h5 className="title">
                                Khách sạn duy trịnh trinh
                            </h5>
                            <p className="address">
                                số 43 anfakafa, ahwif ưa, aihwiahwid
                            </p>
                        </div>
                    </div>
                    <div className="time__content">
                        <div className=" checkin item">
                            <p className="title">Thời gian Checkin</p>
                            <p className="info">14:00 Thứ 2 <br /> Ngày ...</p>
                        </div>
                        <div className=" checkout item">
                            <p className="title">Thời gian Checkout</p>
                            <p className="info">14:00 Thứ 2 <br /> Ngày ...</p>
                        </div>
                        <div className=" room item">
                            <div className="wrapper">
                                <p id="amount__room">1 Phòng</p>
                                <p id="time__stay">1 Đêm</p>
                            </div>
                        </div>
                    </div>
                    <div className="detail__content">
                            <p className="content">
                                <span>Loại phòng</span>
                                <span className="type__room">Phòng Delux</span>
                            </p>
                            <p className="content">
                                <span>Bữa sáng</span>
                                <span className="breakfirst">Buffet miễn phí</span>
                            </p>
                            <p className="content">
                                <span>Hủy đặt</span>
                                <span className="cancel">Có thể hủy sau khi đặt 8 giờ</span>
                            </p>
                    </div>
                    <hr/>
                    <div className="announce">Giá phòng đã áp dụng khuyến mãi 2%</div>
                </div>
                <div className="sm__banner">
                    BANNER
                </div>
            </div>
        </>
    )
}