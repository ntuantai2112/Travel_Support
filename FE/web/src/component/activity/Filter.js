import React from 'react';
import '../../style/filter.scss';
import { rank, typeActivity, typeHotels } from '../../utils/amenities';

export default function Filter({ handleDuration, handleType }) {

    return (
        <>
            <div className="wrapper__filter--hotel">
                <div className=" wrapper__covid">
                    <h4 className="title">Covid<sup>19</sup></h4>
                    <i className="text">Khu vực không phải cách ly y tế</i>
                </div>
                <div className="wrapper__main--filter">
                    <div className="rank__hotel">
                        <h5 className="title">Khung giờ</h5>
                        <ul className="list__filter">
                            <li className="item" >
                                <div className="check-box">
                                    <input type="radio" name="rank" value={""} defaultChecked
                                        onClick={() => handleDuration({
                                            from: "",
                                            to: ""
                                        })}
                                    />
                                    <p className="content">Mặc định</p>
                                </div>
                            </li>
                            <li className="item">
                                <div className="check-box">
                                    <input type="radio" name="rank"
                                        onClick={() => handleDuration({
                                            from: 1,
                                            to: 3
                                        })}
                                    />
                                    <p className="content">11h - 13h</p>
                                </div>
                            </li>
                            <li className="item">
                                <div className="check-box">
                                    <input type="radio" name="rank"
                                        onClick={() => handleDuration({
                                            from: 3,
                                            to: 5
                                        })}
                                    />
                                    <p className="content">13h - 15h</p>
                                </div>
                            </li>
                            <li className="item">
                                <div className="check-box">
                                    <input type="radio" name="rank"
                                        onClick={() => handleDuration({
                                            from: 5,
                                            to: 8
                                        })}
                                    />
                                    <p className="content">15h - 18h</p>
                                </div>
                            </li>

                        </ul>
                    </div>
                    <hr />
                    <div className="type__hotel">
                        <h5 className="title">Loại</h5>
                        <ul className="list__filter">
                            <li className="item" >
                                <div className="check-box">
                                    <input type="radio" name="typeHotels" value={""} defaultChecked
                                        onClick={() => handleType("")}
                                    />
                                    <p className="content">Mặc định</p>
                                </div>
                                {/* <div className="amount">
                                            <p className="number"></p>
                                        </div> */}
                            </li>
                            {typeActivity?.map((item, index) => {
                                return (
                                    <li className="item">
                                        <div className="check-box">
                                            <input type="radio" name="typeHotels" value={item?.value}
                                                onClick={() => handleType(item?.value)}
                                            />
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