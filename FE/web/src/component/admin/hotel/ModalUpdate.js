import moment from 'moment';
import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { Controller, useForm } from "react-hook-form";
import API from '../../../lib/API';

export default function ModalUpdate({ show, handleClose, hotelCode, search, statusName }) {
    let approveStatus = [
        { value: "PENDING", name: "Đang chờ phê duyệt" },
        { value: "APPROVED", name: "Đã phê duyệt" },
        { value: "DECLINES", name: "Hủy bỏ" }
    ]
    let approveStatusValue = approveStatus.filter((item) => item.name == statusName)?.[0]?.value;
  
    let [status, setStatus] = useState(approveStatusValue);
    let hanelStatus = () => {
        setStatus(approveStatusValue)
    }
    useEffect(() => {
        hanelStatus()
    }, [statusName])
    let upload = async () => {
        try {
            let path = '/employee/hotel/approve';
            let resp = await API.authorizedJSONPost(path, {
                hotelCode: hotelCode,
                status: status
            });
            if (resp.ok) {
                search();
                handleClose()
                console.log("ok")
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
                    <Modal.Title>Cập nhật trạng thái khách sạn</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <label>Trạng thái:</label>
                    <select
                        value={status}
                        onChange={e => setStatus(e.target.value)}
                    >
                        {approveStatus?.map((item, index) => {
                            return (
                                <option key={index} value={item.value}>{item?.name}</option>
                            )
                        })}
                    </select>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="success" onClick={() => upload()}>
                        Xác nhận
                    </Button>
                    <Button variant="secondary"
                        onClick={() => {
                            handleClose()

                        }}
                    >
                        Hủy

                    </Button>
                </Modal.Footer>


            </Modal>
        </>
    )
}