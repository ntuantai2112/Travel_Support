import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';
import { typeRestaurant } from '../../../utils/amenities';

export default function ModalDetail({ show, handleClose, slug, restaurantType, feature }) {
    const roleId = localStorage.getItem("roleId");
    let [restaurants, setRestaurants] = useState();
    let [menus, setMenus] = useState();
    let typeRestaurantName = typeRestaurant.filter((item) => item.value == restaurantType)?.[0]?.name;
    let search = async () => {
        let path = `/restaurant/${slug}`;
        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();
            setRestaurants(response?.data)
            setMenus(response?.data?.menus)
        }
    }

    useEffect(() => {
        search()
    }, [slug])
    let deleteItem = async (id) => {
        let message = window.confirm("Bạn chắc chắn muốn xóa món ăn này?");
        if (message) {
            let path = `/partner/restaurant/menu/delete/${id}`;
            let resp = await API.authorizedJSONPost(path);
            if (resp.ok) {
                search()
            }
        }

    }
    return (
        <>

            <Modal show={show} onHide={() => {
                handleClose()

            }} animation={false} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Chi tiết nhà hàng</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="detail">
                        <table>
                            <thead>
                                <tr>
                                    <th>Tên</th>
                                    <th>Mô tả</th>
                                    <th>Địa chỉ</th>
                                    <th>Loại nhà hàng</th>
                                    <th>Tiện ích</th>


                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{restaurants?.title}</td>
                                    <td style={{ width: 200 }}>{restaurants?.description}</td>
                                    <td>{restaurants?.address}</td>
                                    <td>{typeRestaurantName}</td>
                                    <td>
                                        {feature?.map((item, index) => {
                                            return (
                                                <p key={index}>{item?.name}</p>
                                            )
                                        })}
                                    </td>

                                </tr>
                            </tbody>
                        </table>
                        <br />
                        <label>
                            Album nhà hàng
                        </label>
                        <div>
                            {restaurants?.images?.map((item, index) => {
                                return (
                                    <img src={`${BASE_URL_DOWNLOAD}${item?.url}`} key={index} style={{ width: 50, height: 50, borderRadius: 25 }} />
                                )
                            })}
                        </div>
                        <br />
                        <hr />
                        <label>Thực đơn nhà hàng</label>
                        {menus?.length > 0 ?
                            <table>
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Tên</th>
                                        <th>Mô tả</th>
                                        <th>Giá</th>
                                        <th>Ảnh</th>
                                        {roleId == 1 &&
                                            <th>Cài đặt</th>
                                        }

                                    </tr>
                                </thead>
                                <tbody>
                                    {menus?.map((item, idx) => {
                                        return (
                                            <tr key={idx}>
                                                <td>{idx + 1}</td>
                                                <td>{item?.name}</td>
                                                <td>{item?.description}</td>
                                                <td>{item?.price} {item?.currency}</td>
                                                <td><img src={`${BASE_URL_DOWNLOAD}${item?.image}`} style={{ width: 50, height: 50, borderRadius: 25 }} /></td>
                                                {roleId == 1 &&
                                                    <td style={{ verticalAlign: 'middle' }} className="btn__setting">


                                                        {/* <button
                                                        // onClick={handleShowUpdate}
                                                        >
                                                            <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24" color="#00a6d9">
                                                                <path fill="currentColor" d="M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12H20A8,8 0 0,1 12,20A8,8 0 0,1 4,12A8,8 0 0,1 12,4V2M18.78,3C18.61,3 18.43,3.07 18.3,3.2L17.08,4.41L19.58,6.91L20.8,5.7C21.06,5.44 21.06,5 20.8,4.75L19.25,3.2C19.12,3.07 18.95,3 18.78,3M16.37,5.12L9,12.5V15H11.5L18.87,7.62L16.37,5.12Z" />
                                                            </svg>
                                                        </button> */}
                                                        <button
                                                            onClick={() => deleteItem(item?.id)}
                                                        >
                                                            <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                                                                <path fill="currentColor" d="M9,3V4H4V6H5V19A2,2 0 0,0 7,21H17A2,2 0 0,0 19,19V6H20V4H15V3H9M7,6H17V19H7V6M9,8V17H11V8H9M13,8V17H15V8H13Z" />
                                                            </svg>
                                                        </button>


                                                    </td>
                                                }
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