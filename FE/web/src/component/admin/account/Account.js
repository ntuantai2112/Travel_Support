import React, { useEffect, useState } from 'react';
import Pagination from "react-js-pagination";
import API from '../../../lib/API';
import Item from './Item';
import ModalAdd from './ModalAdd';
export default function Account() {
    const [data, setData] = useState();
    const [activePage, setActivePage] = useState(1);
    let [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [roleName, setRoleName] = useState("");
    let [show, setShow] = useState(false);
    const handleShow = () => {
        setShow(true)
    }
    const handleClose = () => {
        setShow(false)
    }
    useEffect(() => {
        search()
    }, [])
    useEffect(() => {
        search()
    }, [activePage])
    let search = async () => {
        let path = `/admin/account/search?name=${name}&email=${email}&role=${roleName}&page=${activePage - 1}`;
        let resp = await API.authorizedJSONGET(path);
        if (resp.ok) {
            let response = await resp.json();
            setData(response)
        }
    }
    return (
        <>
            <div className="title">
                <h1>Quản lý tài khoản</h1>
            </div>

            <div className="wrapper__table">
                <section className="content-header">
                    <div className="button__add" >
                        <button
                            onClick={() => { setShow(true) }}
                        >
                            <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24">
                                <path fill="currentColor" d="M19,13H13V19H11V13H5V11H11V5H13V11H19V13Z" />
                            </svg>
                        </button>

                    </div>
                </section>
                <ModalAdd search={search} handleClose={handleClose} handleShow={handleShow} show={show} />
                <div className="wrapp_search">
                    <input
                        placeholder="Tên"
                        value={name}
                        onChange={e => setName(e.target.value)} />
                    <input
                        placeholder="Email"
                        value={email}
                        onChange={e => setEmail(e.target.value)} />
                    <select value={roleName}
                        onChange={e => {

                            setRoleName(e.target.value)

                        }}

                    >
                        <option value="">Tất cả</option>
                        <option value={"ROLE_ADMIN"}>ADMIN   </option>
                        <option value={"ROLE_EMPLOYEE"}>EMPLOYEE</option>
                        <option value={"ROLE_CONTENT"}>CONTENT </option>
                        <option value={"ROLE_PARTNER"}>PARTNER </option>
                        <option value={"ROLE_MEMBER"}>MEMBER  </option>
                    </select>
                    <button
                        onClick={() => {
                            search()
                            setActivePage(1)
                        }}
                    >

                        <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24" color="#fff">
                            <path fill="currentColor" d="M9.5,3A6.5,6.5 0 0,1 16,9.5C16,11.11 15.41,12.59 14.44,13.73L14.71,14H15.5L20.5,19L19,20.5L14,15.5V14.71L13.73,14.44C12.59,15.41 11.11,16 9.5,16A6.5,6.5 0 0,1 3,9.5A6.5,6.5 0 0,1 9.5,3M9.5,5C7,5 5,7 5,9.5C5,12 7,14 9.5,14C12,14 14,12 14,9.5C14,7 12,5 9.5,5Z" />
                        </svg>
                    </button>

                </div>
                <div className="box-body">
                    <table id="example1" className="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>STT</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Tên</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Số điện thoại</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Email</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Giới tính</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Ngày sinh</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Quyền</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Avatar</th>

                                <th className="text-center" width="12%">Cài đặt</th>
                            </tr>
                        </thead>
                        {data?.totalElement > 0 ?
                            data?.data?.map((item, idx) => {
                                return (
                                    <Item data={item} key={idx} index={parseInt(5 * (activePage - 1) + idx + 1)} search={search} />
                                )
                            }) :
                            <>
                                <tbody >
                                    <tr >
                                        <td colSpan="5">Không có dữ liệu</td>
                                    </tr>
                                </tbody>
                            </>
                        }
                    </table>
                </div>
                {data?.totalElement > 0 ?
                    <div className="wrapper-paginate">
                        <Pagination
                            activePage={activePage}
                            itemsCountPerPage={5}
                            totalItemsCount={parseInt(data?.totalElement)}
                            pageRangeDisplayed={3}
                            onChange={(item) => setActivePage(item)}
                        />
                    </div> : <></>
                }


            </div>
            {/* <AddStory show={showAddStory} search={search}
                onHide={() => setShowAddStory(false)}></AddStory> */}
        </>
    )
}
