import React, { useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { Controller, useForm } from "react-hook-form";
import API from '../../../lib/API';
export default function ChangePassword({ show, handleClose }) {
    const { control, reset, handleSubmit, formState: { errors }, register } = useForm();
    let [message, setMessage] = useState()
    let submitHandler = async form => {
        if (form?.newPassword !== form?.newPss) {
            setMessage("Mật khẩu bạn nhập lại chưa đúng")
        } else {
            console.log(form)
            let path = `/member/change-password`;
            let objReq = {
                password: form?.password,
                newPassword: form?.newPassword
            }
            let resp = await API.authorizedJSONPost(path, objReq);
            if (resp.ok) {
                alert("Bạn đã thay đổi mật khẩu thành công")
                handleClose()
                setMessage()
                reset()
            } else {
                let response = await resp.json();
                setMessage(response?.message)
            }

        }

    }
    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()
                reset()
                setMessage("")
            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Thay đổi mật khẩu</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
                    <ul className="menu">
                        <li className="menu__item">
                            <div className="menu__item--title">Mật khẩu:</div>
                            <div className="menu__item--input">
                                <Controller
                                    control={control}
                                    render={({ field: { onChange, onBlur, value } }) => (
                                        <input
                                            onBlur={onBlur}
                                            type="password"
                                            className=""
                                            onChange={onChange}
                                            value={value}
                                        />

                                    )}
                                    rules={{ required: true }}
                                    name="password"
                                    defaultValue=""
                                />
                            </div>
                            {errors.password && <div style={{ color: 'red' }}>Trường này không được bỏ trống</div>}
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Mật khẩu mới:</div>
                            <div className="menu__item--input">
                                <Controller
                                    control={control}
                                    render={({ field: { onChange, onBlur, value } }) => (
                                        <input
                                            onBlur={onBlur}
                                            type="password"
                                            className=""
                                            onChange={onChange}
                                            value={value}
                                        />

                                    )}
                                    rules={{ required: true }}
                                    name="newPassword"
                                    defaultValue=""
                                />
                            </div>
                            {errors.newPassword && <div style={{ color: 'red' }}>Trường này không được bỏ trống</div>}
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Nhập lại mật khẩu mới:</div>
                            <div className="menu__item--input">
                                <Controller
                                    control={control}
                                    render={({ field: { onChange, onBlur, value } }) => (
                                        <input
                                            onBlur={onBlur}
                                            type="password"
                                            className=""
                                            onChange={onChange}
                                            value={value}
                                        />

                                    )}
                                    rules={{ required: true }}
                                    name="newPss"
                                    defaultValue=""
                                />
                            </div>
                            {errors.newPss && <div style={{ color: 'red' }}>Trường này không được bỏ trống</div>}
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
                            reset()
                            setMessage("")
                        }}
                    >
                        Hủy

                    </Button>
                </Modal.Footer>


            </Modal>
        </>
    )
}