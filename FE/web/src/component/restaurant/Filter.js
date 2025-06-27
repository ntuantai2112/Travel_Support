import React from 'react';
import '../../style/filter.scss';
import { rank, typeHotels, typeRestaurant } from '../../utils/amenities';

export default function Filter({ star, type, handleStar, handleType }) {

    return (
        <>
            <div className="wrapper__filter--hotel">
                {/* <div className=" wrapper__covid">
                    <h4 className="title">Covid<sup>19</sup></h4>
                    <i className="text">Khu vực không phải cách ly y tế</i>
                </div> */}
                <div className="wrapper__main--filter">
                  
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
                            {typeRestaurant?.map((item, index) => {
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