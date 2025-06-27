import React, { useEffect, useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import { Controller, useForm } from "react-hook-form";
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';
import { amenities, typeHotels, typeRooms } from '../../../utils/amenities';
import LoadingProgress from '../../LoadingProgress';

export default function Update({ show, handleClose, search, data, image, menuId, restaurantCode }) {
    const { control, reset, handleSubmit, formState: { errors }, register } = useForm();
    const [filePath, setFilePath] = useState();
    const [file, setFile] = useState();
    const [message, setMessage] = useState();
    const [loading, setLoading] = useState(false)


    let onSubmit = async (form) => {

        try {
            setLoading(true)
            let path = '/partner/restaurant/menu/update';
            let data = new FormData();
            data.append("restaurantCode", restaurantCode);
            data.append("menuId", menuId);
            data.append("name", form?.name)
          
            data.append("description", form?.description)
            data.append("price", form?.price)

            file && data.append("multipartFile", file)

            let resp = await API.authorizedFilePost(path, data);
            if (resp.ok) {
                setLoading(false)
                handleClose()
                search()
                reset();
                console.log("ok")
            } else {
                setLoading(false)
                alert("Yêu cầu điền đầy đủ thông tin!")
            }


        } catch (error) {

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
                        <Modal.Title>Cập nhập món ăn</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {!loading ?
                            <>
                                <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
                                <div>
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
                                                    defaultValue={data?.name}
                                                />
                                            </div>
                                        </li>

                                        <li className="menu__item">
                                            <div className="menu__item--title">Mô tả:</div>
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
                                                    name="description"
                                                    defaultValue={data?.description}
                                                />
                                            </div>
                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Giá:</div>
                                            <div className="menu__item--input">
                                                <Controller
                                                    control={control}
                                                    render={({ field: { onChange, onBlur, value } }) => (
                                                        <input
                                                            onBlur={onBlur}
                                                            className=""
                                                            onChange={e => onChange(e.target.value)}
                                                            value={value}
                                                            type="number"
                                                        />

                                                    )}
                                                    name="price"
                                                    defaultValue={data?.price}
                                                />
                                            </div>
                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Ảnh thực đơn:</div>
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
                                                <>
                                                    <img
                                                        id="target"
                                                        src={`${BASE_URL_DOWNLOAD}${image}`}
                                                        style={{ width: 300, height: 200, objectFit: "cover" }}
                                                        alt=""
                                                    />
                                                </>
                                            )}
                                        </li>
                                    </ul>
                                    <br />
                                </div>
                            </> : <LoadingProgress />
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