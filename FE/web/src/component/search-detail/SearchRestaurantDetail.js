import React, { useEffect, useState } from 'react';
import Gallery from '../hotel/Gallery';
import '../../style/search-restaurant-detail.scss'
import { faParking } from '@fortawesome/free-solid-svg-icons';
import { faWifi } from '@fortawesome/free-solid-svg-icons';
import { faSun } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import RatingStar from '../share/RatingStar';
import Images from './gallery-carousel/Images';
import { useHistory, useLocation } from 'react-router-dom';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';

export default function SearchRestaurantDetail() {
    const [selectedImg, setSelectedImg] = useState(Images[0]);
    let location = useLocation();
    let history = useHistory();
    let slug = location.state;
    let [restaurants, setRestaurants] = useState();
    let [menus, setMenus] = useState();
    let search = async () => {
        let path = `/restaurant/${slug}`;
        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();
            setRestaurants(response?.data)
            setMenus(response?.data?.menus)
        }
    }

    useEffect(() => {
        search()
    }, [slug])
    let token = localStorage.getItem("token");
    let book = async () => {
        if (token) {
            history.push({
                pathname: '/booking-restaurant',
                state: restaurants?.code
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
                    <h3 className="title">{restaurants?.title}</h3>
                </div>
                <div className="row__gallery">
                    <Gallery images={restaurants?.images} />
                </div>
                <div className="row__introduce">
                    <h3 className="title">
                        Giới thiệu
                    </h3>
                    <div>
                        {restaurants?.description}
                    </div>
                    <hr />
                    <h3 className="title">
                        Địa điểm
                    </h3>
                    <div>
                        {restaurants?.address}
                    </div>
                    <hr />
                    <h3 className="title">
                        Tiện ích
                    </h3>
                    <div>
                        {restaurants?.feature?.map((item, index) => {
                            return (
                                <p key={index}>
                                    - {item?.name}
                                </p>

                            )
                        })}
                    </div>
                    <hr />

                    <h3 className="title">
                        Thực đơn nhà hàng
                    </h3>
                    <div className="container">
                        <div className="row">
                            {menus?.map((item, index) => {
                                return (
                                    <MenuItem item={item} key={index} />
                                )
                            })}
                        </div>
                    </div>
                    <div style={{
                        display: 'flex', justifyContent: 'center', alignItems: 'center',
                        marginTop: 20
                    }}>
                        <div style={{
                            width: 100, height: 50, display: 'flex', justifyContent: 'center', alignItems: 'center',
                            backgroundColor: '#45a1d5', borderRadius: 10, color: '#fff', cursor: 'pointer'
                        }}
                            onClick={() => {
                                book()
                            }}
                        >Đặt ngay</div>
                    </div>

                </div>
            </div>
        </>
    )
}


function MenuItem({ item }) {
    return (
        <>
            <div className="col-7" style={{ padding: 15, backgroundColor: '#ececec' }}>
                <div style={{ display: 'flex' }}>
                    <div>
                        <img src={`${BASE_URL_DOWNLOAD}${item?.image}`} style={{ width: '100%', height: 200, objectFit: 'contain' }} />
                    </div>
                    <div style={{ paddingLeft: 15 }}>
                        <label><svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                            <path fill="currentColor" d="M15.5,21L14,8H16.23L15.1,3.46L16.84,3L18.09,8H22L20.5,21H15.5M5,11H10A3,3 0 0,1 13,14H2A3,3 0 0,1 5,11M13,18A3,3 0 0,1 10,21H5A3,3 0 0,1 2,18H13M3,15H8L9.5,16.5L11,15H12A1,1 0 0,1 13,16A1,1 0 0,1 12,17H3A1,1 0 0,1 2,16A1,1 0 0,1 3,15Z" />
                        </svg> {item?.name}</label>
                        <br />
                        <label><svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                            <path fill="currentColor" d="M3,6H21V18H3V6M12,9A3,3 0 0,1 15,12A3,3 0 0,1 12,15A3,3 0 0,1 9,12A3,3 0 0,1 12,9M7,8A2,2 0 0,1 5,10V14A2,2 0 0,1 7,16H17A2,2 0 0,1 19,14V10A2,2 0 0,1 17,8H7Z" />
                        </svg> {item?.price} {item?.currency}</label>
                    </div>
                </div>
                <div style={{ paddingTop: 15, fontStyle: 'italic', fontSize: 18 }}>
                    <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                        <path fill="currentColor" d="M22 13H14V11H22V13M22 7H14V9H22V7M14 17H22V15H14V17M12 9V15C12 16.1 11.1 17 10 17H4C2.9 17 2 16.1 2 15V9C2 7.9 2.9 7 4 7H10C11.1 7 12 7.9 12 9M10.5 15L8.3 12L6.5 14.3L5.3 12.8L3.5 15H10.5Z" />
                    </svg>  {item?.description}
                </div>

            </div>
            <div style={{ height: 0.5, width: '50%', backgroundColor: '#ececec', marginTop: 10 }} />
        </>
    )
}