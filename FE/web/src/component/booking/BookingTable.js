import React from 'react';
import '../../style/booking/booking-table.scss';

export default function BookingTable() {
    return (
        <>
            <div className="wrapper__booking-table">
                <div className="sign__up--text">
                    Hãy <span className="sign__up">Đăng Ký</span> ngay nếu bạn chưa có tài khoản tại Travel Support!
                </div>
                <div className="content__booking">
                    <div className="info__booking--ticket">
                        <h4 className="title">
                            Thông tin đặt bàn
                        </h4>
                        <hr />
                        <div className="row content">
                            <div className="col-12 time">
                                <div className="date item">
                                    <label for="booking__date">Ngày</label>
                                    <input id="booking__date" type="date" />
                                </div>
                                <div className="hour item">
                                    <label for="booking__hour">Giờ</label>
                                    <input id="booking__hour" type="time" />
                                </div>
                                <div className="amount__people item">
                                    <label for="amount">Số người</label>
                                    <input id="amount" type="number" min="0" />
                                </div>
                            </div>
                            {/* <div className="col-4 amount__ticket">
                                <div className="adult__amount item">
                                    <label for="input__adult">Số vé người lớn</label>
                                    <input id="input__adult" type="number" min="0" />
                                </div>
                                <div className="children__amount item">
                                    <label for="input__children">Số vé trẻ em</label>
                                    <input id="input__children" type="number" min="0" />
                                </div>
                            </div>
                            <div className="col-4 attention">
                                <p>
                                    Chú ý:
                                    <br />
                                    Vé trẻ em chỉ áp dụng cho trẻ dưới 1m3
                                </p>
                            </div> */}
                        </div>
                    </div>
                    <div className="contact__info">
                        <h4 className="title">
                            Thông tin đặt vé
                        </h4>
                        <hr />
                        <div className="content">
                            <div className="name__info item">
                                <label for="name">Họ tên</label>
                                <input type="text" id="name" />
                            </div>
                            <div className="phone-number__info item">
                                <label for="phone-number">Số điện thoại</label>
                                <input type="tel" id="phone-number" />
                            </div>
                        </div>
                        <hr />
                        <div className="more__request">
                            <label for="requested">Yêu cầu thêm <span>( tùy chọn )</span></label>
                            <textarea type="text" id="requested" />
                        </div>
                    </div>
                    <div className="rules">
                        <h4 className="title">
                            Điều khoản và điều kiện đặt vé
                        </h4>
                        <hr />
                        <p className="text__rules">
                            Bằng cách hoàn thành việc đặt chỗ này, bạn cần đồng ý với <span id="rules__booking-ticket">Các điều kiện đặt vé</span>, <span id="terms__condition">Điều khoản & điều kiện</span> và <span id="privacy__policy ">Chính sách quyền riêng tư</span>.
                        </p>
                    </div>
                    <div className="wrapper__booking--btn">
                        <div className="booking__btn">
                            ĐẶT NGAY
                        </div>
                    </div>

                </div>
            </div>
        </>
    )
}