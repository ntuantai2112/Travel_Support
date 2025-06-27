import React, { useEffect, useState } from 'react';
import Header from '../../component/share/Header';
import Footer from '../../component/share/Footer';
import BookingTicketResult from '../../component/booking/BookingTicketResult';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import BillBookingTicketResult from '../../component/booking/bill-result/BillBookingTicketResult';
import { useHistory, useLocation } from 'react-router-dom';
import API from '../../lib/API';

export default function BookingTicketResultPage() {
    let location = useLocation();
    console.log(location.state);
    const [data, setData] = useState();
    useEffect(() => {
        search()
    }, [location.state])
    let search = async () => {
        let path = `/activities/book-detail/${location.state}`;
        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();
            setData(response?.data)
        }
    }
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
                        <div className="right__content content" onClick={() => {
                            history.push('/search-entertainment')
                        }}
                            style={{ cursor: 'pointer' }}>
                            <span
                            >
                                <FontAwesomeIcon icon={faChevronLeft} id="icon__back" />
                            </span> Về trang đặt chỗ
                        </div>
                    </div>
                    <div className="col-8 col__booking">
                        <BookingTicketResult data={data} />
                    </div>
                    <div className="col-4 col__bill">
                        <BillBookingTicketResult data={data} />
                    </div>
                </div>
            </div>
            <Footer />
        </>
    )
}