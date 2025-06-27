import React from 'react';
import Header from '../../component/share/Header';
import Footer from '../../component/share/Footer';
import BookingRoomResult from '../../component/booking/BookingRoomResult';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useHistory, useLocation } from 'react-router-dom';
import BillBookingRoomResult from '../../component/booking/bill-result/BillBookingRoomResult';

export default function BookingRoomResultPage() {
    let location = useLocation();

    console.log(location?.state?.data);
    let history = useHistory()
    return (
        <>
            <div className="wrapper container" id="booking__room--result">
                <Header />
                <div className="row row__main">
                    <div className="alert__success">
                        <div className="left__content content">
                            Cảm ơn, yêu cầu đặt chỗ của bạn đã thành công!
                        </div>
                        <div className="right__content content"
                            onClick={() => {
                                history.push('/search-hotel')
                            }}
                        >
                            <span

                            >
                                <FontAwesomeIcon icon={faChevronLeft} id="icon__back" />
                            </span> Về trang đặt chỗ
                        </div>
                    </div>
                    <div className="col-8 col__booking">
                        <BookingRoomResult data={location?.state?.data} />
                    </div>
                    <div className="col-4 col__bill">
                        <BillBookingRoomResult
                            data={location?.state?.data}
                        />
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}