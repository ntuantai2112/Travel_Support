import React, { useEffect, useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import { useForm } from "react-hook-form";
import API from '../../../lib/API';
import { amenities, typeActivity } from '../../../utils/amenities';
import LoadingProgress from '../../LoadingProgress';

export default function ModalAdd({ show, handleClose, handleShow, search }) {

    const { register, handleSubmit, watch, formState: { errors }, reset } = useForm();
    const [fileList, setFileList] = useState();
    const [imageList, setImageList] = useState();
    const [message, setMessage] = useState();
    const [loading, setLoading] = useState(false)
   
   
    const [type,setType] = useState("IN_DOOR")
    let [arrAmenities, setArrAmenities] = useState([])

    let onSubmit = async (form) => {
        try {
            setLoading(true)
        console.log(form)
        let path = '/partner/activities/upload';
        let listPj = Array.from(imageList);
        let data = new FormData();
        for (let i = 0; i < listPj?.length; i++) {
            data.append("imageList", listPj[i]);
        }
        data.append("address", form?.address);
        data.append("description", form?.description);
        data.append("title", form?.title)
        data.append("type", type)
        data.append("duration", form?.duration)
        data.append("childTicketPrice", form?.childTicketPrice)
        data.append("adultTicketPrice", form?.adultTicketPrice)
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
                        <Modal.Title>Thêm khu vui chơi</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {!loading ?
                            <div style={{width:'80vw'}}>
                                <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
                                <div>
                                    <ul className="menu">
                                        <li className="menu__item">
                                            <div className="menu__item--title">Tên:</div>
                                            <div className="menu__item--input">
                                                <input type="text"
                                                    {...register("title")}
                                                />
                                            </div>

                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Mô tả:</div>
                                            <div className="menu__item--input">
                                                <textarea type="text"
                                                    {...register("description")}
                                                    style={{width:'100%',height:200}}
                                                />
                                            </div>

                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Địa chỉ:</div>
                                            <div className="menu__item--input">
                                                <input type="text"
                                                    {...register("address")}
                                                />
                                            </div>

                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Địa điểm:</div>
                                            <div className="menu__item--input">
                                                <select
                                                    {...register("type")}
                                                    onChange={e => {
                                                        setType(e.target.value)
                                                    }}
                                                >
                                                    {typeActivity?.map((item, index) => {
                                                        return (
                                                            <option value={item?.value} key={index}>{item?.name}</option>
                                                        )
                                                    })}
                                                </select>
                                            </div>

                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Giá vé trẻ con:</div>
                                            <div className="menu__item--input">
                                                <input type="text"
                                                    {...register("childTicketPrice")}
                                                />
                                            </div>

                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Giá vé người lớn:</div>
                                            <div className="menu__item--input">
                                                <input type="text"
                                                    {...register("adultTicketPrice")}
                                                />
                                            </div>

                                        </li>
                                        <li className="menu__item">
                                            <div className="menu__item--title">Thời gian chơi:</div>
                                            <div className="menu__item--input">
                                                <input type="text"
                                                    {...register("duration")}
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
                                            {fileList?.map((item, idx) => {
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
                                        </div>
                                    </ul>
                                    <br />
                                </div>
                            </div> : <LoadingProgress />
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