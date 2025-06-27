import React, { useEffect, useState } from 'react';
import '../../style/searchhotel.scss'
import '../../style/searchrestaurant.scss'
import img1 from '../../image/unique/item2.png'
import NavBottom from '../share/NavBottom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faHeart } from '@fortawesome/free-regular-svg-icons';
import { useHistory } from 'react-router-dom';
import RatingStar from '../share/RatingStar';
import { typeRestaurant } from '../../utils/amenities';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';
import Pagination from 'react-js-pagination';


export default function SearchRestaurant({ data, activePage, handleActivePage }) {

    return (
        <>
            <div className="wrapper__search container no-mg">
                <h3 className="title">
                    Nhà Hàng và điểm ăn uống
                </h3>
                

                {data?.restaurants?.length > 0 ?
                    data?.restaurants?.map((item, index) => {
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
    let typeRestaurantName = typeRestaurant.filter((item) => item.value == data?.restaurantType)?.[0]?.name;
    return (
        <>
            <div className="row item__row">
                <div className="col-5 col__image" onClick={() => history.push({
                    pathname: '/search-restaurant-detail',
                    state: slug
                })}>
                    <div className="wrapper">
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
                    <div className="row row__info">
                        <div className="status">
                            <p className="title__status">
                                Đang Mở Cửa
                                <RatingStar />
                                {/* <span className="count__comment">195 đánh giá</span> */}
                            </p>
                            <div className="info">
                                <p className="type__restaurant">
                                    {data?.description}
                                </p>
                                <p className="address">
                                    ĐỊA CHỈ:
                                    {data?.address}
                                </p>
                            </div>
                            <div className="menu" onClick={() => history.push({
                                pathname: '/search-restaurant-detail',
                                state: slug
                            })}>
                                Xem Thực Đơn
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}