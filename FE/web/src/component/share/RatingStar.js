import React from 'react';
import '../../style/rating-star.scss';

export default function RatingStar() {
    return (
        <>
            <div className="wrapper__rating--star">
                <ul className="star__list">
                    <li className="item__star"></li>
                    <li className="item__star"></li>
                    <li className="item__star"></li>
                    <li className="item__star"></li>
                    <li className="item__star"></li>
                </ul>
            </div>
        </>
    )
}