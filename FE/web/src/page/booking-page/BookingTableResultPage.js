import React from 'react';
import Header from '../../component/share/Header';
import Footer from '../../component/share/Footer';
import BookingTableResult from '../../component/booking/BookingTableResult';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import BillBookingTableResult from '../../component/booking/bill-result/BillBookingTableResult';

export default function BookingTableResultPage() {
    return (
        <>
            <div className="wrapper container" id="booking__room--result">
                <Header />
                <div className="row row__main">
                    <div className="alert__success">
                        <div className="left__content content">
                            Cảm ơn, yêu cầu đặt chỗ của bạn đã thành công!
                        </div>
                        <div className="right__content content">
                            <span>
                                <FontAwesomeIcon icon={faChevronLeft} id="icon__back" />
                            </span> Về trang đặt chỗ
                        </div>
                    </div>
                    <div className="col-8 col__booking">
                        <BookingTableResult />
                    </div>
                    <div className="col-4 col__bill">
                        <BillBookingTableResult />
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}