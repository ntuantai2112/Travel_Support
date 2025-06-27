import React, { useEffect, useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import { Controller, useForm } from "react-hook-form";
import API from '../../../lib/API';


export default function ChangePassword({ show, handleClose, handleShow, search }) {

    const { register, handleSubmit, watch, formState: { errors }, reset } = useForm();
    const [message, setMessage] = useState();

    const [showSuccess, setShowSuccess] = useState(false);
    const handleCloseSuccess = () => {
        setShowSuccess(false)
    }
    let submitHandler = async (data) => {
        console.log(data)
        if (data?.newPassword !== data?.overPassword) {
            setMessage("Mật khẩu bạn nhập lại chưa đúng!")
        } else {
            let path = '/member/change-password';
            let resp = await API.authorizedJSONPost(path, {
                password: data?.password,
                newPassword: data?.newPassword
            });
           
            if (resp.ok) {
                reset()
                setMessage("")
                handleClose()
                search()
                alert("Bạn đã đổi mật khẩu thành công")
            } else {
                let response = await resp.json();
                setMessage(response?.message)
            }
        }

    }
    return (
        <>
            <ModalSuccess showSuccess={showSuccess} handleCloseSuccess={handleCloseSuccess} />
            <Modal show={show}
                onHide={() => {
                    handleClose()
                    setMessage(null)
                    reset()
                }}
                animation={false} centered>
                <form
                    onSubmit={handleSubmit(onSubmit)}
                >
                    <Modal.Header closeButton>
                        <Modal.Title>Thay đổi mật khẩu</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
                        <div>
                            <ul className="menu">
                                <li className="menu__item">
                                    <div className="menu__item--title">Mật khẩu cũ:</div>
                                    <div className="menu__item--input">
                                        <Controller
                                            control={control}
                                            render={({ field: { onChange, onBlur, value } }) => (
                                                <input
                                                    onBlur={onBlur}
                                                    className=""
                                                    onChange={e => { onChange(e.target.value) }}
                                                    value={value}
                                                    type="password"
                                                />

                                            )}
                                            name="password"
                                            rules={{ required: true }}
                                            defaultValue=""
                                        />
                                    </div>
                                    <div className="menu__item--error"> {errors.password && <span>Trường này không được để trống</span>}</div>
                                </li>
                                <li className="menu__item">
                                    <div className="menu__item--title">Mật khẩu mới:</div>
                                    <Controller
                                        control={control}
                                        render={({ field: { onChange, onBlur, value } }) => (
                                            <input
                                                onBlur={onBlur}
                                                className=""
                                                onChange={e => { onChange(e.target.value) }}
                                                value={value}
                                                type="password"
                                            />

                                        )}
                                        name="newPassword"
                                        rules={{ required: true }}
                                        defaultValue=""
                                    />
                                    <div className="menu__item--error"> {errors.newPassword && <span>Trường này không được để trống</span>}</div>
                                </li>
                                <li className="menu__item">
                                    <div className="menu__item--title">Nhập lại mật khẩu mới:</div>
                                    <div className="menu__item--input">
                                        <Controller
                                            control={control}
                                            render={({ field: { onChange, onBlur, value } }) => (
                                                <input
                                                    onBlur={onBlur}
                                                    className=""
                                                    onChange={e => { onChange(e.target.value) }}
                                                    value={value}
                                                    type="password"
                                                />

                                            )}
                                            name="overPassword"
                                            rules={{ required: true }}
                                            defaultValue=""
                                        />
                                    </div>
                                    <div className="menu__item--error"> {errors.overPassword && <span>Trường này không được để trống</span>}</div>
                                </li>
                            </ul>
                            <br />
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="success" onClick={handleSubmit(submitHandler)}>
                            Xác nhận
                        </Button>
                        <Button variant="secondary" onClick={() => {

                            handleClose()
                            setMessage(null)
                            reset()
                        }}>
                            Quay lại

                        </Button>
                    </Modal.Footer>
                </form>
            </Modal>

        </>
    )
}

