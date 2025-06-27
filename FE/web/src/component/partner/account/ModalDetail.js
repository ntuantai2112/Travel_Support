import React, { useEffect, useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import { useForm } from "react-hook-form";
import API from '../../../lib/API';

export default function ModalDetail({ show, handleClose, handleShow, data }) {




    return (
        <>

            <Modal show={show}
                onHide={() => {
                    handleClose()

                }}
                animation={false} centered>

                <Modal.Header closeButton>
                    <Modal.Title>Thêm tài khoản</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div>
                        <ul className="menu">
                            <li className="menu__item">
                                <div className="menu__item--title">Tên:</div>
                                <div className="menu__item--input">
                                    <input type="text"
                                        value={data?.name}
                                    />
                                </div>
                            </li>
                            <li className="menu__item">
                                <div className="menu__item--title">Giới tính:</div>
                                <div className="menu__item--input">
                                    <div style={{ display: 'flex', alignItems: 'center', width: 300 }}>
                                        <div style={{ display: 'flex', alignItems: 'center' }}>
                                            <input type="radio" style={{ width: 50 }}
                                                checked={data?.gender == "nam" ? true : false}
                                            /> Nam</div>
                                        <div style={{ display: 'flex', alignItems: 'center' }}><input type="radio" name="gender" style={{ width: 50 }}
                                            checked={data?.gender == "nữ" ? true : false}
                                        />Nữ</div>
                                    </div>
                                </div>


                            </li>
                            <li className="menu__item">
                                <div className="menu__item--title">Ngày sinh:</div>
                                <div className="menu__item--input">
                                    <input type="date"
                                        name="dob"
                                        value={data?.dob}
                                    />
                                </div>


                            </li>
                            <li className="menu__item">
                                <div className="menu__item--title">Số ĐT:</div>
                                <div className="menu__item--input">
                                    <input type="text"
                                        name="phone"
                                        value={data?.phone}
                                    />
                                </div>


                            </li>

                            <li className="menu__item">
                                <div className="menu__item--title">Email:</div>
                                <div className="menu__item--input">
                                    <input type="text"
                                        name="email"
                                        value={data?.email}
                                    />
                                </div>


                            </li>
                            <li className="menu__item">
                                <div className="menu__item--title">Quyền:</div>
                                <div className="menu__item--input">
                                    <select value={data?.roleId}
                                        

                                     

                                    >
                                        <option value={1}>Admin</option>
                                        <option value={2}>Employee</option>
                                        <option value={3}>Content</option>
                                        <option value={4}>Partner</option>
                                        <option value={5}>Member</option>
                                    </select>
                                </div>


                            </li>

                        </ul>
                        <br />
                    </div>
                </Modal.Body>
                <Modal.Footer>

                    <Button variant="secondary" onClick={() => {

                        handleClose()

                    }}>
                        Đóng

                    </Button>
                </Modal.Footer>

            </Modal>

        </>
    )
}