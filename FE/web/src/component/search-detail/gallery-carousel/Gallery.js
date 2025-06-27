import React, { useState } from 'react';
import Images from './Images';
import img1 from '../../../image/unique/item4.png';
import img2 from '../../../image/unique/item5.png'
import '../../../style/gallery.scss';
import { faCamera } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUtensils } from '@fortawesome/free-solid-svg-icons';
import { faBed } from '@fortawesome/free-solid-svg-icons';
import { BASE_URL_DOWNLOAD } from '../../../lib/API';


export default function Gallery({ images }) {
    const [selectedImg, setSelectedImg] = useState(images?.[0]);
    return (
        <>
            <div className="wrapper__gallery container">
                <div className="row row__gallery">
                    <div className="col-9 col__slider">
                        <div className="wrapper__selected">
                            <div className="dark__title">
                                <FontAwesomeIcon className="camera__icon" icon={faCamera} />
                                Tất cả ảnh
                                (<span className="amount">{images?.length}</span>)
                            </div>
                            <img src={`${BASE_URL_DOWNLOAD}${selectedImg?.url}`} alt="Selected" className="selected" />
                        </div>
                        <div className="wrapper__list--gallery">
                            {images?.map((img, index) => (
                                <img
                                    key={index}
                                    src={`${BASE_URL_DOWNLOAD}${img?.url}`}
                                    alt="dog"
                                    onClick={() => setSelectedImg(img)}
                                />
                            ))}
                        </div>
                    </div>
                    {/* <div className="col-3 col__right">
                        <ul className="wrapper__item--right">
                            <li className="item__right">
                                <img src={img1} alt="item" />
                                <div className="content__item">
                                    <div className="avatar__tourist">
                                        <div className="avatar__item">
                                            <img src={img2} alt="item" />
                                        </div>
                                        <div className="avatar__item second__item">
                                            <img src={img1} alt="item" />
                                        </div>
                                        <div className="avatar__item third__item">
                                            <img src={img2} alt="item" />
                                        </div>
                                    </div>
                                    Khách du lịch
                                    <p>(<span className="amount">42</span>)</p>
                                </div>
                            </li>
                            <li className="item__right">
                                <img src={img2} alt="item" />
                                <div className="content__item">
                                    <FontAwesomeIcon className="bed__icon icon" icon={faBed} />
                                    Phòng & Phòng Suite
                                    <p>(<span className="amount">7</span>)</p>
                                </div>
                            </li>
                            <li className="item__right">
                                <img src={img1} alt="item" />
                                <div className="content__item">
                                    <FontAwesomeIcon className="utensils__icon icon" icon={faUtensils} />
                                    Ăn uống
                                    <p>(<span className="amount">4</span>)</p>
                                </div>
                            </li>
                        </ul>
                    </div> */}
                </div>
            </div>
        </>
    )
}