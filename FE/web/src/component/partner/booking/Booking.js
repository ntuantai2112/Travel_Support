import React, { useEffect, useState } from 'react';
import Pagination from "react-js-pagination";
import API from '../../../lib/API';
import Item from './Item';
export default function Booking() {
    const [data, setData] = useState();
    const [activePage, setActivePage] = useState(1);
    let [name, setName] = useState("");
  
    useEffect(() => {
        search()
    }, [])
    useEffect(() => {
        search()
    }, [activePage])
    let search = async () => {
        let path = `/partner/hotel/booking/list?page=${activePage-1}&perPage=5`;
        let resp = await API.authorizedJSONGET(path);
        if (resp.ok) {
            let response = await resp.json();
            setData(response)
        }
    }
    return (
        <>
            <div className="title">
                <h1>Book</h1>
            </div>

            <div className="wrapper__table">
                
                {/* <div className="wrapp_search">
                    <input
                        placeholder="Tiêu đề"
                        value={name}
                        onChange={e => setName(e.target.value)} />
                    <button
                        onClick={() => {
                            search()
                            setActivePage(1)
                        }}
                    > <svg style={{ width: 24, height: 24 }} viewBox="0 0 24 24" color="#fff">
                            <path fill="currentColor" d="M9.5,3A6.5,6.5 0 0,1 16,9.5C16,11.11 15.41,12.59 14.44,13.73L14.71,14H15.5L20.5,19L19,20.5L14,15.5V14.71L13.73,14.44C12.59,15.41 11.11,16 9.5,16A6.5,6.5 0 0,1 3,9.5A6.5,6.5 0 0,1 9.5,3M9.5,5C7,5 5,7 5,9.5C5,12 7,14 9.5,14C12,14 14,12 14,9.5C14,7 12,5 9.5,5Z" />
                        </svg></button>

                </div> */}
                <div className="box-body">
                    <table id="example1" className="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>STT</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Khách sạn</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Loại khách sạn</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Giá</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Phòng</th>
                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Người liên hệ</th>

                                <th className="text-center" style={{ verticalAlign: 'middle' }}>Trạng thái</th>
                                {/* <th className="text-center" style={{ verticalAlign: 'middle' }}>Giá phòng thấp nhất</th> */}
                                <th className="text-center" width="12%">Cài đặt</th>
                            </tr>
                        </thead>
                        {data?.data.length > 0 ?
                            data?.data?.map((item, idx) => {
                                return (
                                    <Item data={item} key={idx} index={parseInt(5 * (activePage - 1) + idx + 1)} search={search} />
                                )
                            }) :
                            <>
                                <tbody >
                                    <tr >
                                        <td colSpan="8">Không có dữ liệu</td>
                                    </tr>
                                </tbody>
                            </>
                        }
                    </table>
                </div>
               

                {data?.meta?.total > 0 ?
                    <div className="wrapper-paginate">
                        <Pagination
                            activePage={activePage}
                            itemsCountPerPage={5}
                            totalItemsCount={parseInt(data?.meta?.total)}
                            pageRangeDisplayed={3}
                            onChange={(item) => setActivePage(item)}
                        />
                    </div> : <></>
                }
            </div>

        </>
    )
}
