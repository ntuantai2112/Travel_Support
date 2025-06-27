import React, { useEffect, useState } from 'react';
import API from '../../lib/API';
import '../../style/searchentertainment.scss';
import '../../style/searchhotel.scss';
import '../../style/searchrestaurant.scss';
import NavBottom from '../share/NavBottom';
import Item from './Item';

export default function SearchEntertainment() {

    const [data, setData] = useState();
    useEffect(() => {
        search()
    }, [])
    let search = async () => {
        let path = `/search/activities/search-filter`;

        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();

            setData(response?.data?.activities)
        }
    }
    return (
        <>
            <div className="wrapper__search container">
                <h3 className="title">
                    Các địa điểm vui chơi, tham quan
                </h3>
                {data?.map((item, index) => {
                    return (
                        <Item key={index} data={item} slug={item?.slug} image={item?.image} />
                    )
                })}


                {/* <NavBottom /> */}
            </div>
        </>
    )
}
