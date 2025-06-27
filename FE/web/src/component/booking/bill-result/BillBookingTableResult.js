import React from 'react';
import '../../../style/booking/share/bill-booking-room.scss';
import '../../../style/booking/share/bill-booking-room-result.scss';
import '../../../style/booking/share/bill-booking-ticket-result.scss';
import '../../../style/booking/share/bill-booking-table-result.scss';

export default function BillBookingTableResult() {
    return (
        <>
            <div className="wrapper__bill-booking-room container" id="bill__table--result">
                <div className="main__bill">
                    
                    <div className="overlay">Thanh <br/> Toán <br/>tại<br/> nhà<br/> hàng</div>
                    
                    <h5 className="title">Chi tiết hóa đơn</h5>
                    <div className="top__content">
                        <p className="time__booking">
                            <span>Ngày đặt</span>
                            <span className="time">16-07-2021 02:24 (GMT + 0)</span>
                        </p>
                        <p className="payment__method">
                            <span>Phương thức thanh toán</span>
                            <span className="method">Tiền mặt</span>
                        </p>
                    </div>

                    <div className="middle__content">
                        <p className="price-per-adult">
                            <span>Giá mỗi vé người lớn</span>
                            <span> &#8363;1,851,214 </span>
                        </p>
                        <p className="price-per-children">
                            <span>Giá mỗi vé trẻ em</span>
                            <span>  &#8363;851,214</span>
                        </p>
                        <p className="amount__price-adult">
                            <span>Vé người lớn x  <span>2</span>     </span>
                            <span>3,702,428</span>
                        </p>
                        <p className="tax__include">
                            <span>Bao gồm thuế</span>
                            <span></span>
                        </p>
                        <p className="amount__price-adult">
                            <span>Vé trẻ em x  <span>0</span>     </span>
                            <span>&#8363;0</span>
                        </p>
                        <p className="tax__include">
                            <span>Bao gồm thuế</span>
                            <span></span>
                        </p>
                    </div>
                    <div className="total__price">
                        <span>Tổng giá</span>
                        <span id="total">&#8363;3,702,428</span>
                    </div>
                    <div className="total__fee">
                        <span>Tổng phí</span>
                        <span id="fee">&#8363;3,702,428</span>
                    </div>
                </div>
                <div className="sm__banner">
                    BANNER
                </div>
            </div>
        </>
    )
}