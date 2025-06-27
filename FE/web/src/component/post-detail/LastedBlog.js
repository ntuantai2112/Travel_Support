import React, { useEffect, useState } from 'react';
import '../../style/lasted-blog.scss';
import hero from '../../image/slick/image 8.png'
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';
import { useHistory, useLocation } from 'react-router-dom';

export default function LastedBlog() {
    let location = useLocation();
    let status = location.state
   
    const [data, setData] = useState([]);
    let [nums, setNums] = useState([]);
    let handleRandom = (post) => {
        let resp = [];
        for (let i = 1; i < 6; i++) {
            resp.push(Math.floor(Math.random() * post?.length))
        }
        setNums(resp)
    }
    useEffect(() => {
        search()
    }, [])
    useEffect(() => {
        search()
    }, [status])
    let search = async () => {
        let path = `/customer/post/search/all`;
        let resp = await API.anonymousJSONGET(path);
        if (resp.ok) {
            let response = await resp.json();
            setData(response?.data)
            handleRandom(response?.data)
        }
    }
    let history = useHistory()
    return (
        <>
            <div className="wrapper__lasted-blog">
                <h3 className="title">
                    Bài viết gần đây
                </h3>
                <div className="item__hero">

                    <div className="item__hero--top">
                        <img alt="" src={`${BASE_URL_DOWNLOAD}${data?.[nums?.[0]]?.image}`} />
                    </div>
                    <div className="item__hero--bottom">
                        <p className="content__hero"
                            onClick={() => history.push({
                                pathname: '/post-detail',
                                state: data?.[nums?.[0]]
                            })} style={{ cursor: 'pointer' }}
                        >
                            {data?.[nums?.[0]]?.title}
                        </p>
                    </div>
                </div>
                <div className="item__lasted-blog">
                    <div className="item">
                        <p
                            onClick={() => history.push({
                                pathname: '/post-detail',
                                state: data?.[nums?.[1]]
                            })} style={{ cursor: 'pointer' }}
                        >
                            {data?.[nums?.[1]]?.title}
                        </p>
                    </div>
                    <div className="item">
                        <p
                            onClick={() => history.push({
                                pathname: '/post-detail',
                                state: data?.[nums?.[2]]
                            })} style={{ cursor: 'pointer' }}
                        >
                            {data?.[nums?.[2]]?.title}
                        </p>
                    </div>
                    <div className="item">
                        <p
                            onClick={() => history.push({
                                pathname: '/post-detail',
                                state: data?.[nums?.[3]]
                            })} style={{ cursor: 'pointer' }}
                        >


                            {data?.[nums?.[3]]?.title}
                        </p>
                    </div>
                    <div className="item">
                        <p
                            onClick={() => history.push({
                                pathname: '/post-detail',
                                state: data?.[nums?.[4]]
                            })} style={{ cursor: 'pointer' }}
                        >
                            {data?.[nums?.[4]]?.title}
                        </p>
                    </div>
                </div>
            </div>
        </>
    )
}