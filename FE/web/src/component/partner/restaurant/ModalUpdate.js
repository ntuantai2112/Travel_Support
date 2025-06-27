import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { Controller, useForm } from "react-hook-form";
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';
import { amenitiesFeatures } from '../../../utils/amenities';
import LoadingProgress from '../../LoadingProgress';

export default function ModalUpdate({ handleClose, handleShow, show, data, search, images, restaurantCode, feature }) {

    const { control, reset, handleSubmit, formState: { errors }, register } = useForm();
    console.log(restaurantCode)
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
        let img = [];
        feature?.map((item) => {
            resp.push(item?.key)
        })
        images?.map((item) => {
            img.push(item?.url)
        })
        setArrAmenities(resp);


    }

    let onSubmit = async (form) => {
        try {
            setLoading(true)
            if (imageList?.length > 0) {
                let path = '/partner/restaurant/update';
                let listPj = Array.from(imageList);
                let data = new FormData();

                for (let i = 0; i < listPj?.length; i++) {
                    data.append("imageList", listPj[i]);
                }
                data.append("address", form?.address);
                data.append("restaurantCode", restaurantCode);
                data.append("description", form?.description);
                data.append("title", form?.title)


                data.append("feature", arrAmenities)

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
                let path = '/partner/restaurant/update';
                // let listPj = Array.from(imageList);
                let data = new FormData();

                // for (let i = 0; i < listPj?.length; i++) {
                //     data.append("imageList", listPj[i]);
                // }
                data.append("address", form?.address);
                data.append("restaurantCode", restaurantCode);
                data.append("description", form?.description);
                data.append("title", form?.title)


                data.append("feature", arrAmenities)


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
                        <Modal.Title>Cập nhập nhà hàng</Modal.Title>
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
                                            <div className="menu__item--title">Tiện ích :</div>
                                            <div className="menu__item--input">
                                                {amenitiesFeatures?.map((item, index) => {
                                                    return (
                                                        <div key={index}>
                                                            <input type="checkbox" value={item?.id}
                                                                onChange={e => handleChange(e.target.value)}
                                                                defaultChecked={arrAmenities.includes(String(item.id))}
                                                            />{item?.name}
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
                                            {fileList && fileList?.map((item, idx) => {
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
                                            })}
                                            {!fileList && images?.map((item, index) => {
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