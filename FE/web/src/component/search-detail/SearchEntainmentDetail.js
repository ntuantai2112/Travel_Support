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

export default function SearchEntainmentDetail() {
    const [selectedImg, setSelectedImg] = useState(Images[0]);
    let location = useLocation();
    let history = useHistory();
    let slug = location.state;
    let [activities, setActivities] = useState();
    let [games, setGames] = useState();
    let search = async () => {
        let path = `/activities/${slug}`;
        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();
            setActivities(response?.data)
            setGames(response?.data?.games)
        }
    }
    console.log(activities?.code)
    useEffect(() => {
        search()
    }, [slug]);
    let token = localStorage.getItem("token");
    let book = async () => {
        if (token) {
            history.push({
                pathname: '/booking-ticket',
                state: activities?.code
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
                <div className="name__detail" style={{ display: 'flex' }}>
                    Khu vui chơi:  &nbsp;
                    <h3 className="title">{activities?.title}</h3>
                </div>
                <div className="row__gallery">
                    <Gallery images={activities?.images} />
                </div>
                <div className="row__introduce">
                    <h3 className="title">
                        Giới thiệu
                    </h3>
                    <div style={{ fontStyle: 'italic', fontSize: 20 }}>
                        {activities?.description}
                    </div>
                    <hr />
                    <h3 className="title">
                        Địa điểm
                    </h3>
                    <div>
                        {activities?.address}
                    </div>
                    <hr />
                    <h3 className="title">
                        Giá vé trẻ con
                    </h3>
                    <div>
                        {activities?.childPrice} VNĐ
                    </div>
                    <hr />
                    <h3 className="title">
                        Giá vé người lớn
                    </h3>
                    <div>
                        {activities?.adultPrice} VNĐ
                    </div>
                    <h3 className="title">
                        Thời gian chơi
                    </h3>
                    <div>
                        {activities?.duration} giờ
                    </div>
                    <hr />
                    <h3 className="title">
                        Trò chơi tại khu vui chơi
                    </h3>
                    <div className="container">
                        <div className="row">
                            {games?.map((item, index) => {
                                return (
                                    <GameItem item={item} key={index} />
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
                            onClick={book}
                        >Tham gia</div>
                    </div>

                </div>
            </div>
        </>
    )
}

function GameItem({ item }) {
    return (
        <>
            <div className="col-7" style={{ padding: 15, backgroundColor: '#ececec' }}>
                <div style={{ display: 'flex' }}>
                    <div>
                        <img src={`${BASE_URL_DOWNLOAD}${item?.image}`} style={{ width: '100%', height: 'auto', objectFit: 'contain', maxHeight: 200 }} />
                    </div>
                    <div style={{ paddingLeft: 15 }}>
                        <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                            <path fill="currentColor" d="M17.5,7A5.5,5.5 0 0,1 23,12.5A5.5,5.5 0 0,1 17.5,18C15.79,18 14.27,17.22 13.26,16H10.74C9.73,17.22 8.21,18 6.5,18A5.5,5.5 0 0,1 1,12.5A5.5,5.5 0 0,1 6.5,7H17.5M6.5,9A3.5,3.5 0 0,0 3,12.5A3.5,3.5 0 0,0 6.5,16C7.9,16 9.1,15.18 9.66,14H14.34C14.9,15.18 16.1,16 17.5,16A3.5,3.5 0 0,0 21,12.5A3.5,3.5 0 0,0 17.5,9H6.5M5.75,10.25H7.25V11.75H8.75V13.25H7.25V14.75H5.75V13.25H4.25V11.75H5.75V10.25M16.75,12.5A1,1 0 0,1 17.75,13.5A1,1 0 0,1 16.75,14.5A1,1 0 0,1 15.75,13.5A1,1 0 0,1 16.75,12.5M18.75,10.5A1,1 0 0,1 19.75,11.5A1,1 0 0,1 18.75,12.5A1,1 0 0,1 17.75,11.5A1,1 0 0,1 18.75,10.5Z" />
                        </svg>   <label>{item?.name}</label>
                    </div>
                </div>
                <div style={{ paddingTop: 15, fontStyle: 'italic' }}>
                    {item?.description}
                </div>
            </div>
            <div style={{ height: 0.5, width: '50%', backgroundColor: '#ececec', marginTop: 10 }} />
        </>
    )
}