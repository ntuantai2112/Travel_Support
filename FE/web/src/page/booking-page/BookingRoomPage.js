import React from 'react';
import Header from '../../component/share/Header';
import Footer from '../../component/share/Footer';
import BookingRoom from '../../component/booking/BookingRoom';
import { useLocation } from 'react-router-dom';
import BillBookingRoom from '../../component/booking/bill-result/BillBookingRoom';
import TopNav from '../../component/booking/share/TopNav';

export default function BookingRoomPage() {
    let location = useLocation();
  
    // let { hotelCode, packageId } = location.state;
    return (
        <>
            <div className="wrapper container" id="booking__room">
                <Header />
                <div className="row row__main">
                    <div className="col-12 col__top--nav">
                        <TopNav />
                    </div>
                    <div className="col-10 col__booking">
                        <BookingRoom hotelCode={location?.state?.hotelCode} packageId={location?.state?.packageId} />
                    </div>
                    {/* <div className="col-4 col__bill">

                        <BillBookingRoom />
                    </div> */}
                </div>
            </div>
            <Footer />
        </>
    )
}