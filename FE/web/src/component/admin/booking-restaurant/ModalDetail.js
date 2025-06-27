import moment from 'moment';
import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { BASE_URL_DOWNLOAD } from '../../../lib/API';
import { amenities, approveStatus, typeActivity, typeHotels, typeRestaurant, typeRooms } from '../../../utils/amenities';

export default function ModalDetail({ show, handleClose, data }) {

    let approveStatusName = approveStatus.filter((item) => item.value == data?.status)?.[0]?.name;
    let addressName = typeActivity.filter((item) => item.value == data?.activitiesInfoDTO?.activitiesType)?.[0]?.name;
    let typeRestaurantName = typeRestaurant.filter((item) => item.value == data?.restaurantInfoDTO?.restaurantType)?.[0]?.name;
    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()

            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Chi tiết đặt nhà hàng</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="detail" style={{ width: '80vw' }}>
                        <label>Vé đặt</label>
                        <table>
                            <thead>
                                <tr>
                                    <th>Ngày đặt</th>
                                    <th>Người liên hệ</th>
                                    <th>Email</th>
                                    <th>Số điện thoại</th>

                                    <th>Trạng thái book</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{data?.checkinTime} {moment(data?.checkinDay, "YYYYMMDD").format("DD-MM-YYYY")}</td>
                                    <td>{data?.contact?.firstName} {data?.contact?.lastName} </td>
                                    <td>{data?.contact?.email}</td>
                                    <td>{data?.contact?.phone}</td>

                                    <td>{approveStatusName}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br />
                        <label>Nhà hàng</label>
                        <table>
                            <thead>
                                <tr>
                                    <th>Tên</th>
                                    <th>Mô tả</th>
                                    <th>Địa chỉ</th>
                                    <th>Loại nhà hàng</th>


                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{data?.restaurantInfoDTO?.title}</td>
                                    <td style={{ width: 200 }}>{data?.restaurantInfoDTO?.description}</td>
                                    <td>{data?.restaurantInfoDTO?.address}</td>
                                    <td>{typeRestaurantName}</td>


                                </tr>
                            </tbody>
                        </table>
                        <br />
                        <label>Album nhà hàng</label>
                        <div>
                            {data?.restaurantInfoDTO?.imagesList?.map((item, index) => {
                                return (
                                    <img alt="" src={`${BASE_URL_DOWNLOAD}${item}`} style={{ margin: 10, width: 150, height: 'auto', objectFit: 'contain' }} />
                                )
                            })}
                        </div>
                        <label>Thực đơn nhà hàng</label>
                        {data?.restaurantInfoDTO?.menuDTOList?.length > 0 ?
                            <table>
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Tên</th>
                                        <th>Mô tả</th>
                                        <th>Giá</th>
                                        <th>Ảnh</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {data?.restaurantInfoDTO?.menuDTOList?.map((item, idx) => {
                                        return (
                                            <tr key={idx}>
                                                <td>{idx + 1}</td>
                                                <td>{item?.name}</td>
                                                <td>{item?.description}</td>
                                                <td>{item?.price} {item?.currency}</td>
                                                <td><img src={`${BASE_URL_DOWNLOAD}${item?.image}`} style={{ width: 50, height: 50, borderRadius: 25 }} /></td>
                                            </tr>
                                        )
                                    })}
                                </tbody>
                            </table>
                            : <p> Chưa có thực đơn! </p>

                        }
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