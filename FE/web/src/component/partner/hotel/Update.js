import React, { useEffect, useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import { Controller, useForm } from "react-hook-form";
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';
import { amenities, typeHotels, typeRooms } from '../../../utils/amenities';
import LoadingProgress from '../../LoadingProgress';

export default function Update({ show, handleClose, search, data, image, roomId, hotelCode }) {
    const { control, reset, handleSubmit, formState: { errors }, register } = useForm();
    const [filePath, setFilePath] = useState();
    const [file, setFile] = useState();
    const [message, setMessage] = useState();
    const [loading, setLoading] = useState(false)
    let [arrAmenities, setArrAmenities] = useState([])

    useEffect(() => {
        handleAmenities()
    }, [data])
    let handleAmenities = () => {
        let resp = [];

        data?.amenities?.map((item) => {
            resp.push(item?.key)
        })

        setArrAmenities(resp);


    }

    let onSubmit = async (form) => {
      
        try {
            setLoading(true)
            let path = '/partner/hotel/room/update';
            let data = new FormData();
            data.append("hotelCode", hotelCode);
            data.append("roomId", roomId);
            data.append("hotelOptionCode", form?.hotelOptionCode)
            data.append("type", form?.type)
            data.append("currency", "VNĐ")
            data.append("price", form?.price)
            data.append("amenities", arrAmenities)
            file && data.append("image", file)

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
                        <Modal.Title>Cập nhập phòng</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {!loading ?
                            <>
                                <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
                                <div>
                                    <ul className="menu">
                                        <li className="menu__item">
                                            <div className="menu__item--title">Mã phòng:</div>
                                            <div className="menu__item--input">
                                                <Controller
                                                    control={control}
                                                    render={({ field: { onChange, onBlur, value } }) => (
                                                        <input
                                                            onBlur={onBlur}
                                                            className=""
                                                            onChange={e => onChange(e.target.value)}
                                                            value={value}
                                                            type="text"
                                                        />

                                                    )}
                                                    name="hotelOptionCode"
                                                    defaultValue={data?.hotelOptionCode}
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
                                            <div className="menu__item--title">Loại:</div>
                                            <div className="menu__item--input">
                                                <Controller
                                                    control={control}
                                                    render={({ field: { onChange, onBlur, value } }) => (
                                                        <select
                                                            onChange={e => onChange(e.target.value)}
                                                            value={value}
                                                            onBlur={onBlur}
                                                        >
                                                            {typeRooms?.map((item, index) => {
                                                                return (
                                                                    <option value={item?.value} key={index}>{item?.name}</option>
                                                                )
                                                            })}
                                                        </select>

                                                    )}
                                                    name="type"
                                                    defaultValue={data?.roomType}
                                                />
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
                                                                defaultChecked={arrAmenities.includes(String(item.id))}
                                                            /> <img src={item?.icon} style={{ width: 20, height: 20 }} /> {item?.name}
                                                        </div>
                                                    )
                                                })}
                                            </div>
                                        </li>


                                        <li className="menu__item">
                                            <div className="menu__item--title">Ảnh phòng:</div>
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