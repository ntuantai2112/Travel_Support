import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import API from '../../../lib/API';
import { approveStatus } from '../../../utils/amenities';

export default function ModalUpdate({ show, handleClose, bookingId, search, statusName }) {
    
  
  
    let [status, setStatus] = useState(statusName);
    let hanelStatus = () => {
        setStatus(statusName)
    }
    useEffect(() => {
        hanelStatus()
    }, [statusName])
    let upload = async () => {
        try {
            let path = '/partner/hotel/booking/approve';
            let resp = await API.authorizedJSONPost(path, {
                bookingId: bookingId,
                status: status
            });
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