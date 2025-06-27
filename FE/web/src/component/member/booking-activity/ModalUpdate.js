import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import API from '../../../lib/API';
import { approveStatus } from '../../../utils/amenities';

export default function ModalUpdate({ show, handleClose, hotelCode, search, statusName ,bookingId}) {

    let [status, setStatus] = useState();
    let hanelStatus = () => {
        statusName == "Hủy bỏ" ? setStatus("BOOKING_REJECT") : setStatus("BOOKING_PENDING")

    }
    useEffect(() => {
        hanelStatus()
    }, [statusName])
    let upload = async () => {
        try {
            let path = '/member/booking/approve/activities';
            let objReq = {
                bookingId: bookingId,
                status: status
            }
            console.log(path)
            console.log(objReq)
            let resp = await API.authorizedJSONPost(path, objReq);
            if (resp.ok) {
                search();
                handleClose()
                console.log("ok")
            }
        } catch (error) {
            console.log(error)
        }
    }

    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()

            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Cập nhật trạng thái book</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <label>Trạng thái:</label>
                    <select
                        onChange={e => setStatus(e.target.value)}
                        value={status}
                    >

                        <option value="BOOKING_PENDING" >Đang chờ phê duyệt</option>
                        <option value="BOOKING_REJECT" >Hủy bỏ</option>
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