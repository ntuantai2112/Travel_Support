import React, { useEffect, useState } from 'react';
import SearchRestaurant from '../component/search/SearchRestaurant';
import ExploreVN from '../component/share/ExploreVN';
import FilterHotel from '../component/share/FilterHotel';
import Footer from '../component/share/Footer';
import Header from '../component/share/Header';
import SearchPlace from '../component/share/SearchPlace';
import API from '../lib/API';
import '../style/searchhotel.scss';
import '../style/style.scss';

export default function SearchRestaurantPage (){
    const [text, setText] = useState("");
    const [star, setStar] = useState();
    const [type, setType] = useState("");
    const [activePage, setActivePage] = useState(1);
    let handleText = (value) => {
        setText(value)
    }
    let handleStar = (value) => {
        setStar(value)
    }
    let handleType = (value) => {
        setType(value)
    }
    let handleActivePage = (value) => {
        setActivePage(value)
    }

    const [data, setData] = useState();

    const LIMITS = 3;
    useEffect(() => {
        search()
    }, [])
    useEffect(() => {
        search()
    }, [activePage])

    let search = async () => {
        let path = `/search/restaurant/search-filter?q=${text}&type=${type}&page=${activePage}&perPage=${LIMITS}`;

        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();

            setData(response?.data)
        }
    }
    return(
        <>
            <div className="wrapper__search search__restaurant container">
                <Header />
                <div className="row row__search--btn">
                    <div className="col-2"></div>
                    <div className="col-5 col__search--btn">
                        <SearchPlace search={search} handleActivePage={handleActivePage} />
                    </div>
                </div>
                <div className="row row__search-page">
                    <div className="col-2 col__filter">
                        <FilterHotel />
                    </div>
                    <div className="col-9 col__main">
                    <SearchRestaurant data={data} activePage={activePage} handleActivePage={handleActivePage} />
                    </div>
                    <div className="col-1">

                    </div>
                </div>
                <div className="row row__explore">
                    <ExploreVN />
                </div>
            </div>
            <Footer />
        </>
    )
}