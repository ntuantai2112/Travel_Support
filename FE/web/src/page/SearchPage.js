import React from 'react';
import '../style/search.scss';
import Header from '../component/share/Header';
import Footer from '../component/share/Footer';
import Search from '../component/search/Search';
import SearchPlace from '../component/share/SearchPlace'
import ExploreVN from '../component/share/ExploreVN';

export default function SearchPage() {
    return (
        <>
            
            <div className="wrapper__search-page container">
                <Header />
                <div className="row row__search-page">
                    <div className="col-6 col__search">
                        <SearchPlace />
                    </div>
                    <div className="col-12 col__main">
                        <Search />
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