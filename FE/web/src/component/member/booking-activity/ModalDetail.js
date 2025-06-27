import moment from 'moment';
import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { BASE_URL_DOWNLOAD } from '../../../lib/API';
import { amenities, approveStatus, typeActivity, typeHotels, typeRooms } from '../../../utils/amenities';

export default function ModalDetail({ show, handleClose, data }) {

    let approveStatusName = approveStatus.filter((item) => item.value == data?.status)?.[0]?.name;
    let addressName = typeActivity.filter((item) => item.value == data?.activitiesInfoDTO?.activitiesType)?.[0]?.name;

    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()

            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Chi tiết book</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="detail" style={{ width: '80vw' }}>
                        <label>Vé book phòng</label>
                        <table>
                            <thead>
                                <tr>
                                    <th>Từ ngày</th>
                                    <th>Đến ngày</th>
                                    <th>Người liên hệ</th>
                                    <th>Email</th>
                                    <th>Số điện thoại</th>
                                    <th>Giá</th>
                                    <th>Trạng thái book</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{moment(data?.checkin).format("DD-MM-YYY")}</td>
                                    <td>{moment(data?.checkout).format("DD-MM-YYY")}</td>
                                    <td>{data?.contact?.firstName} {data?.contact?.lastName} </td>
                                    <td>{data?.contact?.email}</td>
                                    <td>{data?.contact?.phone}</td>
                                    <td>{data?.price} VNĐ</td>
                                    <td>{approveStatusName}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br />
                        <label>Khu vui chơi</label>
                        <table>
                            <thead>
                                <tr>
                                    <th>Tên</th>
                                    <th>Mô tả</th>
                                    <th>Giá vé người lớn</th>
                                    <th>Giá vé trẻ con</th>
                                    <th>Địa điểm</th>
                                    <th>Đia chỉ</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{data?.activitiesInfoDTO?.title}</td>
                                    <td>{data?.activitiesInfoDTO?.description}</td>
                                    <td>{data?.activitiesInfoDTO?.adultTicketPrice}</td>
                                    <td>{data?.activitiesInfoDTO?.childTicketPrice}</td>
                                    <td>{addressName}</td>

                                    <td>{data?.activitiesInfoDTO?.address}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br />
                        <label>Album khu vui chơi</label>
                        <div>
                            {data?.activitiesInfoDTO?.imagesList?.map((item, index) => {
                                return (
                                    <img alt="" src={`${BASE_URL_DOWNLOAD}${item}`} style={{ margin: 10, width: 150, height: 'auto', objectFit: 'contain' }} />
                                )
                            })}
                        </div>
                        <label>Trò chơi</label>
                        <table>
                            <thead>
                                <tr>
                                    <th>Tên</th>
                                    <th>Mô tả</th>
                                    <th>Ảnh</th>
                                </tr>
                            </thead>
                            {data?.activitiesInfoDTO?.gameDTOList?.length > 0 ?
                                <tbody>
                                    {data?.activitiesInfoDTO?.gameDTOList?.map((item, index) => {
                                        return (
                                            <tr key={index}>
                                                <td>{item?.name}</td>
                                                <td>{item?.description}</td>
                                                <td>
                                                    <img alt="" src={`${BASE_URL_DOWNLOAD}${item?.image}`} style={{ width: 150, height: 'auto', objectFit: 'contain' }}  />
                                                </td>
                                            </tr>
                                        )
                                    })}

                                </tbody> : <tbody>
                                    <tr>
                                        <td colSpan="3">Chưa có trò chơi nào</td>
                                    </tr>
                                </tbody>
                            }

                        </table>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="success" onClick={() => {
                        handleClose()
                    }}>
                        Đóng
                    </Button>

                </Modal.Footer>


            </Modal>
        </>
    )
}