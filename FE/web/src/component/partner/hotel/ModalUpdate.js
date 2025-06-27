import React, { useEffect, useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import { Controller, useForm } from "react-hook-form";
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';
import { amenities, typeHotels } from '../../../utils/amenities';
import LoadingProgress from '../../LoadingProgress';

export default function ModalUpdate({ handleClose, handleShow, show, data, search, images, hotelCode }) {

    const { control, reset, handleSubmit, formState: { errors }, register } = useForm();

    const [fileList, setFileList] = useState();
    const [imageList, setImageList] = useState([]);
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
            if (imageList?.length > 0) {
                let path = '/partner/hotel/update';
                let listPj = Array.from(imageList);
                let data = new FormData();

                for (let i = 0; i < listPj?.length; i++) {
                    data.append("imageList", listPj[i]);
                }
                data.append("address", form?.address);
                data.append("hotelCode", hotelCode);
                data.append("description", form?.description);
                data.append("title", form?.title)
                data.append("type", form?.type)
                data.append("rank", form?.rank)
                data.append("star", form?.star)
                data.append("amenities", arrAmenities)

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
                let path = '/partner/hotel/update';
                // let listPj = Array.from(imageList);
                let data = new FormData();

                // for (let i = 0; i < listPj?.length; i++) {
                //     data.append("imageList", listPj[i]);
                // }
                data.append("hotelCode", hotelCode);
                data.append("address", form?.address);
                data.append("description", form?.description);
                data.append("title", form?.title)
                data.append("type", form?.type)
                data.append("rank", form?.rank)
                data.append("star", form?.star)
                data.append("amenities", arrAmenities)

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
                        <Modal.Title>Cập nhập khách sạn</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {!loading ?
                            <>
                                <div className="menu__item--error" style={{ width: '80vw', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
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
                                                            style={{ width: '50%' }}
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
                                                            style={{ width: '100%', height: 250 }}

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
                                                            style={{ width: '50%' }}
                                                        />

                                                    )}
                                                    name="address"
                                                    defaultValue={data?.address}
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
                                                            {typeHotels?.map((item, index) => {
                                                                return (
                                                                    <option value={item?.value} key={index}>{item?.name}</option>
                                                                )
                                                            })}
                                                        </select>

                                                    )}
                                                    name="type"
                                                    defaultValue={data?.type}
                                                />
                                            </div>
                                        </li>


                                        <li className="menu__item">
                                            <div className="menu__item--title">Đánh giá khách sạn :</div>
                                            <div className="menu__item--input">
                                                <Controller
                                                    control={control}
                                                    render={({ field: { onChange, onBlur, value } }) => (
                                                        <select
                                                            value={value}
                                                            onChange={e => onChange(e.target.value)}
                                                            onBlur={onBlur}
                                                        >
                                                            <option value={1} >1 sao</option>
                                                            <option value={2} >2 sao</option>
                                                            <option value={3} >3 sao</option>
                                                            <option value={4} >4 sao</option>
                                                            <option value={5} >5 sao</option>
                                                        </select>

                                                    )}
                                                    name="star"
                                                    defaultValue={data?.star}
                                                />
                                            </div>
                                        </li>


                                        <li className="menu__item">
                                            <div className="menu__item--title">Phân loại khách sạn :</div>
                                            <div className="menu__item--input">
                                                <Controller
                                                    control={control}
                                                    render={({ field: { onChange, onBlur, value } }) => (
                                                        <select
                                                            value={value}
                                                            onChange={e => onChange(e.target.value)}
                                                            onBlur={onBlur}
                                                        >
                                                            <option value={1} >1</option>
                                                            <option value={2} >2</option>
                                                            <option value={3} >3</option>
                                                            <option value={4} >4</option>
                                                        </select>

                                                    )}
                                                    name="rank"
                                                    defaultValue={data?.rank}
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
                                            {imageList &&
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
                                                })
                                            }
                                            {!imageList && images?.map((item, index) => {
                                                return (
                                                    <div key={index} style={{ margin: 10 }}>
                                                        <img
                                                            
                                                            src={`${BASE_URL_DOWNLOAD}${item?.url}`}
                                                            style={{ width: 300, height: 200, objectFit: "cover" }}
                                                            alt=""
                                                        />
                                                    </div>
                                                )
                                            })}
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