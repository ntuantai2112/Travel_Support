import React, { useEffect, useState } from 'react';
import '../../style/booking/booking-room-result.scss';
import '../../style/booking/booking-ticket-result.scss';
import { faChevronUp } from '@fortawesome/free-solid-svg-icons';
import { faChevronDown } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useLocation } from 'react-router-dom';
import API from '../../lib/API';
import moment from 'moment';

export default function BookingRestaurantResult({ data }) {
    console.log(data);

    return (

        <>
            <div className="wrapper__booking-room--result">
                <div className="result__booking">
                    <div className="hotel__detail">
                        <div style={{display:'flex'}}>
                            Nhà hàng:  <h4 className="title">
                               &nbsp;  {data?.restaurantInfoDTO?.title}
                            </h4>
                        </div>

                        <hr />
                        <div className="row" id="first__detail">
                            <div className="col-3 col__left col__content">
                                <p className="address">Địa chỉ</p>
                                {/* <p className="phone__number">Số điện thoại</p> */}
                                <p className="time__checkin">Ngày đặt</p>
                                <p className="status__booking">Tình trạng đặt vé</p>
                            </div>
                            <div className="col-9 col__right col__content">
                                <p className="address">{data?.restaurantInfoDTO?.address}</p>
                                {/* <p className="phone__number">012515210</p> */}
                                <p className="time__checkin bold__text">{data?.checkinTime} {moment(data?.checkinDay, "YYYYMMDD").format("DD-MM-YYYY")} </p>
                                <p className="status__booking color__status">Thành công</p>
                            </div>
                        </div>
                        <hr />
                        <div className="row" id="second__detail">
                            <div className="col-3 col__left col__content">
                                {/* <p className="amount__order">Số đơn hàng</p> */}
                                <p className="booking__code">mã đặt vé</p>
                                <p className="name__guest ">Tên khách:</p>
                                <p className="amount__guest ">Email:</p>
                                <p className="name__guest ">Số điện thoại:</p>
                            </div>
                            <div className="col-9 col__right col__content">
                                <p className="amount__order">{data?.bookingId}</p>
                                {/* <p className="booking__code">8162498194</p> */}
                                <p className="name__guest text-upper">{data?.contact?.firstName} {data?.contact?.lastName} </p>
                                <p className="amount__order">{data?.contact?.email}</p>
                                <p className="amount__order">{data?.contact?.phone}</p>
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
                        {/* <h4 className="title">
                            Tiện tích nhà hàng
                            <span className="wrap__section">
                                <FontAwesomeIcon icon={faChevronUp} id="wrap-btn" />
                            </span>
                        </h4> */}
                        {/* <p className="content__policy mg-top">Không có camera</p>
                        <p className="content__policy">Không đảm bảo có phòng cách âm</p>
                        <p className="content__policy">Nơi thỏa sức tôi khoe cá tính và độ mạnh mẽ của đàn ông, nơi những con thú hoang được lên ngôi </p>
                        <p className="content__policy mg-top bolder__text">Giờ mở cửa: <span id="time__take">20:00 PM</span></p>
                        <p className="content__policy bolder__text">giờ đóng cửa: <span id="time__leave">06:00 AM</span></p> */}
                        <p className="content__policy mg-top bolder__text">Thực hành làm sạch và an toàn</p>
                        {/* <ul>
                            <li className="content__policy">Nơi vui chơi được vệ sinh bằng dung dịch khử trùng</li>
                            <li className="content__policy">Những bề mặt thường có nhiều tiếp xúc được vệ sinh bằng dung dịch khử trùng</li>
                            <li className="content__policy">free áo mưa</li>
                            <li className="content__policy">Chỉ nhân viên thiết yếu - CÓ</li>
                        </ul> */}
                    </div>
                    <hr />
                    {/* <div className="important__info">
                        <h4 className="title">
                            Thông tin quan trọng
                            <span className="wrap__section">
                                <FontAwesomeIcon icon={faChevronDown} id="wrap-btn" />
                            </span>
                        </h4>
                    </div>
                    <hr /> */}
                </div>
            </div>
        </>
    )
}