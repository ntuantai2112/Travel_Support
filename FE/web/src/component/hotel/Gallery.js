import { faCamera } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { BASE_URL_DOWNLOAD } from '../../lib/API';
import '../../style/gallery.scss';


export default function Gallery({ images }) {
    const [selectedImg, setSelectedImg] = useState();
    const [data, setData] = useState([]);
    let handleData = () => {
        setData(images)
    }
    useEffect(() => {
        handleData()
    }, [])
    useEffect(() => {
        handleData()
    }, [images])
    
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
                            {selectedImg ? <img src={`${BASE_URL_DOWNLOAD}${selectedImg?.url}`} alt="Selected" className="selected" />
                                : <img src={`${BASE_URL_DOWNLOAD}${data?.[0]?.url}`} alt="Selected" className="selected" />
                            }

                        </div>
                        <div className="wrapper__list--gallery">
                            {data?.map((img, index) => (
                                <img
                                    key={index}
                                    src={`${BASE_URL_DOWNLOAD}${img?.url}`}
                                    alt="dog"
                                    onClick={() => setSelectedImg(img)}
                                />
                            ))}
                        </div>
                    </div>

                </div>
            </div>
        </>
    )
}