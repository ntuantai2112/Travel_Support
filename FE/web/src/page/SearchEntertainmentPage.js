import React from 'react';
import SearchEntertainment from '../component/search/SearchEntertainment';
import ExploreVN from '../component/share/ExploreVN';
import FilterHotel from '../component/share/FilterHotel';
import Footer from '../component/share/Footer';
import Header from '../component/share/Header';
import SearchPlace from '../component/share/SearchPlace';
import '../style/searchhotel.scss';
import '../style/style.scss';


export default function SearchEntertainmentPage() {
    return (
        <>
            <div className="wrapper__search search__entertainment  container">
                <Header />
                <div className="row row__search--btn">
                    <div className="col-2"></div>
                    <div className="col-5 col__search--btn">
                        <SearchPlace />
                    </div>
                </div>
                <div className="row row__search-page">
                    <div className="col-2 col__filter">
                        {/* <FilterHotel /> */}
                    </div>
                    <div className="col-9 col__main">
                        <SearchEntertainment />
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