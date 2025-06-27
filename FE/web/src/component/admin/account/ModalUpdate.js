import moment from 'moment';
import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { Controller, useForm } from "react-hook-form";
import API from '../../../lib/API';

export default function ModalUpdate({ show, handleClose, id, search, account }) {
    const { control, reset, handleSubmit, formState: { errors }, register } = useForm();
    console.log(account?.dob, account?.gender)
    const [message, setMessage] = useState()
    const [gender, setGender] = useState(true);
    const [dob, setDob] = useState()
    let handleGender = () => {
        if (account?.gender == "nam") {
            setGender(true)
        } else {
            setGender(false)
        }
    }
    let handleDate = () => {
        let date = moment(account?.dob, "YYYYMMDD").format("YYYY-MM-DD");
        setDob(date)
    }
    useEffect(() => {
        handleGender()
        handleDate()
    }, [id])

    let submitHandler = async form => {
        let path = `/admin/account/update/${id}`;

        let objReq = {
            name: form?.name,
            phone: form?.phone,
            email: form?.email,
            roleId: form?.roleId,
            dob: moment(dob, "YYYY-MM-DD").format("YYYYMMDD"),
            gender: gender
        }
        let resp = await API.authorizedJSONPut(path, objReq);
        if (resp.ok) {
            search()
            reset(null)
            handleClose()
        } else {
            setMessage("Vui lòng kiểm tra lại thông tin")
        }
    }
    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()
                setMessage(null)
            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Cập nhật tài khoản</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
                    <ul className="menu">
                        <li className="menu__item">
                            <div className="menu__item--title">Tên:</div>
                            <div className="menu__item--input">
                                <Controller
                                    control={control}
                                    render={({ field: { onChange, onBlur, value } }) => (
                                        <input
                                            onBlur={onBlur}
                                            className=""
                                            onChange={e => onChange(e.target.value)}
                                            value={value}
                                        />

                                    )}
                                    name="name"
                                    defaultValue={account?.name}
                                />
                            </div>
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Giới tính:</div>
                            <div className="menu__item--input">
                                <div style={{ display: 'flex', alignItems: 'center', width: 300 }}>
                                    <div style={{ display: 'flex', alignItems: 'center' }}>
                                        <input type="radio" style={{ width: 50 }}
                                            checked={gender}
                                            name="gender"
                                            onClick={() => setGender(true)}
                                        /> Nam</div>
                                    <div style={{ display: 'flex', alignItems: 'center' }}><input type="radio" name="gender" style={{ width: 50 }}
                                        checked={!gender}
                                        onClick={() => setGender(false)}
                                    />Nữ</div>
                                </div>
                            </div>
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Ngày sinh:</div>
                            <div className="menu__item--input">

                                <input

                                    type="date"
                                    className=""
                                    onChange={e => {
                                        setDob(e.target.value)
                                    }}
                                    value={dob}
                                // defaultValue={moment(account?.dob, "YYYYMMDD").format("YYYY-MM-dd")}
                                />


                            </div>
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Số điện thoại:</div>
                            <div className="menu__item--input">
                                <Controller
                                    control={control}
                                    render={({ field: { onChange, onBlur, value } }) => (
                                        <input
                                            onBlur={onBlur}
                                            className=""
                                            onChange={e => onChange(e.target.value)}
                                            value={value}
                                        />

                                    )}
                                    name="phone"
                                    defaultValue={account?.phone}
                                />
                            </div>
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Email:</div>
                            <div className="menu__item--input">
                                <Controller
                                    control={control}
                                    render={({ field: { onChange, onBlur, value } }) => (
                                        <input
                                            onBlur={onBlur}
                                            className=""
                                            onChange={e => onChange(e.target.value)}
                                            value={value}
                                        />

                                    )}
                                    name="email"
                                    defaultValue={account?.email}
                                />
                            </div>
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Quyền:</div>
                            <div className="menu__item--input">
                                <Controller
                                    control={control}
                                    render={({ field: { onChange, onBlur, value } }) => (
                                        <select
                                            onChange={e => onChange(e.target.value)}
                                            value={value}

                                        >
                                            <option value={1}>Admin</option>
                                            <option value={2}>Employee</option>
                                            <option value={3}>Content</option>
                                            <option value={4}>Partner</option>
                                            <option value={5}>Member</option>
                                        </select>

                                    )}
                                    name="roleId"
                                    defaultValue={account?.roleId}
                                />
                            </div>
                        </li>
                    </ul>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="success" onClick={handleSubmit(submitHandler)}>
                        Xác nhận
                    </Button>
                    <Button variant="secondary"
                        onClick={() => {
                            handleClose()
                            setMessage(null)
                        }}
                    >
                        Hủy

                    </Button>
                </Modal.Footer>


            </Modal>
        </>
    )
}