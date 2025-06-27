import React, { useEffect, useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import { useForm } from "react-hook-form";
import API from '../../../lib/API';
import { amenities, typeRooms } from '../../../utils/amenities';
import LoadingProgress from '../../LoadingProgress';

export default function ModalUploadMenu({ show, handleClose, search, restaurantCode }) {

    const { register, handleSubmit, watch, formState: { errors }, reset } = useForm();
    const [filePath, setFilePath] = useState();
    const [file, setFile] = useState();
    const [loading, setLoading] = useState(false)
    const [message, setMessage] = useState();




    let onSubmit = async (form) => {
        console.log(form)
        let path = '/partner/restaurant/menu/upload';

        let data = new FormData()
        data.append("multipartFile", file);
        data.append("restaurantCode", restaurantCode)
        data.append("description", form?.description)
        data.append("name", form?.name)
        data.append("price", form?.price)
        data.append("currency", "VNĐ")
        let resp = await API.authorizedFilePost(path, data);
        if (resp.ok) {
            handleClose()
            search()
            reset();
            alert("Thêm thực đơn thành công!")
            window.location.reload()
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
                        <Modal.Title>Thêm thực đơn </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {!loading ?
                            <>
                                <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
                                <div>
                                    <ul className="menu">
                                        <li className="menu__item">
                                            <div className="menu__item--title">Tên món ăn:</div>
                                            <div className="menu__item--input">
                                                <input type="text"
                                                    {...register("name", { required: true })}
                                                />
                                            </div>

                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Mô tả:</div>
                                            <div className="menu__item--input">
                                                <textarea type="text"
                                                    {...register("description")}
                                                    style={{ width: '100%', height: 200 }}
                                                />
                                            </div>

                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Nhập giá:</div>
                                            <div className="menu__item--input">
                                                <input type="text"
                                                    {...register("price")}
                                                  
                                                />
                                            </div>

                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Ảnh:</div>
                                            <div className="menu__item--input">

                                                <input
                                                    type="file"
                                                    onChange={e => {
                                                        setFile(e.target.files[0])
                                                        setFilePath(URL.createObjectURL(e.target.files[0]));
                                                    }}

                                                />
                                            </div>
                                            {filePath ? (
                                                <div>
                                                    <img
                                                        id="target"
                                                        src={filePath}
                                                        style={{ width: 300, height: 200, objectFit: "cover" }}
                                                        alt=""
                                                    />
                                                </div>
                                            ) : (
                                                <></>
                                            )}
                                        </li>

                                    </ul>
                                    <br />
                                </div>
                            </> :
                            <LoadingProgress />
                        }
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