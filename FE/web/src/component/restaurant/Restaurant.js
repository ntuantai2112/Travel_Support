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
export default function Restaurant() {
    const [text, setText] = useState("");
    
    const [type, setType] = useState("");
    const [activePage, setActivePage] = useState(1);
    let handleText = (value) => {
        setText(value)
    }
    // let handleStar = (value) => {
    //     setStar(value)
    // }
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
        let path = `/search/restaurant/search-filter?q=${text}&type=${type}&page=${activePage}&perPage=${LIMITS}`;
        console.log(path)
        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();

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
            for (let i = 0; i < response?.data?.restaurant?.length; i++) {
                resps.push(response?.data?.restaurant[i]?.slug)
            }

            setFavourite(resps)
        }
    }

    return (
        <>
            <div className="wrapper__search search__restaurant container">
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
                            // star={star} 
                            type={type}
                            // handleStar={handleStar} 
                            handleType={handleType}

                        />
                    </div>
                    <div className="col-9 col__main">
                        <div className="wrapper__search container no-mg">
                           
                            {data?.restaurants?.length > 0 ?
                                data?.restaurants?.map((item, index) => {
                                    return (
                                        <Item key={index} image={item?.image} slug={item?.slug}
                                            favourite={favourite}
                                            searchFavourite={searchFavourite}
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