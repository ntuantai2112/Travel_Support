import moment from 'moment';
import React, { useEffect, useState } from 'react';
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';
import ChangePassword from './ChangePassword';
import ModalUpdate from './ModalUpdate';
export default function Profile() {
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
               
            }
        } catch (error) {
            console.log(error)

        }
    }
    let [showUpdate, setShowUpdate] = useState(false);
    const handleShowUpdate = () => {
        setShowUpdate(true)
    }
    const handleCloseUpdate = () => {
        setShowUpdate(false)
    }
    let [show, setShow] = useState(false);
    const handleShow = () => {
        setShow(true)
    }
    const handleClose = () => {
        setShow(false)
    }
    console.log(user)
    return (
        <>
            <div style={{display:'flex',justifyContent:'space-around',alignItems:'center',marginTop:100}}>
                <div className="btn__edit">
                    <button onClick={handleShowUpdate}>
                        <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                            <path fill="currentColor" d="M20.71,7.04C21.1,6.65 21.1,6 20.71,5.63L18.37,3.29C18,2.9 17.35,2.9 16.96,3.29L15.12,5.12L18.87,8.87M3,17.25V21H6.75L17.81,9.93L14.06,6.18L3,17.25Z" />
                        </svg>
                    </button>
                </div>
                <div  className="edit_password" >
                    <button onClick={handleShow}>
                        Thay đổi mật khẩu
                    </button>
                </div>
            </div>
            <ModalUpdate handleClose={handleCloseUpdate} handleShow={handleShowUpdate} show={showUpdate} user={user} search={search} />
            <ChangePassword handleClose={handleClose} handleShow={handleShow} show={show}  />

            <div className="profile__admin">
                <div className="profile__admin__item">
                    <label>
                        Họ tên
                    </label>
                    <div>
                        {user?.name}
                    </div>
                </div>
                <div className="profile__admin__item">
                    <label>
                        Ngày sinh
                    </label>
                    <div>
                        {moment(user?.dob,"YYYYMMDD").format("DD/MM/YYYY")}
                    </div>
                </div>
                <div className="profile__admin__item">
                    <label>
                        Số điện thoại
                    </label>
                    <div>
                        {user?.phone}
                    </div>
                </div>
                <div className="profile__admin__item">
                    <label>
                        Giới tính
                    </label>
                    <div>
                        {user?.gender}
                    </div>
                </div>
                <div className="profile__admin__item">
                    <label>
                        Email
                    </label>
                    <div>
                        {user?.email}
                    </div>
                </div>
                <div className="profile__admin__item">
                    <label>
                        Avatar
                    </label>
                    <div>
                        <img src={`${BASE_URL_DOWNLOAD}${user?.image}`} style={{ width: 50, height: 50, borderRadius: 25 }} />
                    </div>
                </div>
            </div>

        </>
    )
}