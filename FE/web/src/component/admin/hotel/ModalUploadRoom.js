import React, { useEffect, useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import { useForm } from "react-hook-form";
import API from '../../../lib/API';
import { amenities, typeRooms } from '../../../utils/amenities';

export default function ModalUploadRoom({ show, handleClose, handleShow, search, hotelCode, handleUpload }) {

    const { register, handleSubmit, watch, formState: { errors }, reset } = useForm();
    const [filePath, setFilePath] = useState();
    const [file, setFile] = useState();

    const [message, setMessage] = useState();


    let [arrAmenities, setArrAmenities] = useState([])

    let onSubmit = async (form) => {
        console.log(form)
        let path = '/partner/hotel/room/upload';

        let data = new FormData()
        data.append("imageList", file);

        data.append("hotelOptionCode", form?.hotelOptionCode);
        data.append("price", form?.price);
        data.append("hotelCode", hotelCode)
        data.append("type", form?.type)
        data.append("currency", "VNĐ")
        data.append("imageList", file)
        data.append("amenities", arrAmenities)

        let resp = await API.authorizedFilePost(path, data);
        if (resp.ok) {
            handleClose()
            search()
            reset();
            handleUpload()
            console.log("ok")
        } else {
            setMessage("Vui lòng kiểm tra lại thông tin")
        }

    }

    let handleChange = (value) => {
        if (arrAmenities.includes(value)) {
            let newArr = arrAmenities.filter((item) => item !== value);
            setArrAmenities(newArr)
        } else {
            setArrAmenities([
                ...arrAmenities,
                value
            ])
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
                        <Modal.Title>Thêm phòng khách sạn</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
                        <div>
                            <ul className="menu">
                               
                                <li className="menu__item">
                                    <div className="menu__item--title">Giá:</div>
                                    <div className="menu__item--input">
                                        <input type="text"
                                            {...register("price", { required: true })}
                                        />
                                    </div>

                                </li>

                                <li className="menu__item">
                                    <div className="menu__item--title">Loại phòng:</div>
                                    <div className="menu__item--input">
                                        <select
                                            {...register("type", { required: true })}
                                            defaultValue={typeRooms[0]?.value}
                                        >
                                            {typeRooms?.map((item, index) => {
                                                return (
                                                    <option value={item?.value} key={index}>{item?.name}</option>
                                                )
                                            })}
                                        </select>
                                    </div>

                                </li>

                                <li className="menu__item">
                                    <div className="menu__item--title">Tiện ích :</div>
                                    <div className="menu__item--input">
                                        {amenities?.map((item, index) => {
                                            return (
                                                <div key={index}>
                                                    <input type="checkbox" value={item?.id}
                                                        onChange={e => handleChange(e.target.value)}
                                                    /> <img src={item?.icon} style={{ width: 20, height: 20 }} /> {item?.name}
                                                </div>
                                            )
                                        })}
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