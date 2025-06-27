import React from 'react';
import BookingTable from '../../component/booking/BookingTable';
import Header from '../../component/share/Header';
import Footer from '../../component/share/Footer';
import ColBanner from '../../component/booking/share/ColBanner';
import TopNav from '../../component/booking/share/TopNav';


export default function BookingTablePage() {
    return (
        <>
            <div className="wrapper container" id="booking__table--page">
                <Header />
                <div className="row row__main">
                    <div className="col-12 col__top--nav">
                        <TopNav />
                    </div>
                    <div className="col-8 col__booking">
                        <BookingTable />
                    </div>
                    <div className="col-4 col__bill">
                        <ColBanner />
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}
