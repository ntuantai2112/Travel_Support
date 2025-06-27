import React, { useEffect, useState } from 'react';
import ExploreVN from '../../component/share/ExploreVN';
import Filter from './Filter';
import Footer from '../../component/share/Footer';
import Header from '../../component/share/Header';
import Search from './Search';
import API from '../../lib/API';
import '../../style/searchhotel.scss';
import '../../style/style.scss';
import Item from './Item';
import Pagination from 'react-js-pagination';
export default function Activity() {
    const [text, setText] = useState("");

    const [type, setType] = useState("");
    const [activePage, setActivePage] = useState(1);
    const [duration, setDuration] = useState({
        from: "",
        to: ""
    })
    let handleText = (value) => {
        setText(value)
    }
    let handleDuration = (value) => {
        setDuration(value)
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
        searchFavourite()
    }, [])
    useEffect(() => {
        search()
    }, [activePage])

    let search = async () => {
        let path = `/search/activities/search-filter?q=${text}&type=${type}&durationFrom=${duration?.from}&durationTo=${duration?.to}&page=${activePage}&perPage=${LIMITS}`;
        console.log(path)
        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();
            console.log(response)
            setData(response?.data)
        }
    }
    let [favourite, setFavourite] = useState([])

    let searchFavourite = async () => {
        let path = '/favorite/list';
        let resp = await API.authorizedJSONFavouriteGET(path);
        if (resp.ok) {
            let resps = [];
            let response = await resp.json();
            for (let i = 0; i < response?.data?.activities?.length; i++) {
                resps.push(response?.data?.activities[i]?.slug)
            }

            setFavourite(resps)
        }
    }
  
    return (
        <>
            <div className="wrapper__search search__entertainment  container">
                <Header
                />
                <div className="row row__search--btn">
                    <div className="col-2"></div>
                    <div className="col-5 col__search--btn">
                        <Search search={search} handleActivePage={handleActivePage} text={text} handleText={handleText} />
                    </div>
                </div>
                <div className="row row__search-page">
                    <div className="col-2 col__filter">
                        <Filter
                            duration={duration} type={type}
                            handleDuration={handleDuration} handleType={handleType}

                        />
                    </div>
                    <div className="col-9 col__main">
                        <div className="wrapper__search container no-mg">
                           
                            {data?.activities?.length > 0 ?
                                data?.activities?.map((item, index) => {
                                    return (
                                        <Item
                                            favourite={favourite} searchFavourite={searchFavourite}
                                            key={index} image={item?.image} slug={item?.slug} 
                                            type={item?.type}
                                            />
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