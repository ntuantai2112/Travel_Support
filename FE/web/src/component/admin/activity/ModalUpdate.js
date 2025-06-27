import moment from 'moment';
import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { Controller, useForm } from "react-hook-form";
import API from '../../../lib/API';
import { statusHotel } from '../../../utils/amenities';

export default function ModalUpdate({ show, handleClose, hotelCode, search, statusName }) {
   
   
    
    let [status, setStatus] = useState();
    let hanelStatus = () => {
        setStatus(statusName)
    }
    useEffect(() => {
        hanelStatus()
    }, [statusName])
    let upload = async () => {
        try {
            let path = '/employee/activities/approve';
            let resp = await API.authorizedJSONPost(path, {
                activitiesCode: hotelCode,
                status: status
            });
            if (resp.ok) {
                search();
                handleClose()
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
                    <Modal.Title>Cập nhật trạng thái khu vui chơi</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <label>Trạng thái:</label>
                    <select
                        value={status}
                        onChange={e => setStatus(e.target.value)}
                    >
                        {statusHotel?.map((item, index) => {
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