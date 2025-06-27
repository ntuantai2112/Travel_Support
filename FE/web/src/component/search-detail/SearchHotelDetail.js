import { faHeart } from '@fortawesome/free-regular-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import img1 from '../../image/unique/item4.png';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';
import '../../style/search-hotel-detail.scss';
import { typeRooms } from '../../utils/amenities';
import Gallery from '../hotel/Gallery.js';

export default function SearchHotelDetail() {
    let location = useLocation();
    let history = useHistory();
    let slug = location.state;
    let [hotel, setHotel] = useState();
    let [metas, setMetas] = useState();
    let [data, setData] = useState()
    let handleTypeRoomName = (value) => {
        return typeRooms.filter((item) => item.value == value)?.[0]?.name
    }
    let search = async () => {
       
        let path = `/hotel/${slug}`;
        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();
            setHotel(response?.data)
            setMetas(response?.meta)
            setData(response)
        }
    }
    useEffect(() => {
        search()
    }, [slug])
    let token = localStorage.getItem("token");
    let book = async (item) => {
        if (token) {
            history.push({
                pathname: '/booking-room',
                state: {
                    hotelCode: hotel?.code,
                    packageId: item?.id
                }
            })
        } else {
            let message = window.confirm("Hãy đăng nhập ngay để trải nghiệm dịch vụ của chúng tôi");
            if (message) {
                history.push('/login')

            }
        }

    }
    return (
        <>
            <div className="wrapper__search--detail container">
                <div className="name__detail">
                    <h3 className="title">{hotel?.title}</h3>
                </div>
                <div className="top__content--left">
                    <p className="content"
                        style={{ paddingLeft: 0, marginBottom: 25 }}
                    >Địa chỉ: {hotel?.address}</p>
                    <br />
                </div>

                <div className="row__gallery">
                    <Gallery images={hotel?.images} />
                </div>
                <div className="row__introduce" >
                    <h3 className="title">
                        Giới thiệu
                    </h3>
                    <p className="content">
                        {hotel?.description}
                    </p>
                </div>
                <br />


                <div className="row__order">
                    {/* <div className="top__title">
                        <h3 className="title">Chọn phòng</h3>
                        <ul className="list__top">
                            <li className="list__top--item">Chọn Ngày</li>
                            <li className="list__top--item">Mức Giá</li>
                            <li className="list__top--item">Loại Phòng</li>
                            <li className="list__top--item">Chọn Lại</li>
                        </ul>
                    </div> */}
                    <div className="table__order">
                        <div className="background__bar"></div>
                        <div className="row title__col">
                            <div className="col-3 title__item ">Ảnh</div>
                            <div className="col-3 title__item">Loại chỗ nghỉ</div>
                            <div className="col-3 title__item">Giá hôm nay</div>
                            <div className="col-3 title__item">Book</div>
                            {/* <div className="col-2 title__item">Số phòng còn lại</div> */}
                            {/* <div className="col-2 title__item">Số lượng đặt</div> */}
                            {/* <div className="col-2 title__item">Chi phí thanh toán</div> */}
                        </div>
                        {metas?.map((item, index) => {
                            return (
                                <div className="row row__item--order">
                                    <div className="col-3 col__image">
                                        <div className="wrapper__image">
                                            <div className="wrapper__image--top">
                                                <img src={`${BASE_URL_DOWNLOAD}${item?.image}`} alt="img" />
                                            </div>

                                        </div>
                                    </div>
                                    <div className="col-3 col__content--item first__child">
                                        {handleTypeRoomName(item?.roomType)}
                                        <div className="content__item">

                                            {item?.amenities?.map((ame, idx) => {
                                                return (
                                                    <p key={idx}>
                                                        <img src={ame?.icon} width={24} height={24} style={{ marginRight: 5 }} />
                                                        {ame?.name}
                                                    </p>
                                                )
                                            })}
                                        </div>

                                    </div>
                                    <div className="col-3 col__content--item">
                                        <span className="price">{item?.price}</span> {item?.currency}
                                    </div>

                                    <div className="col-3 col__content--item">
                                        <div className="wrapper__order--btn" onClick={() => {
                                            book(item)
                                        }}>
                                            <span className="order__btn">Đặt ngay</span>
                                        </div>
                                    </div>
                                </div>
                            )
                        })}


                        {/* <div className="row row__total">
                            <div className="col-4 col__total--title">
                                Tổng:
                            </div>
                            <div className="col-2 bd__left"></div>
                            <div className="col-2 bd__left"></div>
                            <div className="col-2 bd__left">0</div>
                            <div className="col-2 bd__left">0</div>
                        </div>
                        <div className="wrapper__order--btn">
                            <span className="order__btn">Đặt ngay</span>
                        </div> */}
                    </div>
                </div>
            </div>
        </>
    )
}