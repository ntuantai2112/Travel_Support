import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';


export default function Profile({ isLogin, tokenParams }) {
    const token = localStorage.getItem("token");

    const [user, setUser] = useState();
    // useEffect(() => {
    //     search()
    // }, []);
    useEffect(() => {
        search()
    }, [isLogin]);
    useEffect(() => {
        search()
    }, []);
    const search = async () => {
        try {
            let path = '/member/account/profile';
            let resp = await API.authorizedJSONGETAVATAR(path, token);
            if (resp.ok) {
                let response = await resp.json();
                setUser(response)
            } else {
                let response = await resp.json();
                console.log(response)
            }
        } catch (error) {
            console.log(error)

        }
    }
    let history = useHistory();
    let [isMenu, setIsMenu] = useState(false);
    const roleId = localStorage.getItem("roleId")
    return (
        <>
            <div className="col-3 col-account col-nav" style={{ display: 'flex', alignItems: 'center' }}>
                <div style={{ display: 'flex', alignItems: 'center', position: 'relative', cursor: 'pointer' }}
                    onClick={() => setIsMenu(!isMenu)}
                >
                    <div className="account__wrapper" onClick={() => setIsMenu(!isMenu)} style={{ cursor: 'pointer' }}>
                                    <img src={`${BASE_URL_DOWNLOAD}${user?.image}`} style={{ width: 50, height: 50, borderRadius: 25 }} />
                                    <span style={{ marginLeft: 15 }}> {user?.name}</span>
                                </div>
                    {/* <img src={`${BASE_URL_DOWNLOAD}${user?.image}`} style={{ width: 50, height: 50, borderRadius: 25 }} />

                    <span style={{ marginLeft: 5,padding:5,background:'#fff5fb',fontWeight:700,borderRadius:8 }}>
                        {user?.name}
                    </span> */}
                    <div style={{ position: 'absolute', background: '#fff', border: '1px solid #333', top: '110%', display: isMenu ? 'flex' : 'none' }}>
                        {roleId == 5 ?
                            <ul style={{ flexDirection: 'column', padding: 10, color: '#333' }}>
                                <li onClick={() => {
                                    localStorage.setItem("page", "profile")
                                    history.push('/admin')
                                }} style={{ color: '#333', paddingLeft: 0, cursor: 'pointer' }}>Cá nhân</li>
                                <hr />
                                <li onClick={() => {
                                    localStorage.setItem("page", "booking-hotel")
                                    history.push('/admin')
                                }} style={{ color: '#333', paddingLeft: 0, cursor: 'pointer' }}>Danh sách đặt</li>
                                <hr />
                                <li
                                    onClick={() => {
                                        localStorage.removeItem("token")
                                        window.location.reload()
                                    }}
                                    style={{ color: '#333', paddingLeft: 0, cursor: 'pointer' }} >Đăng xuất</li>
                            </ul> :
                            <ul style={{ flexDirection: 'column', padding: 10, color: '#333' }}>
                                <li onClick={() => {
                                    localStorage.setItem("page", "profile")
                                    history.push('/admin')
                                }} style={{ color: '#333', paddingLeft: 0, cursor: 'pointer' }}>Cá nhân</li>
                                <hr />
                                <li
                                    onClick={() => {
                                        localStorage.removeItem("token")
                                        window.location.reload()
                                    }}
                                    style={{ color: '#333', paddingLeft: 0, cursor: 'pointer' }} >Đăng xuất</li>
                            </ul>
                        }
                    </div>
                </div>

                {/* <div>
                    <ul>
                        <li onClick={() => {
                            history.push('/admin')
                        }}>Cá nhân</li>
                        <li
                            onClick={() => {
                                localStorage.removeItem("token")
                                window.location.reload()
                            }}
                        >Đăng xuất</li>
                    </ul>
                </div> */}
                {/* <div className="account__wrapperr" onClick={() => {
                    localStorage.removeItem("token")
                    window.location.reload()
                }}>
                    <span>Đăng xuất</span>
                </div> */}
            </div>
        </>
    )
}