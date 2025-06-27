import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import logo from '../../image/logot.png';
import API, { BASE_URL_DOWNLOAD } from '../../lib/API';
import '../../style/style.scss';


export default function HeaderAdmin({ handlePage }) {
    let history = useHistory();
    const token = localStorage.getItem("token");
    console.log(token);
    const [isMenu, setIsMenu] = useState(false)
    const [user, setUser] = useState();
    useEffect(() => {
        search()
    }, [])
    let search = async () => {
        try {
            let path = '/member/account/profile';
            let resp = await API.authorizedJSONGET(path);
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
    console.log(user)
    return (
        <>
            <header>
                <div className="wrapper__header">
                    <div className="container__header container-fluid">
                        <div className="row">
                            <div className="col-2 col-logo col-nav">
                                <div className="wrapper__logo">
                                    <img alt="" src={logo} />
                                </div>
                            </div>
                            <div className="col-7 col-menu col-nav">

                            </div>
                            <div className="col-3 col-account col-nav" style={{ position: 'relative' }}>
                                <div className="account__wrapper" onClick={() => setIsMenu(!isMenu)} style={{cursor:'pointer'}}>
                                    <img src={`${BASE_URL_DOWNLOAD}${user?.image}`} style={{ width: 50, height: 50, borderRadius: 25 }} />
                                    <span style={{marginLeft:15}}> {user?.name}</span>
                                </div>
                                {isMenu && <ul style={{
                                    position: 'absolute', backgroundColor: '#fff', flexDirection: 'column',
                                    right: '4%', top: '73%',
                                    border: '1px soldi #333', zIndex: 9
                                }}>
                                    <li
                                        onClick={() => handlePage("profile")} style={{ color: '#333' }}
                                    >Thông tin cá nhân</li>
                                    <li onClick={() => {
                                        history.push('/login')
                                        localStorage.removeItem("token")
                                    }}
                                        style={{ color: '#333' }}
                                    >Đăng xuất</li>
                                </ul>}
                            </div>
                        </div>
                    </div>
                </div>
            </header>
        </>
    )
}