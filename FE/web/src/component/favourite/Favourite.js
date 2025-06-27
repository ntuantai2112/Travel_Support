import React, { useEffect, useState } from 'react';
import Footer from '../../component/share/Footer';
import Header from '../../component/share/Header';
import API from '../../lib/API';
import '../../style/search.scss';
import Search from './Search';

export default function Favourite() {
 
    const [data, setData] = useState()
    
    
  
    useEffect(() => {
      
        search()
    }, []);
    let search = async () => {
        let path = `/favorite/list`;
        let resp = await API.authorizedJSONFavouriteGET(path);
        if (resp.ok) {
            let response = await resp.json();
            setData(response?.data)
        }
    }
    return (
        <>

            <div className="wrapper__search-page container">
                <Header />
                <div className="row row__search-page">
                
                    <div className="col-12 col__main">
                        <Search data={data} />
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}