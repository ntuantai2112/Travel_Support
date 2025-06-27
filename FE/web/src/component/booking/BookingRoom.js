import React, { useState } from 'react';
import '../../style/booking/booking-room.scss';
import { faBed } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import moment from 'moment';
import API from '../../lib/API';
import { useHistory } from 'react-router-dom';
import { useForm } from "react-hook-form";


export default function BookingRoom({ hotelCode, packageId }) {
    const { register, handleSubmit, watch, formState: { errors }, reset } = useForm();

    const [contact, setContact] = useState({});
    const [data, setData] = useState();
    const handleChange = (e) => {
        let { value, name } = e.target;
        setContact({
            ...contact,
            [name]: value
        })
    }
    let history = useHistory();

    const [checkinDate, setCheckinDate] = useState(new Date())
    const [checkoutDate, setCheckoutDate] = useState(new Date());
    const token = localStorage.getItem("token");
    const roleId = localStorage.getItem("roleId")
    const bookActivity = async (form) => {
        if (token) {
            try {
                if (roleId == 5) {
                    if (new Date(checkoutDate).getTime() > new Date(checkinDate).getTime()) {
                        let path = 'http://localhost:8887/qldv/hotel/book';
                        let objReq = {
                            hotelCode: hotelCode,
                            packageId: packageId,
                            contact: form,
                            checkinDate: Number(moment(checkinDate).format("YYYYMMDD")),
                            checkoutDate: Number(moment(checkoutDate).format("YYYYMMDD"))
                        }
                        console.log(objReq)
                        let token = localStorage.getItem("token");

                        let resp = await API.authorizedJSONBOOKPost(path, objReq, token);
                        if (resp.ok) {
                            let response = await resp.json();
                            setData(response)
                            alert("Bạn đã book thành công!")
                            history.push({
                                pathname: '/booking-room-result',
                                state: response
                            })
                        } else {
                            alert("Vui lòng nhập đủ thông tin!")
                        }

                    } else {
                        alert("Ngày ở phải lớn hơn 1!")
                    }
                }else{
                    alert("Tài khoản này không phải tài khoản khách hàng, vui lòng tạo tài khoản user để có thể booking!")
                }
            } catch (error) {

            }
        } else {
            let message = window.confirm("Hãy đăng nhập ngay để trải nghiệm dịch vụ của chúng tôi");
            if (message) {
                history.push('/login')

            }

        }

    }
    return (
        <>
            <div className="wrapper__booking-room" >
                {/* <div className="sign__up--text">
                    Hãy <span className="sign__up"
                    onClick={() => {
                        history.push('/register')
                    }}
                    >Đăng Ký</span> ngay nếu bạn chưa có tài khoản tại Travel Support!
                </div> */}

                <form className="content__booking"
                    onSubmit={handleSubmit(bookActivity)}
                >
                    <div className="contact__info">
                        <h4 className="title">
                            Thông tin đặt vé
                        </h4>
                        <hr />
                        <div className="content">
                            <div className="name__info item">
                                <label for="name">Họ</label>
                                <input type="text"
                                    {...register("firstName", { required: true })}
                                />
                                {errors.firstName && <span style={{ color: 'red' }}>Trường này không được bỏ trống *</span>}


                            </div>
                            <div className="name__info item">
                                <label for="name">Tên</label>
                                <input type="text"
                                    {...register("lastName", { required: true })}
                                />
                                {errors.lastName && <span style={{ color: 'red' }}>Trường này không được bỏ trống *</span>}
                            </div>
                            <div className="phone-number__info item">
                                <label for="phone-number">Số điện thoại</label>

                                <input type="tel"
                                    {...register("phone", { required: true, pattern: /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/ })}
                                />
                                {errors.phone?.type == "required" && <span style={{ color: 'red' }}>Trường này không được bỏ trống *</span>}
                                {errors.phone?.type == "pattern" && <span style={{ color: 'red' }}>Số điện thoại chưa đúng định dạng *</span>}
                            </div>
                            <div className="mail__info">
                                <label for="phone-number">Địa chỉ Email</label>
                                <div id="bottom__content">
                                    <input type="email"
                                        {...register("email", { required: true })}
                                    />
                                    {errors.email && <span style={{ color: 'red' }}>Trường này không được bỏ trống *</span>}
                                    <span>Thông Tin Sẽ Được Gửi Qua Email</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <br />
                    <p></p>
                    <div className="contact__info" style={{ marginTop: 150, marginBottom: 100 }}>
                        <h4 className="title">
                            Thời gian
                        </h4>
                        <hr />
                        <div className="content">
                            <div className="name__info item">
                                <label for="name">Từ ngày</label>
                                <input type="date"
                                    value={moment(checkinDate).format("YYYY-MM-DD")}
                                    onChange={e => setCheckinDate(e.target.value)}
                                    min={moment(new Date()).format("YYYY-MM-DD")}
                                />
                            </div>
                            <div className="phone-number__info item">
                                <label for="phone-number">Đến ngày</label>
                                <input type="date"
                                    value={moment(checkoutDate).format("YYYY-MM-DD")}
                                    onChange={e => setCheckoutDate(e.target.value)}
                                    min={moment(checkinDate).format("YYYY-MM-DD")}
                                />
                            </div>

                        </div>
                    </div>
                    <br />
                    {/* <div className="bed__info">
                        <h4 className="title">
                            Thông tin giường ngủ
                        </h4>
                        <hr />
                        <p className='bed__info--text'>
                            <span><FontAwesomeIcon icon={faBed} id="bed__icon" /></span>
                            Một giường đơn và một ghế tình yêu
                        </p>
                    </div>
                    <div className="name__order--info">
                        <h4 className="title">
                            Tên Người đặt chỗ
                        </h4>
                        <hr />
                        <div className="name__info item">
                            <label for="name">Họ tên</label>
                            <input type="text" id="name" />
                        </div>
                        <hr />
                        <div className="more__request">
                            <label for="requested">Yêu cầu thêm <span>( tùy chọn )</span></label>
                            <form action="" id="form-checkbox">
                                <input type="checkbox" id="request1" name="request1" value="Smoke" />
                                <label for="request1"> Phòng không hút thuốc </label>
                                <br />
                                <input type="checkbox" id="vehicle2" name="vehicle2" value="Car" />
                                <label for="vehicle2"> Yêu cầu đặc biệt ( yêu cầu nhập chi tiết )</label>
                            </form>
                            <textarea type="text" id="requested" />
                        </div>
                    </div> */}
                    {/* <div className="payment__method">
                        <h4 className="title">
                            Hình thức thanh toán <span id="sm-text">( chọn một trong hai hình thức bên dưới )</span>
                        </h4>
                        <hr />
                        <form action="" id="form-checkbox__payment">
                            <div id="at__hotel">
                                <input type="checkbox" id="request1" name="request1" value="Smoke" />
                                <label for="request1"> Phòng không hút thuốc </label>
                                <ul>
                                    <li id="text">Thanh toán tại khách sạn sẽ là thanh toán trực tiếp bằng tiền mặt tại khách sạn.</li>
                                </ul>
                            </div>
                            <div id="by__banking">
                                <input type="checkbox" id="vehicle2" name="vehicle2" value="Car" />
                                <label for="vehicle2"> Yêu cầu đặc biệt ( yêu cầu nhập chi tiết )</label>
                                <ul>
                                    <li id="text">
                                        Thanh toán chuyển khoản trước vui lòng chuyển khoản như sau : <br />
                                        Số tài khoản: 12512516124 <br />
                                        Chủ tài khoản: Phạm Hưng <br />
                                        Viecombank chi nhánh VVV <br />
                                        Nội dung: Thue Phong Khach San
                                    </li>
                                </ul>
                            </div>
                        </form>
                    </div> */}
                    <div className="rules">
                        <h4 className="title">
                            Điều khoản và điều kiện đặt chỗ
                        </h4>
                        <hr />
                        <p className="text__rules">
                            Bằng cách hoàn thành việc đặt chỗ này, bạn cần đồng ý với <span id="rules__booking-ticket">Các điều kiện đặt vé</span>, <span id="terms__condition">Điều khoản & điều kiện</span> và <span id="privacy__policy ">Chính sách quyền riêng tư</span>.
                        </p>
                    </div>
                    <div className="wrapper__booking--btn">
                        <button className="booking__btn" type="submit">


                            ĐẶT NGAY



                        </button>
                    </div>

                </form>

            </div>
        </>
    )
}