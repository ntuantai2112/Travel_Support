import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import item7 from '../../image/slick/image 9.jpg';
import item3 from '../../image/unique/item3.png';
import item4 from '../../image/unique/item4.png';
import item5 from '../../image/unique/item5.png';
import item6 from '../../image/unique/item6.png';
import item1 from '../../image/unique/vinhhalong.png';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';
import '../../style/UniquePlace.scss';

export default function UniquePlace() {
    const [items, setItems] = useState();
    useEffect(() => {
        search()
    }, [])
    let search = async () => {
        let path = `/customer/post/search/all`;
        let resp = await API.anonymousJSONGET(path);
        if (resp.ok) {
            let response = await resp.json();
            let arr = [];
            for (let i = 5; i < 9; i++) {
                arr.push(response?.data[i])
            }
           
            setItems(arr)
        }
    }

    let history = useHistory()
    return (
        <>
            <div className="wrapper__unique-place">
                <div className="container container__unique-place">
                    <h3 className="title">
                        Những địa điểm độc đáo
                    </h3>
                    <div className="row row__unique-place">

                        {items?.map((data, idx) => {
                            return (
                               
                                    <div className="col-3 col__item" key={idx}>
                                        <div className="wrapper__item">
                                            <div className="image__blog" onClick={() => history.push({
                                                pathname: '/post-detail',
                                                state: data
                                            })}>
                                                <img alt="" src={`${BASE_URL_DOWNLOAD}${data?.image}`} />
                                            </div>
                                            <div className="content__blog">
                                                <h4 className="title">
                                                    {data?.title}
                                                </h4>
                                            </div>
                                        </div>
                                    </div>
                               
                            )
                        })}


                    </div>
                </div>
            </div>
        </>
    )
}