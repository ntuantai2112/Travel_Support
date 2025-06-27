import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import API, { BASE_URL_DOWNLOAD } from '../../../lib/API';
import Update from './Update';

export default function ModalDetail({ show, handleClose, slug, isUpload }) {

    let [activities, setActivities] = useState();
    let [games, setGames] = useState();

    let search = async () => {
        let path = `/activities/${slug}`;
        let resp = await API.anonymousJSONGETHOTEL(path);
        if (resp.ok) {
            let response = await resp.json();
            setActivities(response?.data)
            setGames(response?.data?.games)
        }
    }

    useEffect(() => {
        search()
    }, [slug, isUpload])
    let deleteItem = async (id) => {
        let message = window.confirm("Bạn chắc chắn muốn xóa trò chơi này?");
        if (message) {
            let path = `/partner/activities/game/delete/${id}`;
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
                    <Modal.Title>Chi tiết khu vui chơi</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="detail">
                        <table>
                            <thead>
                                <tr>
                                    <th>Tên</th>
                                    <th>Mô tả</th>
                                    <th>Địa chỉ</th>
                                    <th>Giá vé trẻ con</th>
                                    <th>Giá vé người lớn</th>


                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{activities?.title}</td>
                                    <td style={{ width: 200 }}>{activities?.description}</td>
                                    <td>{activities?.address}</td>
                                    <td>{activities?.childPrice}</td>
                                    <td>{activities?.adultPrice}</td>

                                </tr>
                            </tbody>
                        </table>
                        <br />
                        <label>
                            Album khu vui chơi
                        </label>
                        <div>
                            {activities?.images?.map((item, index) => {
                                return (
                                    <img src={`${BASE_URL_DOWNLOAD}${item?.url}`} key={index} style={{ width: 50, height: 50, borderRadius: 25 }} />
                                )
                            })}
                        </div>
                        <br />
                        <hr />
                        <label>Trò chơi khu vui chơi</label>
                        {games?.length > 0 ?
                            <table>
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Tên</th>
                                        <th>Mô tả</th>
                                        <th>Ảnh</th>
                                        <th>Cài đặt</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {games?.map((item, idx) => {
                                        return (
                                            <Item idx={idx} deleteItem={deleteItem} key={idx} item={item}
                                                search={search} activities={activities}
                                            />
                                        )
                                    })}
                                </tbody>
                            </table>
                            : <p> Chưa có trò chơi nào được thêm </p>

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

function Item({ idx, deleteItem, item, search, activities }) {
    const [showRoom, setShowRoom] = useState(false);
    let handleCloseShowRoom = () => {
        setShowRoom(false)
    }
    return (
        <>
            <tr >
                <td>{idx + 1}</td>
                <td>{item?.name}</td>
                <td>{item?.description}</td>
                <td><img src={`${BASE_URL_DOWNLOAD}${item?.image}`} style={{ width: 50, height: 50, borderRadius: 25 }} /></td>
                <td style={{ verticalAlign: 'middle' }} className="btn__setting">
                    <Update gameId={item?.id} show={showRoom} handleClose={handleCloseShowRoom} search={search} data={item} activitiesCode={activities?.code} image={item?.image} />

                    <button
                        onClick={() => setShowRoom(true)}
                    >
                        <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24" color="#00a6d9">
                            <path fill="currentColor" d="M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12H20A8,8 0 0,1 12,20A8,8 0 0,1 4,12A8,8 0 0,1 12,4V2M18.78,3C18.61,3 18.43,3.07 18.3,3.2L17.08,4.41L19.58,6.91L20.8,5.7C21.06,5.44 21.06,5 20.8,4.75L19.25,3.2C19.12,3.07 18.95,3 18.78,3M16.37,5.12L9,12.5V15H11.5L18.87,7.62L16.37,5.12Z" />
                        </svg>
                    </button>
                    <button
                        onClick={() => deleteItem(item?.id)}
                    >
                        <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                            <path fill="currentColor" d="M9,3V4H4V6H5V19A2,2 0 0,0 7,21H17A2,2 0 0,0 19,19V6H20V4H15V3H9M7,6H17V19H7V6M9,8V17H11V8H9M13,8V17H15V8H13Z" />
                        </svg>
                    </button>


                </td>
            </tr>
        </>
    )
}