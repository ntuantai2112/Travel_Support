import React, { useEffect, useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import { Controller, useForm } from "react-hook-form";
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';
import { amenities, typeActivity, typeHotels } from '../../../utils/amenities';
import LoadingProgress from '../../LoadingProgress';

export default function ModalUpdate({ handleClose, handleShow, show, data, images, search, activitiesCode, type }) {

    const { control, reset, handleSubmit, formState: { errors }, register } = useForm();
    console.log("dât", data)
    const [fileList, setFileList] = useState();
    const [imageList, setImageList] = useState([]);
    const [message, setMessage] = useState();
    const [loading, setLoading] = useState(false)

    let [arrAmenities, setArrAmenities] = useState([])



    let onSubmit = async (form) => {
        console.log(form)
        console.log(activitiesCode)
        try {
            setLoading(true)
            if (imageList?.length > 0) {
                let path = '/partner/activities/update';
                let listPj = Array.from(imageList);
                let data = new FormData();

                for (let i = 0; i < listPj?.length; i++) {
                    data.append("imageList", listPj[i]);
                }
                data.append("address", form?.address);
                data.append("activitiesCode", activitiesCode);
                data.append("description", form?.description);
                data.append("title", form?.title)
                data.append("type", form?.type)

                data.append("adultTicketPrice", form?.adultTicketPrice);
                data.append("childTicketPrice", form?.childTicketPrice)
                data.append("duration", form?.duration)

                let resp = await API.authorizedFilePost(path, data);
                if (resp.ok) {
                    setLoading(false)
                    handleClose()
                    search()
                    reset();

                } else {
                    setLoading(false)
                    alert("Yêu cầu điền đầy đủ thông tin!")
                }
            } else {
                let path = '/partner/activities/update';
                // let listPj = Array.from(imageList);
                let data = new FormData();

                // for (let i = 0; i < listPj?.length; i++) {
                //     data.append("imageList", listPj[i]);
                // }
                data.append("address", form?.address);
                data.append("activitiesCode", activitiesCode);
                data.append("description", form?.description);
                data.append("title", form?.title)
                data.append("type", form?.type)

                data.append("adultTicketPrice", form?.adultTicketPrice);
                data.append("childTicketPrice", form?.childTicketPrice)
                data.append("duration", 5)


                let resp = await API.authorizedFilePost(path, data);
                if (resp.ok) {
                    setLoading(false)
                    handleClose()
                    search()
                    reset();

                } else {
                    setLoading(false)
                    alert("Yêu cầu điền đầy đủ thông tin!")
                }
            }

        } catch (error) {

        }

    }
    let handleFileList = (e) => {
        let image = [];
        image.push(e.target.files);
        let listImage = [];
        for (let i = 0; i < image[0].length; i++) {
            listImage.push(URL.createObjectURL(image[0][i]));
        }
        setFileList(listImage);
    };
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
                        <Modal.Title>Cập nhập khu vui chơi</Modal.Title>
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
                                                            style={{width:'100%'}}
                                                        />

                                                    )}
                                                    name="title"
                                                    defaultValue={data?.title}
                                                />
                                            </div>
                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Mô tả:</div>
                                            <div className="menu__item--input">
                                                <Controller
                                                    control={control}
                                                    render={({ field: { onChange, onBlur, value } }) => (
                                                        <textarea
                                                            onBlur={onBlur}
                                                            className=""
                                                            onChange={e => onChange(e.target.value)}
                                                            value={value}
                                                            style={{width:'100%',height:300}}
                                                        />

                                                    )}
                                                    name="description"
                                                    defaultValue={data?.description}
                                                />
                                            </div>
                                        </li>

                                        <li className="menu__item">
                                            <div className="menu__item--title">Địa chỉ:</div>
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
                                                    name="address"
                                                    defaultValue={data?.address}
                                                />
                                            </div>
                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Giá vé người lớn:</div>
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
                                                    name="adultTicketPrice"
                                                    defaultValue={data?.adultPrice}
                                                />
                                            </div>
                                        </li>

                                        <li className="menu__item">
                                            <div className="menu__item--title">Giá vé trẻ em:</div>
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
                                                    name="childTicketPrice"
                                                    defaultValue={data?.childPrice}
                                                />
                                            </div>
                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Thời gian chơi:</div>
                                            <div className="menu__item--input">
                                                <Controller
                                                    control={control}
                                                    render={({ field: { onChange, onBlur, value } }) => (
                                                        <input
                                                            onBlur={onBlur}
                                                            className=""
                                                            onChange={e => onChange(e.target.value)}
                                                            value={value}
                                                            type="datetime"
                                                        />

                                                    )}
                                                    name="duration"
                                                    defaultValue={data?.duration}
                                                />
                                            </div>
                                        </li>

                                        <li className="menu__item">
                                            <div className="menu__item--title">Địa điểm:</div>
                                            <div className="menu__item--input">
                                                <Controller
                                                    control={control}
                                                    render={({ field: { onChange, onBlur, value } }) => (
                                                        <select
                                                            onChange={e => onChange(e.target.value)}
                                                            value={value}
                                                            onBlur={onBlur}
                                                        >
                                                            {typeActivity?.map((item, index) => {
                                                                return (
                                                                    <option value={item?.value} key={index}>{item?.name}</option>
                                                                )
                                                            })}
                                                        </select>

                                                    )}
                                                    name="type"
                                                    defaultValue={type}
                                                />
                                            </div>
                                        </li>




                                        <div>
                                            <label>Ảnh List:</label>{" "}
                                            <input
                                                type="file"
                                                onChange={(e) => {
                                                    setImageList(e.target.files);
                                                    handleFileList(e);
                                                }}
                                                multiple
                                                accept="image/gif, image/jpeg, image/png"
                                            />
                                        </div>
                                        <div style={{ display: "flex" }}>
                                            {fileList ?
                                                fileList?.map((item, idx) => {
                                                    return (
                                                        <div key={idx} style={{ margin: 10 }}>
                                                            <img
                                                                id="target"
                                                                src={item}
                                                                style={{ width: 300, height: 200, objectFit: "cover" }}
                                                                alt=""
                                                            />
                                                        </div>
                                                    );
                                                }) : <>
                                                    {images?.map((item, index) => {
                                                        return (
                                                            <div key={index} style={{ margin: 10 }}>
                                                                <img
                                                                    id="target"
                                                                    src={`${BASE_URL_DOWNLOAD}${item?.url}`}
                                                                    style={{ width: 300, height: 200, objectFit: "cover" }}
                                                                    alt=""
                                                                />
                                                            </div>
                                                        )
                                                    })}
                                                </>
                                            }

                                        </div>
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