import React, { useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { Controller, useForm } from "react-hook-form";
import API from '../../../lib/API';
import LoadingProgress from '../../LoadingProgress';

export default function ModalAdd({ show, handleClose, id, search }) {
    const { control, reset, handleSubmit, formState: { errors }, register } = useForm();

    const [message, setMessage] = useState()

    const [filePath, setFilePath] = useState();
    const [file, setFile] = useState();
    const [loading, setLoading] = useState(false)
    let submitHandler = async form => {
        setLoading(true)
        let path = `/content/post/add`;
        let data = new FormData();
        data.append("title", form?.title);
        data.append("content", form?.content);
        data.append("multipartFile", file);
        let resp = await API.authorizedFilePost(path, data);
        if (resp.ok) {
            setLoading(false)
            setFilePath(null)
            search()
            reset(null)
            handleClose()
        } else {
            setLoading(false)
            setMessage("Vui lòng kiểm tra lại thông tin")
        }
    }
    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()
                setMessage(null)
            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Thêm bài viết</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    {!loading ?
                        <div style={{ width: '80vw' }}>
                            <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
                            <ul className="menu">
                                <li className="menu__item">
                                    <div className="menu__item--title">Tiêu đề:</div>
                                    <div className="menu__item--input">
                                        <Controller
                                            control={control}
                                            render={({ field: { onChange, onBlur, value } }) => (
                                                <input
                                                    onBlur={onBlur}
                                                    className=""
                                                    onChange={e => {
                                                        onChange(e.target.value)

                                                    }}
                                                    value={value}
                                                    style={{ width: '100%', padding: 8 }}
                                                    placeholder="Tiêu đề ..."
                                                />

                                            )}
                                            name="title"
                                            rules={{ required: true }}
                                            defaultValue=""
                                        />
                                    </div>
                                    {errors.title?.type == "required" && <span style={{ color: 'red' }}>Trường này không được bỏ trống *</span>}
                                </li>

                                <li className="menu__item">
                                    <div className="menu__item--title">Nội dung:</div>
                                    <div className="menu__item--input">
                                        <Controller
                                            control={control}
                                            render={({ field: { onChange, onBlur, value } }) => (
                                                <textarea
                                                    onBlur={onBlur}
                                                    className=""
                                                    onChange={e => {
                                                        onChange(e.target.value)

                                                    }}
                                                    value={value}
                                                    style={{ width: '100%', height: 500, padding: 8 }}
                                                    placeholder="Nội dung ..."
                                                />

                                            )}
                                            rules={{ required: true }}
                                            name="content"
                                            defaultValue=""
                                        />
                                    </div>
                                    {errors.content?.type == "required" && <span style={{ color: 'red' }}>Trường này không được bỏ trống *</span>}
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
                        </div> : <LoadingProgress />
                    }
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="success" onClick={handleSubmit(submitHandler)}>
                        Xác nhận
                    </Button>
                    <Button variant="secondary"
                        onClick={() => {
                            handleClose()
                            setMessage(null)
                        }}
                    >
                        Hủy

                    </Button>
                </Modal.Footer>


            </Modal>
        </>
    )
}