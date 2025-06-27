import { faHeart } from '@fortawesome/free-regular-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';
import '../../style/searchentertainment.scss';
import '../../style/searchhotel.scss';
import '../../style/searchrestaurant.scss';
import { typeActivity } from '../../utils/amenities';


export default function Item({ slug, image }) {
    let history = useHistory();
    const [data, setData] = useState();
    let typeActivityName = typeActivity.filter((item) => item.value == data?.type)?.[0]?.name;
  
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
    let changeFavourite = async () => {
        if (data?.favorite) {
            let path = `/favorite/delete?slug=${slug}`;
            let resp = await API.authorizedJSONFavouriteGET(path);
            if (resp.ok) {
                fetchData()
            }
        } else {
            let path = '/favorite/add';
            let objReq = {
                slug: slug,
                image: image,
                title: data?.title,
                type: "ACTIVITIES"
            };
            let resp = await API.authorizedJSONFavouritePost(path, objReq);
            if (resp.ok) {
                let response = await resp.json();
                console.log("b",response)
                fetchData();

            }
        }

    }
   
    return (
        <>
            <div className="row item__row">
                <div className="col-5 col__image" >
                    <div className="wrapper">
                        <img src={`${BASE_URL_DOWNLOAD}${image}`}
                        />
                        <span className="wrapper__icon" style={{cursor:'pointer',zIndex:99}}
                        onClick={() => changeFavourite()}
                        >
                            {data?.favorite ?
                                <svg style={{ width: 24, height: 24 }} color="red" viewBox="0 0 24 24">
                                    <path fill="currentColor" d="M12,21.35L10.55,20.03C5.4,15.36 2,12.27 2,8.5C2,5.41 4.42,3 7.5,3C9.24,3 10.91,3.81 12,5.08C13.09,3.81 14.76,3 16.5,3C19.58,3 22,5.41 22,8.5C22,12.27 18.6,15.36 13.45,20.03L12,21.35Z" />
                                </svg> :
                                <FontAwesomeIcon className="icon__heart" icon={faHeart} color="red" />

                            }
                        </span>

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
                                <p className="type__restaurant">
                                    ĐỊA CHỈ:
                                    {data?.address}
                                </p>
                                <p className="address">
                                    ĐỊA ĐIỂM:
                                    {typeActivityName}
                                </p>
                            </div>
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