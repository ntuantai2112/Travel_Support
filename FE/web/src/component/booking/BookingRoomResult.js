import React, { useEffect, useState } from 'react';
import '../../style/booking/booking-room-result.scss';
import { faChevronUp } from '@fortawesome/free-solid-svg-icons';
import { faChevronDown } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import moment from 'moment';
import { amenities, typeRooms } from '../../utils/amenities';

export default function BookingRoomResult({ data }) {
    const [arr, setArr] = useState([]);
    let arrAmenities = data?.hotelInfoDTO?.amenities;

    let handleArr = () => {
        let x = [];
        for (let i = 0; i < arrAmenities?.length; i++) {
            let newArr = amenities?.filter(item => item.id == arrAmenities[i])?.[0];
            x.push(newArr)
        }
        setArr(x);
    }
    let typeRoomName = typeRooms.filter((item) => item.value == data?.packageInfo?.roomType)?.[0]?.name;
    useEffect(() => {
        handleArr()
    }, [data])
    return (
        <>
            <div className="wrapper__booking-room--result">
                <div className="result__booking">
                    <div className="hotel__detail">
                        <h4 className="title">
                            {data?.hotelInfoDTO?.title}
                        </h4>
                        <hr />
                        <div className="row" id="first__detail">
                            <div className="col-3 col__left col__content">
                                <p className="address">Địa chỉ</p>
                                <p className="phone__number">Số điện thoại</p>
                                <p className="time__checkin">Thời gian checkin</p>
                                <p className="time__checkout">Thời gian checkout</p>
                                <p className="status__booking">Tình trạng đặt chỗ</p>
                            </div>
                            <div className="col-9 col__right col__content">
                                <p className="address">{data?.hotelInfoDTO?.address}</p>
                                <p className="phone__number">{data?.contact?.phone}</p>
                                <p className="time__checkin bold__text">{moment(data?.checkin.toString(), "YYYYMMDD").format("DD-MM-YYYY")}</p>
                                <p className="time__checkout bold__text">{moment(data?.checkout.toString(), "YYYYMMDD").format("DD-MM-YYYY")}</p>
                                <p className="status__booking color__status">Gửi yêu cầu đặt chỗ thành công.</p>
                            </div>
                        </div>
                        
                        {/* <div className="row" id="second__detail">
                            <div className="col-3 col__left col__content">
                                <p className="amount__order">Số đơn hàng</p>
                                <p className="booking__code">mã đặt chỗ</p>
                                <p className="name__guest ">Tên khách</p>
                                <p className="amount__room">Số phòng</p>
                                <p className="amount__guest ">Số lượng khách</p>
                            </div>
                            <div className="col-9 col__right col__content">
                                <p className="amount__order">129878152</p>
                                <p className="booking__code">8162498194</p>
                                <p className="name__guest text-upper">Bui Thang</p>
                                <p className="amount__room">69</p>
                                <p className="amount__guest ">
                                    3
                                    <br />
                                    <span className="detail__guest">Người lớn (3)</span>
                                </p>
                            </div>
                        </div> */}
                        <hr />
                        <div className="row" id="third__detail">
                            <div className="col-3 col__left col__content">
                                <p className="type__room">Loại Phòng</p>

                                <p className="name__guest ">tiện nghi</p>
                            </div>
                            <div className="col-9 col__right col__content">
                                <p className="type__room text-upper color__status">{typeRoomName}</p>

                                <p className="amount__guest ">
                                    {arr?.map((item, index) => {
                                        return (
                                            <p key={index}>{item?.name} </p>
                                        )
                                    })}
                                    {/* FULL DỊCH VỤ
                                    <br />
                                    Giặt là trả tận phòng
                                    <br />
                                    wifi free
                                    <br />
                                    Khách sạn không khói thuốc */}
                                </p>
                            </div>
                        </div>
                    </div>
                    <hr />
                    <div className="cancel__policy">
                        <h4 className="title">
                            Chính sách hủy phòng
                        </h4>
                        <p className="content__policy mg-top">Chính sách tùy theo yêu cầu của khách sạn. Thường sẽ do bên khách sạn liên hệ. </p>
                    </div>
                    {/* <div className="hotel__policy">
                        <h4 className="title">
                            Chính sách khách sạn
                            <span className="wrap__section">
                                <FontAwesomeIcon icon={faChevronUp} id="wrap-btn" />
                            </span>
                        </h4>
                        <p className="content__policy mg-top">Không có thang máy</p>
                        <p className="content__policy">Không đảm bảo có phòng cách âm</p>
                        <p className="content__policy">Nơi lưu trú xác nhận đang áp dụng biện pháp vệ sinh tăng cường</p>
                        <p className="content__policy mg-top bolder__text">Nhận phòng: <span id="time__take">Từ 14:00</span></p>
                        <p className="content__policy bolder__text">trả phòng: <span id="time__leave">đến 16:00</span></p>
                        <p className="content__policy mg-top bolder__text">Thực hành làm sạch và an toàn</p>
                        <ul>
                            <li className="content__policy">Nơi lưu trú được vệ sinh bằng dung dịch khử trùng</li>
                            <li className="content__policy">Những bề mặt thường có nhiều tiếp xúc được vệ sinh bằng dung dịch khử trùng</li>
                            <li className="content__policy">Chỉ nhân viên thiết yếu - CÓ</li>
                        </ul>
                    </div> */}
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