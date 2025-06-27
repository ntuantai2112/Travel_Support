import React from 'react';
import '../../../style/booking/share/bill-booking-room.scss';
import '../../../style/booking/share/bill-booking-room-result.scss';
import '../../../style/booking/share/bill-booking-ticket-result.scss';
import moment from 'moment';

export default function BillBookingTicketResult({ data }) {
    return (
        <>
            <div className="wrapper__bill-booking-room container" id="bill__ticket--result">
                <div className="main__bill">
                    <h5 className="title">Chi tiết hóa đơn</h5>
                    <div className="top__content">
                        <p className="time__booking">
                            <span>Ngày đặt</span>
                            <span className="time">{moment(data?.travelDate, "YYYYMMDD").format("DD-MM-YYYY")}</span>
                        </p>
                        <p className="payment__method">
                            <span>Phương thức thanh toán</span>
                            <span className="method">Tiền mặt</span>
                        </p>
                    </div>

                    <div className="middle__content">
                        <p className="price-per-adult">
                            <span>Giá mỗi vé người lớn</span>
                            <span>{data?.activitiesInfoDTO?.adultTicketPrice} &nbsp; VNĐ</span>
                        </p>
                        <p className="price-per-children">
                            <span>Giá mỗi vé trẻ em</span>
                            <span>{data?.activitiesInfoDTO?.childTicketPrice} &nbsp; VNĐ</span>
                        </p>
                        <p className="amount__price-adult">
                            <span>Vé người lớn x  <span>{data?.numberTicketAdult}</span>     </span>
                            <span>{Number(data?.numberTicketAdult) * Number(data?.activitiesInfoDTO?.adultTicketPrice)} &nbsp; VNĐ</span>
                        </p>
                        <p className="tax__include">
                            <span>Bao gồm thuế</span>
                            <span></span>
                        </p>
                        <p className="amount__price-adult">
                            <span>Vé trẻ em x  <span>{data?.numberTicketChild}</span>     </span>
                            <span>{Number(data?.numberTicketChild) * Number(data?.activitiesInfoDTO?.childTicketPrice)} &nbsp; VNĐ</span>
                        </p>
                        <p className="tax__include">
                            <span>Bao gồm thuế</span>
                            <span></span>
                        </p>
                    </div>
                    <div className="total__price">
                        <span>Tổng giá</span>
                        <span id="total">{data?.price} &nbsp; VNĐ</span>
                    </div>
                    {/* <div className="total__fee">
                        <span>Tổng phí</span>
                        <span id="fee">&#8363;3,702,428</span>
                    </div> */}
                </div>
                {/* <div className="sm__banner">
                    BANNER
                </div> */}
            </div>
        </>
    )
}