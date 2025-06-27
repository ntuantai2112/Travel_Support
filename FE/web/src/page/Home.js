import React from 'react';
import { useLocation } from 'react-router-dom';
import HomeSlick from '../component/home/HomeSlick';
import SearchHome from '../component/home/SearchHome';
import ExploreVN from '../component/share/ExploreVN';
import Footer from '../component/share/Footer';
import Header from '../component/share/Header';
import UniquePlace from '../component/share/UniquePlace';
import '../style/home.scss';
import '../style/style.scss';

export default function Home({ isLogin }) {
    let location = useLocation();
    let tokenParams = location.state;
    return (
        <>

            <div className="wrapper__home container">
                <Header isLogin={isLogin} tokenParams={tokenParams} />
                <div className="row row__home">
                    <div className="col-6 col__home--left">
                        <div className="title__search">tìm kiếm nơi lưu trú, ăn uống, giải trí và nhiều hơn nữa...</div>
                        <SearchHome />
                    </div>
                    <div className="col-6 col__home--right">
                        <HomeSlick />
                    </div>
                </div>
                <div className="row row__explore">
                    <div className="col-12 col__explore">
                        <ExploreVN />
                    </div>
                </div>
                <div className="row row__unique">
                    <div className="col-12 col__unique">
                        <UniquePlace />
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}