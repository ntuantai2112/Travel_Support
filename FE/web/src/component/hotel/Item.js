import { faHeart } from '@fortawesome/free-regular-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';
import '../../style/searchhotel.scss';
import NavBottom from '../share/NavBottom';
import Pagination from "react-js-pagination";

export default function Item({ image, slug, favourite, searchFavourite }) {
    let history = useHistory();
    const [data, setData] = useState();

    useEffect(() => {
        search()
    }, [])
    useEffect(() => {
        search()
        // searchToken()
    }, [slug])
    let search = async () => {
        
        let path = `/hotel/${slug}`;
        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();
            setData(response?.data)
        }
    }
   let token = localStorage.getItem("token")
    let handleFavourite = async () => {
        if (token) {
            if (favourite?.includes(slug)) {
                let path = `/favorite/delete?slug=${slug}`;
                let resp = await API.authorizedJSONFavouriteGET(path);
                if (resp.ok) {
                    searchFavourite()
                    search()
                }
            } else {
                let path = `/favorite/add`;
                let objReq = {
                    slug: slug,
                    image: image,
                    title: data?.title,
                    type: "HOTEL"
                };
                let resp = await API.authorizedJSONFavouritePost(path, objReq);
                if (resp.ok) {
                    searchFavourite()
                    search()
                }
            }
        } else {
            let message = window.confirm("Hãy đăng nhập ngay để trải nghiệm dịch vụ của chúng tôi");
            if (message) {
                history.push('/login')

            }
        }

    }

   
    const currencyFormat = (num) => {
        return num?.replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.') + ' VNĐ'
    }
    return (
        <>
            <div className="row item__row" >
                <div className="col-5 col__image">
                    <div className="wrapper">
                        {/* <img alt="" src={img1} /> */}
                        <img src={`${BASE_URL_DOWNLOAD}${image}`} />
                        {favourite?.includes(slug) ?
                            <span className="wrapper__icon"
                                onClick={() => handleFavourite()}
                                style={{ cursor: 'pointer' }}
                            >
                                <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24" color="red">
                                    <path fill="currentColor" d="M12,21.35L10.55,20.03C5.4,15.36 2,12.27 2,8.5C2,5.41 4.42,3 7.5,3C9.24,3 10.91,3.81 12,5.08C13.09,3.81 14.76,3 16.5,3C19.58,3 22,5.41 22,8.5C22,12.27 18.6,15.36 13.45,20.03L12,21.35Z" />
                                </svg>
                            </span>
                            :
                            <span className="wrapper__icon"
                                onClick={() => handleFavourite()}
                                style={{ cursor: 'pointer' }}
                            >
                                <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                                    <path fill="currentColor" d="M12.1,18.55L12,18.65L11.89,18.55C7.14,14.24 4,11.39 4,8.5C4,6.5 5.5,5 7.5,5C9.04,5 10.54,6 11.07,7.36H12.93C13.46,6 14.96,5 16.5,5C18.5,5 20,6.5 20,8.5C20,11.39 16.86,14.24 12.1,18.55M16.5,3C14.76,3 13.09,3.81 12,5.08C10.91,3.81 9.24,3 7.5,3C4.42,3 2,5.41 2,8.5C2,12.27 5.4,15.36 10.55,20.03L12,21.35L13.45,20.03C18.6,15.36 22,12.27 22,8.5C22,5.41 19.58,3 16.5,3Z" />
                                </svg>
                            </span>
                        }

                    </div>
                </div>
                <div className="col-7 col__content">
                    <h4 className="title__name">
                        {data?.title}
                    </h4>
                    <div className="row info">
                        <div className="col-6 col__price">
                            <div className="info">
                                <div className="address" style={{ color: '#333', display: 'flex', alignItems: 'center' }}>
                                    <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                                        <path fill="currentColor" d="M12,11.5A2.5,2.5 0 0,1 9.5,9A2.5,2.5 0 0,1 12,6.5A2.5,2.5 0 0,1 14.5,9A2.5,2.5 0 0,1 12,11.5M12,2A7,7 0 0,0 5,9C5,14.25 12,22 12,22C12,22 19,14.25 19,9A7,7 0 0,0 12,2Z" />
                                    </svg>
                                    ĐỊA CHỈ: &nbsp;
                                    {data?.address}
                                </div>
                            </div>
                            <div style={{ color: '#333', display: 'flex', alignItems: 'center' }}>
                                <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                                    <path fill="currentColor" d="M5,6H23V18H5V6M14,9A3,3 0 0,1 17,12A3,3 0 0,1 14,15A3,3 0 0,1 11,12A3,3 0 0,1 14,9M9,8A2,2 0 0,1 7,10V14A2,2 0 0,1 9,16H19A2,2 0 0,1 21,14V10A2,2 0 0,1 19,8H9M1,10H3V20H19V22H1V10Z" />
                                </svg>
                                <h4 className="price title__name" style={{ marginBottom: 0 }}>
                                    &nbsp;  {currencyFormat(data?.price?.toString())} 
                                </h4>
                            </div>
                            <br />
                            <div className="show-more" onClick={() => history.push({
                                pathname: '/search-hotel-detail',
                                state: slug
                            })}>Xem Chi Tiết</div>
                        </div>
                        <div className="col-6 col__utilities">
                            <ul className="list">
                                {data?.amenities?.map((item, idx) => {
                                    return (
                                        <li className="item" key={idx}>
                                            <img src={item?.icon} style={{ width: 24, height: 24, marginRight: 10 }} />
                                            <p>{item?.name}</p>
                                        </li>
                                    )
                                })}

                                {/* <li className="item">
                                    <FontAwesomeIcon className="icon" icon={faParking} />
                                    <p>Bãi đỗ xe miễn phí</p>
                                </li>
                                <li className="item">
                                    <FontAwesomeIcon className="icon" icon={faSwimmer} />
                                    <p>Bể bơi</p>
                                </li>
                                <li className="item">
                                    <FontAwesomeIcon className="icon" icon={faUtensils} />
                                    <p>Nhà hàng</p>
                                </li>
                                <li className="item">
                                    <FontAwesomeIcon className="icon" icon={faHandsWash} />
                                    <p> Thực hiện biện pháp an toàn</p>
                                </li> */}
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}