import React from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { BASE_URL_DOWNLOAD } from '../../../lib/API';

export default function ModalDetail({ show, handleClose, data }) {


    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()

            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Chi tiết bài viết</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div style={{ width: '80vw' }}>
                        <ul className="menu">
                            <li className="menu__item">
                                <div className="menu__item--title">Tiêu đề:</div>
                                <div className="menu__item--input">

                                    <input
                                        className=""
                                        value={data?.title}
                                        style={{ width: '100%', padding: 8 }}
                                    />


                                </div>
                            </li>

                            <li className="menu__item">
                                <div className="menu__item--title">Nội dung:</div>
                                <div className="menu__item--input">

                                    <textarea

                                        style={{ width: '100%', height: 500, padding: 8 }}
                                        value={data?.content}
                                    />

                                </div>
                            </li>
                            <li className="menu__item">
                                <div className="menu__item--title">Ảnh:</div>
                                <div className="menu__item--input">
                                </div>

                                <>
                                    <img
                                        id="target"
                                        src={`${BASE_URL_DOWNLOAD}${data?.image}`}
                                        style={{ width: 300, height: 200, objectFit: "cover" }}
                                        alt=""
                                    />
                                </>

                            </li>
                        </ul>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="success" onClick={() => {
                        handleClose()
                    }}>
                        Đóng
                    </Button>

                </Modal.Footer>


            </Modal>
        </>
    )
}