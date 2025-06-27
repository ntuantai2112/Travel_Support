import React from 'react';
import { useHistory } from 'react-router-dom';
import logo from '../../image/logot.png';
import '../../style/style.scss';
import Profile from './Profile';

export default function Header({ isLogin, tokenParams }) {
    let history = useHistory();
    const token = localStorage.getItem("token");
    const pageHeader = localStorage.getItem("pageHeader")
    let stylePage = (value) => {
        if (pageHeader == value) {
            return `#2D749A`
        } else {
            return null
        }
    }
    return (
        <div style={{ width: '100%', height: 80 }}>
            <header>
                <div className="wrapper__header">
                    <div className="container__header container">
                        <div className="row">
                            <div className="col-2 col-logo col-nav">
                                <div className="wrapper__logo" onClick={() => {
                                    history.push('/')
                                    localStorage.removeItem("pageHeader")
                                }} style={{ cursor: 'pointer' }}>
                                    <img alt="" src={logo} />
                                </div>
                            </div>
                            <div className="col-7 col-menu col-nav">
                                <ul>
                                    <li
                                        style={{ backgroundColor: stylePage("hotel") }}
                                        className="hotel" onClick={() => {
                                            history.push('/search-hotel')
                                            localStorage.setItem("pageHeader", "hotel")
                                        }}>Khách Sạn</li>
                                    <li
                                        style={{ backgroundColor: stylePage("restaurant") }}
                                        className="restaurant" onClick={() => {
                                            history.push('/search-restaurant')
                                            localStorage.setItem("pageHeader", "restaurant")
                                        }}>Nhà Hàng</li>
                                    <li
                                        style={{ backgroundColor: stylePage("entainment") }}
                                        className="entertainment" onClick={() => {
                                            history.push('/search-entertainment')
                                            localStorage.setItem("pageHeader", "entertainment")
                                        }}>Khu Vui Chơi</li>
                                    <li className="post"
                                        onClick={() => {
                                            history.push('/post')
                                            localStorage.setItem("pageHeader", "post")
                                        }}
                                    >Bài Viết</li>
                                    <li className="favourite"
                                        style={{ backgroundColor: stylePage("favourite") }}
                                        onClick={() => {
                                            history.push('/favourite')
                                            localStorage.setItem("pageHeader", "favourite")
                                        }}
                                    >Yêu Thích</li>
                                </ul>
                            </div>
                            {!token ? <div className="col-3 col-account col-nav">
                                <div className="account__wrapper" onClick={() => history.push('/login')}>
                                    <span>Đăng Nhập</span>
                                </div>
                                {/* `${BASE_URL_DOWNLOAD}${user?.image}` */}
                            </div> : <Profile isLogin={isLogin} tokenParams={tokenParams} />}
                        </div>
                    </div>
                </div>
            </header>
        </div>
    )
}