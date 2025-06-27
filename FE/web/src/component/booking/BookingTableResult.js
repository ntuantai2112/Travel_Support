import React from 'react';
import '../../style/booking/booking-room-result.scss';
import '../../style/booking/booking-ticket-result.scss';
import { faChevronUp } from '@fortawesome/free-solid-svg-icons';
import { faChevronDown } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export default function BookingTableResult() {
    return (
        <>
            <div className="wrapper__booking-room--result">
                <div className="result__booking">
                    <div className="hotel__detail">
                        <h4 className="title">
                            nhà hàng bà tân siêu to khổng lồ
                        </h4>
                        <hr />
                        <div className="row" id="first__detail">
                            <div className="col-3 col__left col__content">
                                <p className="address">Địa chỉ</p>
                                <p className="phone__number">Số điện thoại</p>
                                <p className="time__checkin">Thời gian nhận bàn</p>
                                <p className="status__booking">Tình trạng đặt bàn</p>
                            </div>
                            <div className="col-9 col__right col__content">
                                <p className="address">Số 1 Đường 1 Xã 1 aksnfjanbwfknawf</p>
                                <p className="phone__number">012515210</p>
                                <p className="time__checkin bold__text">14:00 Thứ 2 - 29/7/2021</p>
                                <p className="status__booking color__status">Thành công</p>
                            </div>
                        </div>
                        <hr />
                        <div className="row" id="second__detail">
                            <div className="col-3 col__left col__content">
                                <p className="booking__code">mã đặt bàn</p>
                                <p className="name__guest ">Tên khách</p>
                                <p className="amount__guest ">Số khách hàng</p>
                            </div>
                            <div className="col-9 col__right col__content">
                                <p className="booking__code">8162498194</p>
                                <p className="name__guest text-upper">Bui Thang</p>
                                <p className="amount__guest ">
                                    2 người
                                </p>
                            </div>
                        </div>
                    </div>
                    <hr />
                    <div className="cancel__policy">
                        <h4 className="title">
                            Chính sách hủy
                        </h4>
                        <p className="content__policy mg-top">Chính sách tùy theo yêu cầu của nhà hàng </p>
                    </div>
                    <hr />
                    <div className="hotel__policy">
                        <h4 className="title">
                            Chính sách nhà hàng
                            <span className="wrap__section">
                                <FontAwesomeIcon icon={faChevronUp} id="wrap-btn" />
                            </span>
                        </h4>
                        <p className="content__policy mg-top">Không có camera</p>
                        <p className="content__policy">Không đảm bảo có phòng cách âm</p>
                        <p className="content__policy">Nơi thỏa sức ăn thỏa sức nhồi nhét bất cứ gì vào mồm</p>
                        <p className="content__policy mg-top bolder__text">Giờ mở cửa: <span id="time__take">12:00 PM</span></p>
                        <p className="content__policy bolder__text">giờ đóng cửa: <span id="time__leave">23:00 PM</span></p>
                        <p className="content__policy mg-top bolder__text">Thực hành làm sạch và an toàn</p>
                        <ul>
                            <li className="content__policy">Nơi vui chơi được vệ sinh bằng dung dịch khử trùng</li>
                            <li className="content__policy">Những bề mặt thường có nhiều tiếp xúc được vệ sinh bằng dung dịch khử trùng</li>
                            <li className="content__policy">free nước rửa tay</li>
                            <li className="content__policy">Chỉ nhân viên thiết yếu - CÓ</li>
                        </ul>
                    </div>
                    <hr />
                    <div className="important__info">
                        <h4 className="title">
                            Thông tin quan trọng
                            <span className="wrap__section">
                                <FontAwesomeIcon icon={faChevronDown} id="wrap-btn" />
                            </span>
                        </h4>
                    </div>
                    <hr />
                </div>
            </div>
        </>
    )
}