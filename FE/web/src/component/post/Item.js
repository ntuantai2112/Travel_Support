import React from 'react';
import { useHistory } from 'react-router';
import { BASE_URL_DOWNLOAD } from '../../lib/API';
export default function Item({ data }) {
    let history = useHistory()
    return (
        <>
            <li className="post__item" onClick={() => history.push({
                pathname: '/post-detail',
                state: data
            })}>
                <div className="wrapper__item">
                    <img alt="" src={`${BASE_URL_DOWNLOAD}${data?.image}`} />
                    <div className="describe__content">
                        <div className="wrapper__content">
                            <div className="background">

                            </div>
                            <p className="content">
                                {data?.title}
                            </p>
                        </div>
                    </div>
                </div>
            </li>


        </>
    )
}