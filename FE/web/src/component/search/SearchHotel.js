import { faHeart } from '@fortawesome/free-regular-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';
import '../../style/searchhotel.scss';
import NavBottom from '../share/NavBottom';
import Pagination from "react-js-pagination";

export default function SearchHotel({data,activePage,handleActivePage}) {
    
    return (
        <>
            <div className="wrapper__search container no-mg">
                <h3 className="title">
                    Khách sạn tại và điểm lưu trú
                </h3>
                {data?.hotels?.length > 0 ?
                    data?.hotels?.map((item, index) => {
                        return (
                            <Item key={index} data={item} slug={item?.slug} />
                        )
                    }) :
                    <>
                        Không có dữ liệu
                    </>
                }


                {data?.total > 0 ?
                    <div className="wrapper-paginate">
                        <Pagination
                            activePage={activePage}
                            itemsCountPerPage={3}
                            totalItemsCount={parseInt(data?.total)}
                            pageRangeDisplayed={3}
                            onChange={(item) => handleActivePage(item)}
                        />
                    </div> : <></>
                }
            </div>
        </>
    )
}

function Item({ data, slug }) {
    let history = useHistory();


    const currencyFormat = (num) => {
        return num?.replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.') + ' VNĐ'
    }

    return (
        <>
            <div className="row item__row" >
                <div className="col-5 col__image">
                    <div className="wrapper">
                        {/* <img alt="" src={img1} /> */}
                        <img src={`${BASE_URL_DOWNLOAD}${data?.image}`} />
                        <span className="wrapper__icon">
                            <FontAwesomeIcon className="icon__heart" icon={faHeart} />
                        </span>
                    </div>
                </div>
                <div className="col-7 col__content">
                    <h4 className="title__name">
                        {data?.title}
                    </h4>
                    <div className="row info">
                        <div className="col-6 col__price">
                            <h4 className="price title__name">
                                {currencyFormat(data?.minPrice?.toString())} 
                            </h4>
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