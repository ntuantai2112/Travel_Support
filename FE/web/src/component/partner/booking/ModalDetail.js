import moment from 'moment';
import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import { BASE_URL_DOWNLOAD } from '../../../lib/API';
import { amenities, approveStatus, typeHotels, typeRooms } from '../../../utils/amenities';

export default function ModalDetail({ show, handleClose, data }) {

    let approveStatusName = approveStatus.filter((item) => item.value == data?.status)?.[0]?.name;

    let typeHotelName = typeHotels.filter((item) => item.value == data?.hotelInfoDTO?.hotelType)?.[0]?.name;

    let typeRoomName = typeRooms.filter((item) => item.value == data?.packageInfo?.roomType)?.[0]?.name;
    const [arr, setArr] = useState([]);
    let arrAmenities = data?.hotelInfoDTO?.amenities;

    let handleArr = () => {
        let x = [];
        for (let i = 0; i < arrAmenities?.length; i++) {
            let newArr = amenities?.filter(item => item.id == arrAmenities[i])?.[0];
            x.push(newArr)
        }
        setArr(x);
    }

    useEffect(() => {
        handleArr()
    }, [data])
    const currencyFormat = (num) => {
        return num?.replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.') + ' VNĐ'
    }
    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()

            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Chi tiết book</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="detail">
                        <label>Vé book phòng</label>
                        <table>
                            <thead>
                                <tr>
                                    <th>Từ ngày</th>
                                    <th>Đến ngày</th>
                                    <th>Số ngày ở</th>
                                    <th>Người liên hệ</th>
                                    <th>Email</th>
                                    <th>Số điện thoại</th>
                                    <th>Giá</th>
                                    <th>Trạng thái book</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{moment(`${data.checkin}`,"YYYYMMDD").format("DD-MM-YYYY")}</td>
                                    <td>{moment(`${data.checkout}`,"YYYYMMDD").format("DD-MM-YYYY")}</td>
                                    <td>{data?.totalNights}</td>
                                    <td>{data?.contact?.firstName} {data?.contact?.lastName} </td>
                                    <td>{data?.contact?.email}</td>
                                    <td>{data?.contact?.phone}</td>
                                    <td>{currencyFormat(data?.price?.toString())}</td>
                                    <td>{approveStatusName}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br />
                        <label>Khách sạn</label>
                        <table>
                            <thead>
                                <tr>
                                    <th>Tên</th>
                                    <th>Mô tả</th>
                                    <th>Loại khách sạn</th>
                                    {/* <th>Mức khách sạn</th> */}
                                    <th>Đánh giá</th>
                                    <th>Tiện ích</th>
                                    <th>Đia chỉ</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{data?.hotelInfoDTO?.title}</td>
                                    <td>{data?.hotelInfoDTO?.description}</td>
                                    <td>{typeHotelName}</td>
                                    {/* <td>{data?.hotelInfoDTO?.rank}</td> */}
                                    <td>{data?.hotelInfoDTO?.star} Sao</td>
                                    <td>{arr?.map((item, index) => {
                                        return (
                                            <p key={index}>{item?.name}</p>
                                        )
                                    })}</td>
                                    <td>{data?.hotelInfoDTO?.address}</td>
                                </tr>
                            </tbody>
                        </table>
                        <br />
                        <label>Phòng</label>
                        <table>
                            <thead>
                                <tr>
                                    <th>Tên</th>
                                    <th>Loại phòng</th>
                                    <th>Giá</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{data?.packageInfo?.hotelOptionCode}</td>
                                    <td>{typeRoomName}</td>
                                    <td>{currencyFormat(data?.packageInfo?.price?.toString())} </td>
                                </tr>
                            </tbody>
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