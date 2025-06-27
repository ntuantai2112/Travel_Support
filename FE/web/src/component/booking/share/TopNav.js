import React from 'react';
import { faCheck } from '@fortawesome/free-solid-svg-icons';
import '../../../style/booking/share/top-nav.scss'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export default function TopNav() {
    return (
        <>
            <div className="wrapper__top--nav container">
                <div className="wrapper">
                    <div className="row row__top--nav">
                        <div className="col-4 col__left col__item">
                            <div className="line"></div>
                            <span className="number">
                                1
                            </span>
                            <p>Property Details</p>
                        </div>
                        <div className="col-4 col__middle col__item">
                            <div className="line dark"></div>
                            <span className="number">
                                2
                            </span>
                            <p>Your Infomation</p>
                        </div>
                        <div className="col-4 col__right col__item">
                            <span className="number" id="completed">
                                <FontAwesomeIcon icon={faCheck} id="check__icon" />
                            </span>
                            <p>Booking Completed</p>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}
