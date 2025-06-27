import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { Controller, useForm } from "react-hook-form";
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';
import moment from 'moment'
export default function ModalUpdate({ show, handleClose, user, search }) {
    const { control, reset, handleSubmit, formState: { errors }, register } = useForm();

    const [message, setMessage] = useState();
    const [account, setAccount] = useState(user);
    const [filePath, setFilePath] = useState();
    const [file, setFile] = useState();
    const [gender, setGender] = useState(true);
    let handleGender = () => {
        if (user?.gender == "nam") {
            setGender(true)
        } else {
            setGender(false)
        }
    }
    let handleAccount = () => {
        setAccount(user)
    }

    useEffect(() => {
        handleAccount()
        handleGender()
    }, [user])

    let submitHandler = async form => {
        console.log(form)
        let path = `/member/update/profile`;
        let data = new FormData();
        data.append("name", form?.name);
        data.append("dob", form?.dob);
        data.append("phone", form?.phone);
        data.append("email", account?.email);
        data.append("gender", gender);
        file && data.append("multipartFile", file)
        let resp = await API.authorizedFilePost(path, data);
        if (resp.ok) {
            search()
            reset(null)
            handleClose()
        } else {
            let response = await resp.json();
            console.log(response)
            setMessage("Vui lòng kiểm tra lại thông tin")
        }
    }
    console.log(user)
    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()
                setMessage(null)
            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Cập nhật tài khoản</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="menu__item--error" style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}> {message && <span>{message}</span>}</div>
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
                                            onChange={e => {
                                                onChange(e.target.value)
                                                setAccount({
                                                    ...account,
                                                    name: e.target.value
                                                })
                                            }}
                                            value={account?.name}
                                        />

                                    )}
                                    name="name"
                                    defaultValue={account?.name}
                                />
                            </div>
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Ngày sinh:</div>
                            <div className="menu__item--input">
                                <Controller
                                    control={control}
                                    render={({ field: { onChange, onBlur, value } }) => (
                                        <input
                                            onBlur={onBlur}
                                            type="date"
                                            className=""
                                            onChange={e => {
                                                onChange(e.target.value)
                                                setAccount({
                                                    ...account,
                                                    dob: e.target.value
                                                })
                                            }}
                                            value={moment(account?.dob).format("YYYY-MM-DD")}
                                        />

                                    )}
                                    name="dob"
                                    defaultValue={account?.dob}
                                />
                            </div>
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Giới tính:</div>
                            <div className="menu__item--input">

                                <div style={{ display: 'flex', alignItems: 'center', width: 300 }}>
                                    <div style={{ display: 'flex', alignItems: 'center' }}>
                                        <input type="radio" style={{ width: 50 }}
                                            checked={gender}
                                            onClick={() => setGender(true)}
                                        /> Nam</div>
                                    <div style={{ display: 'flex', alignItems: 'center' }}><input type="radio" name="gender" style={{ width: 50 }}
                                        checked={!gender}
                                        onClick={() => setGender(false)}
                                    />Nữ</div>
                                </div>



                            </div>
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Số điện thoại:</div>
                            <div className="menu__item--input">
                                <Controller
                                    control={control}
                                    render={({ field: { onChange, onBlur, value } }) => (
                                        <input
                                            onBlur={onBlur}
                                            className=""
                                            onChange={e => {
                                                onChange(e.target.value)
                                                setAccount({
                                                    ...account,
                                                    phone: e.target.value
                                                })
                                            }}
                                            value={account?.phone}
                                        />

                                    )}
                                    name="phone"
                                    defaultValue={account?.phone}
                                />
                            </div>
                        </li>
                        <li className="menu__item">
                            <div className="menu__item--title">Avatar:</div>
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
                                        src={`${BASE_URL_DOWNLOAD}${account?.image}`}
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