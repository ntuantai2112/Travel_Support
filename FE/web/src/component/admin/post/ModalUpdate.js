import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';

export default function ModalUpdate({ show, handleClose, data, search }) {
    const [post, setPost] = useState();
    let handlePost = () => {
        setPost(data)
    }
    useEffect(() => {
        handlePost()
    }, [data]);

    const [filePath, setFilePath] = useState();
    const [file, setFile] = useState();
    let updatePost = async () => {
        try {
            let path = `/content/post/update?postId=${post?.id}`;
            let formData = new FormData();
            formData.append("title", post?.title);
            formData.append("content", post?.content);
            file && formData.append("multipartFile", file)
            let resp = await API.authorizedFilePut(path, formData);
            if (resp.ok) {
                search()
                handleClose()
            } else {
                alert("Vui lòng kiểm tra lại thông tin")
            }
        } catch (error) {

        }
    }
    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()

            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Chi tiết bài viết</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div style={{ width: '80vw' }}>
                        <ul className="menu">
                            <li className="menu__item">
                                <div className="menu__item--title">Tiêu đề:</div>
                                <div className="menu__item--input">

                                    <input
                                        className=""
                                        value={post?.title}
                                        style={{ width: '100%', padding: 8 }}
                                        onChange={e => {
                                            setPost({
                                                ...post,
                                                title: e.target.value
                                            })
                                        }}
                                    />


                                </div>
                            </li>

                            <li className="menu__item">
                                <div className="menu__item--title">Nội dung:</div>
                                <div className="menu__item--input">

                                    <textarea

                                        style={{ width: '100%', height: 500, padding: 8 }}
                                        value={post?.content}
                                        onChange={e => {
                                            setPost({
                                                ...post,
                                                content: e.target.value
                                            })
                                        }}
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
                    </div>
                </Modal.Body>
                <Modal.Footer>
                <Button variant="success" onClick={() => updatePost()}>
                        Xác nhận
                    </Button>
                    <Button variant="secondary" onClick={() => {
                        handleClose()
                    }}>
                        Đóng
                    </Button>

                </Modal.Footer>


            </Modal>
        </>
    )
}