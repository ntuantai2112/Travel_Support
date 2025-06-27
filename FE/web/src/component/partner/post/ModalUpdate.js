import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { Controller, useForm } from "react-hook-form";
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';

export default function ModalUpdate({ show, handleClose, id, search }) {
    const { control, reset, handleSubmit, formState: { errors }, register } = useForm();

    const [message, setMessage] = useState()
    const [post, setPost] = useState();
    const [filePath, setFilePath] = useState();
    const [file, setFile] = useState();

    useEffect(() => {
        fetchData()
    }, [])
    useEffect(() => {
        fetchData()
    }, [id])
    let fetchData = async () => {
        let path = `/content/post/detail?postId=${id}`;
        let resp = await API.authorizedJSONGET(path);
        if (resp.ok) {
            let response = await resp.json();
            setPost(response)
        }
    }



    let submitHandler = async form => {
        let path = `/content/post/update?postId=${id}`;
        let data = new FormData();
        data.append("title", form?.title);
        data.append("content", form?.content);
        data.append("multipartFile", file)
        let resp = await API.authorizedFilePut(path, data);
        if (resp.ok) {
            search()
            reset(null)
            handleClose()
        } else {
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
                    <Modal.Title>Sửa bài viết</Modal.Title>
                </Modal.Header>

                <Modal.Body>
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
                                                setPost({
                                                    ...post,
                                                    title: e.target.value
                                                })
                                            }}
                                            value={post?.title}
                                        />

                                    )}
                                    name="title"
                                    defaultValue={post?.title}
                                />
                            </div>
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
                                                setPost({
                                                    ...post,
                                                    content: e.target.value
                                                })
                                            }}
                                            style={{width:'100%',height:500}}
                                            value={post?.content}
                                        />

                                    )}
                                    name="content"
                                    defaultValue={post?.content}
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
                                <>
                                    <img
                                        id="target"
                                        src={`${BASE_URL_DOWNLOAD}${post?.image}`}
                                        style={{ width: 300, height: 200, objectFit: "cover" }}
                                        alt=""
                                    />
                                </>
                            )}
                        </li>
                    </ul>
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