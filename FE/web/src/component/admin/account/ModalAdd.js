import React, { useEffect, useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import { useForm } from "react-hook-form";
import API from '../../../lib/API';
import { roles } from '../../../utils/amenities';

export default function ModalAdd({ show, handleClose, handleShow, search }) {

    const { register, handleSubmit, watch, formState: { errors }, reset } = useForm();

    const [message, setMessage] = useState();



    let onSubmit = async (data) => {
        let path = '/admin/account/add';
        let resp = await API.authorizedJSONPost(path, data);
        if (resp.ok) {
            alert("Thêm tài khoản thành công!")
            handleClose()
            search()
            reset(null)
        } else {
            setMessage("Vui lòng kiểm tra lại thông tin")
        }

    }
    return (
        <>

            <Modal show={show}
                onHide={() => {
                    handleClose()
                    setMessage(null)
                }}
                animation={false} centered>
                <form
                    onSubmit={handleSubmit(onSubmit)}
                >
                    <Modal.Header closeButton>
                        <Modal.Title>Thêm tài khoản</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
                        <div>
                            <ul className="menu">
                                <li className="menu__item">
                                    <div className="menu__item--title">Tên:</div>
                                    <div className="menu__item--input">
                                        <input type="text"
                                            {...register("name", { required: true })}
                                        />
                                    </div>
                                    <div className="menu__item--error"> {errors.name && <span>Trường này không được để trống</span>}</div>
                                </li>
                                <li className="menu__item">
                                    <div className="menu__item--title">Giới tính:</div>
                                    <div className="menu__item--input">
                                        <div style={{ display: 'flex', alignItems: 'center', width: 300 }}>
                                            <div style={{ display: 'flex', alignItems: 'center' }}>
                                                <input type="radio" style={{ width: 50 }}

                                                    {...register("gender", { required: true })}
                                                    value={true}
                                                    defaultChecked={true}
                                                    defaultValue={true}
                                                /> Nam</div>
                                            <div style={{ display: 'flex', alignItems: 'center' }}><input type="radio" name="gender" style={{ width: 50 }}
                                                {...register("gender", { required: true })}
                                                value={false}
                                            />Nữ</div>
                                        </div>
                                    </div>
                                    <div className="menu__item--error"> {errors.gender && <span>Trường này không được để trống</span>}</div>

                                </li>
                                <li className="menu__item">
                                    <div className="menu__item--title">Ngày sinh:</div>
                                    <div className="menu__item--input">
                                        <input type="date"
                                            name="dob"
                                            {...register("dob", { required: true })}
                                        />
                                    </div>
                                    <div className="menu__item--error"> {errors.dob && <span>Trường này không được để trống</span>}</div>

                                </li>
                                <li className="menu__item">
                                    <div className="menu__item--title">Số ĐT:</div>
                                    <div className="menu__item--input">
                                        <input type="number"
                                            name="phone"
                                            {...register("phone", { required: true })}
                                        />
                                    </div>
                                    <div className="menu__item--error"> {errors.phone && <span>Trường này không được để trống</span>}</div>

                                </li>

                                <li className="menu__item">
                                    <div className="menu__item--title">Email:</div>
                                    <div className="menu__item--input">
                                        <input type="email"
                                            name="email"
                                            {...register("email", { required: true })}
                                        />
                                    </div>
                                    <div className="menu__item--error"> {errors.email && <span>Trường này không được để trống</span>}</div>

                                </li>
                                <li className="menu__item">
                                    <div className="menu__item--title">Role Id:</div>
                                    <div className="menu__item--input">
                                        <select
                                            name="roleId"
                                            {...register("roleId", { required: true })}
                                            defaultValue={roles[0]?.value}
                                        >
                                            {roles?.map((item, index) => {
                                                return (
                                                    <option value={item?.value}>{item?.name}</option>
                                                )
                                            })}
                                        </select>

                                    </div>
                                    <div className="menu__item--error"> {errors.roleId && <span>Trường này không được để trống</span>}</div>

                                </li>

                            </ul>
                            <br />
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="success" type="submit">
                            Xác nhận
                        </Button>
                        <Button variant="secondary" onClick={() => {

                            handleClose()
                            setMessage(null)
                        }}>
                            Đóng

                        </Button>
                    </Modal.Footer>
                </form>
            </Modal>

        </>
    )
}