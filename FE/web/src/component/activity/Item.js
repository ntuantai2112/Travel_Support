import { faHeart } from '@fortawesome/free-regular-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';
import '../../style/searchentertainment.scss';
import '../../style/searchhotel.scss';
import '../../style/searchrestaurant.scss';
import { typeActivity } from '../../utils/amenities';


export default function Item({ slug, image, favourite, searchFavourite, type }) {
    let history = useHistory();
    const [data, setData] = useState();
    let typeActivityName = typeActivity.filter((item) => item.value == type)?.[0]?.name;

    let fetchData = async () => {
        let path = `/activities/${slug}`;
        let resp = await API.anonymousJSONGETHOTEL(path);
        console.log(path)
        if (resp.ok) {
            let response = await resp.json();
            setData(response?.data)
        }
    }
    useEffect(() => {
        fetchData()
    }, []);
    useEffect(() => {
        fetchData()
    }, [slug]);
    let token = localStorage.getItem("token")
    let handleFavourite = async () => {
        if (token) {
            if (favourite?.includes(slug)) {
                let path = `/favorite/delete?slug=${slug}`;
                let resp = await API.authorizedJSONFavouriteGET(path);
                if (resp.ok) {
                    searchFavourite()
                    fetchData()
                }
            } else {
                let path = `/favorite/add`;
                let objReq = {
                    slug: slug,
                    image: image,
                    title: data?.title,
                    type: "ACTIVITIES"
                };
                let resp = await API.authorizedJSONFavouritePost(path, objReq);
                if (resp.ok) {
                    searchFavourite()
                    fetchData()
                }
            }
        } else {
            let message = window.confirm("Hãy đăng nhập ngay để trải nghiệm dịch vụ của chúng tôi");
            if (message) {
                history.push('/login')

            }
        }
    }

    console.log(data)
    return (
        <>
            <div className="row item__row">
                <div className="col-5 col__image" >
                    <div className="wrapper">
                        <img src={`${BASE_URL_DOWNLOAD}${image}`}
                        />
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
                    <div className="row row__info">
                        <div className="status">
                            {/* <p className="title__status">
                                Đang Mở Cửa
                                <RatingStar />
                                <span className="count__comment">195 đánh giá</span>
                            </p> */}
                            <div className="info">
                                <p className="address">
                                    Địa chỉ:   &nbsp;
                                    {data?.address}
                                </p>

                            </div>
                            <p className="address">
                                Địa điểm:
                                &nbsp; {typeActivityName}
                            </p>
                            <div className="menu"
                                onClick={() => history.push({
                                    pathname: '/search-entertainment-detail',
                                    state: slug
                                })}
                                style={{ cursor: 'pointer' }}
                            >
                                Xem chi tiết
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </>
    )
}