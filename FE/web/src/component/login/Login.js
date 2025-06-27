import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import logo from '../../image/login/logo-travelSupport.png';
import API from '../../lib/API';
import '../../style/login.scss';

export default function Login({ handleLogin }) {
    let [account, setAccount] = useState();
    let history = useHistory();
    const [message, setMessage] = useState();

    let login = async () => {

        let path = "/login";
        let resp = await API.anonymousJSONPost(path, account);
        if (resp.ok) {
            let response = await resp.json();
            localStorage.setItem("token", response?.accessToken);
            if (response?.roleId == 1) {
                localStorage.setItem("roleId", response?.roleId)
                handleLogin()
                localStorage.setItem("page", "account")
                localStorage.setItem("pageHeader","")
                history.push('/admin')
                window.location.reload()
            } else if (response?.roleId == 2) {
                localStorage.setItem("roleId", response?.roleId)
                localStorage.setItem("pageHeader","")
                handleLogin()
                localStorage.setItem("page", "hotel")
                history.push('/admin')
                window.location.reload()
            } else if (response?.roleId == 3) {
                localStorage.setItem("roleId", response?.roleId)
                localStorage.setItem("pageHeader","")
                handleLogin()
                localStorage.setItem("page", "post")
                history.push('/admin')
                window.location.reload()
            } else if (response?.roleId == 4) {
                localStorage.setItem("roleId", response?.roleId)
                localStorage.setItem("pageHeader","")
                handleLogin()
                localStorage.setItem("page", "hotel")
                history.push('/admin')
                window.location.reload()
            } else {
                localStorage.setItem("roleId", response?.roleId)
                localStorage.getItem("page","booking-hotel")
                localStorage.setItem("pageHeader","")
                handleLogin()
                history.push({
                    pathname: '/',
                    state: response?.accessToken
                })
                window.location.reload()
            }




        } else {
            alert("Tài khoản đăng nhập chưa đúng!")
        }
    }


    useEffect(() => {
        localStorage.removeItem("token")
        localStorage.removeItem("page")
        localStorage.removeItem("roleId")

    }, [])
    return (
        <>
            <div className="main__login">
                <div className="dark-bg"></div>
                <div className="wrapper__sign">
                    {/* <img alt="" className="logo" src={logo} alt="" /> */}
                    <div className="logo" onClick={() => {
                                    history.push('/')
                                    localStorage.removeItem("pageHeader")
                                }} style={{ cursor: 'pointer' }}>
                                    <img alt="" src={logo} />
                                </div>
                    <div className="title">
                        <h1>Đăng Nhập <br />Để Khám Phá Điều Tuyệt Vời Nhất Của Travel Support</h1>
                    </div>
                    <div className="sign-with">
                        <div className="item">
                            <input
                                type="text"
                                placeholder="Email ..."
                                value={account?.username}
                                onChange={e => setAccount({
                                    ...account,
                                    username: e.target.value
                                })}
                            />
                        </div>
                        <div className="item">
                            <input
                                type="password"
                                placeholder="Mật khẩu ..."
                                value={account?.password}
                                onChange={e => setAccount({
                                    ...account,
                                    password: e.target.value
                                })}
                            />
                        </div>
                        {message && <p style={{ color: 'red' }}>{message}</p>}
                        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }} className="btn__login-hover">
                            <button onClick={() => {
                                login()
                            }}

                            >Đăng nhập</button>
                        </div>
                    </div>

                    <div className="sign-up" style={{ display: 'flex', justifyContent: 'space-evenly', marginTop: 15 }} >
                        <div className="sign-up__text"> <span onClick={() => {
                            history.push('/register')
                        }}>Đăng ký</span></div>
                        <div className="sign-up__text"> <span onClick={() => {
                            history.push('/forgot')
                        }}>Quên mật khẩu?</span></div>
                    </div>
                </div>
            </div>
        </>
    )
}