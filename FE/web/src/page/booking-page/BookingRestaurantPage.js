import React from 'react';
import BookingRestaurant from '../../component/booking/BookingRestaurant';
import BookingTicket from '../../component/booking/BookingTicket';
import TopNav from '../../component/booking/share/TopNav';
import Footer from '../../component/share/Footer';
import Header from '../../component/share/Header';

export default function BookingRestaurantPage() {
    
    return (
        <>
            <div className="wrapper container" id="booking__ticket--page">
                <Header />
                <div className="row row__main">
                    <div className="col-12 col__top--nav">
                        <TopNav />
                    </div>
                    <div className="col-12 col__booking">
                        <BookingRestaurant />
                    </div>
                    {/* <div className="col-4 col__bill">
                        <ColBanner />
                    </div> */}
                </div>
            </div>
            <Footer />
        </>
    )
}
