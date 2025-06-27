import React, { useEffect, useState } from 'react';
import '../../style/search.scss';
import Header from '../../component/share/Header';
import Footer from '../../component/share/Footer';
import Search from './Search';
import SearchPlace from './SearchPlace'
import ExploreVN from '../share/ExploreVN';
import { useLocation } from 'react-router-dom';
import API from '../../lib/API';

export default function HomeSearch() {
    let location = useLocation();
    const [data, setData] = useState()

    let handleText = (value) => {
        setText(value)
    }
    const [text, setText] = useState("");
    useEffect(() => {
        search(location.state)
    }, [location.state]);
    
    let search = async (value) => {
        let path = `/search/customer/search-filter?title=${value}`;
        console.log(path)
        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();
            console.log(response)
            setData(response)
        }
    }
    return (
        <>

            <div className="wrapper__search-page container">
                <Header />
                <div className="row row__search-page">
                    <div className="col-6 col__search">
                        <SearchPlace search={search}  />
                    </div>
                    <div className="col-12 col__main">
                        <Search data={data} />
                    </div>
                </div>
                <div className="row row__explore">
                    <div className="col-12 col__explore">
                        <ExploreVN />
                    </div>
                </div>

            </div>
            <Footer />
        </>
    )
}