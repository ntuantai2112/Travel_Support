
import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';
import '../../style/post.scss';
import Item from './Item';

export default function Post() {
    let history = useHistory();
    let [data, setData] = useState();
    const [items, setItems] = useState();
    useEffect(() => {
        search()
    }, [])
    let search = async () => {
        let path = `/customer/post/search?page=0`;
        let resp = await API.anonymousJSONGET(path);
        if (resp.ok) {
            let response = await resp.json();
            setData(response?.data);
           
            let arr = [];
            for (let i = 1; i < 5; i++) {
                arr.push(response?.data[i])
            }
            setItems(arr)
        }
    }

   
    return (
        <>
            <div className="wrapper__post">
                <div className="container container__post">
                    <div className="row row__post">
                        <div className="col-6 col__post col__post-hero" onClick={() => history.push({
                            pathname:'/post-detail',
                            state: data?.[0]
                        })}>
                            <div className="post__hero">
                                <img alt="" src={`${BASE_URL_DOWNLOAD}${data?.[0]?.image}`} />
                                <div className="describe__content">
                                    <div className="wrapper__content">
                                        <div className="background">

                                        </div>
                                        <p className="content">
                                            {data?.[0]?.title}
                                        </p>
                                    </div>
                                </div>

                            </div>
                        </div>
                     
                        <div className="col-6 col__post col__post-item" >
                            <ul className="list__post-item">
                                {items?.map((item, index) => {
                                    return (
                                        <Item key={index} data={item} />
                                    )
                                })}

                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}