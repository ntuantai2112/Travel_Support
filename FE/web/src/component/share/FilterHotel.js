import React from 'react';
import '../../style/filter.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSortDown } from '@fortawesome/free-solid-svg-icons'
import { rank, typeHotels } from '../../utils/amenities';

export default function FilterHotel(props) {

    return (
        <>
            <div className="wrapper__filter--hotel">
                <div className=" wrapper__covid">
                    <h4 className="title">Covid<sup>19</sup></h4>
                    <i className="text">Khu vực không phải cách ly y tế</i>
                </div>
                <div className="wrapper__main--filter">
                    <div className="rank__hotel">
                        <h5 className="title">Hạng Khách Sạn</h5>
                        <ul className="list__filter">
                            <li className="item" >
                                <div className="check-box">
                                    <input type="radio" name="rank" value={""} defaultChecked />
                                    <p className="content">Mặc định</p>
                                </div>
                                {/* <div className="amount">
                                            <p className="number"></p>
                                        </div> */}
                            </li>
                            {rank?.map((item, idx) => {
                                return (
                                    <li className="item" key={idx}>
                                        <div className="check-box">
                                            <input type="radio" name="rank" value={item?.value} />
                                            <p className="content">{item?.name}</p>
                                        </div>
                                        {/* <div className="amount">
                                            <p className="number"></p>
                                        </div> */}
                                    </li>
                                )
                            })}
                        </ul>
                    </div>
                    <hr />
                    <div className="type__hotel">
                        <h5 className="title">Loại</h5>
                        <ul className="list__filter">
                            <li className="item" >
                                <div className="check-box">
                                    <input type="radio" name="typeHotels" value={""} defaultChecked />
                                    <p className="content">Mặc định</p>
                                </div>
                                {/* <div className="amount">
                                            <p className="number"></p>
                                        </div> */}
                            </li>
                            {typeHotels?.map((item, index) => {
                                return (
                                    <li className="item">
                                        <div className="check-box">
                                            <input type="radio" name="typeHotels" value={item?.value} />
                                            <p className="content">{item?.name}</p>
                                        </div>
                                        {/* <div className="amount">
                                            <p className="number">1</p>
                                        </div> */}
                                    </li>
                                )
                            })}
                        </ul>
                    </div>
                    {/* <div className="show-more">
                        Hiển thị thêm
                        <span className="icon">
                            <FontAwesomeIcon className="icon__sort" icon={faSortDown} />
                        </span>
                    </div> */}
                </div>
            </div>
        </>
    )
}