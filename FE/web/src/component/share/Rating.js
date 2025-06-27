import React from 'react';
import '../../style/rating.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSortDown } from '@fortawesome/free-solid-svg-icons'
import NavBottom from './NavBottom';

export default function Rating() {
    return (
        <>
            <div className="wrapper__rating">
                <h3 className="title">
                    Đánh giá
                </h3>
                <div className="wrapper__comment--item">
                    <h4 className="name__title">
                        Lê Nguyên
                    </h4>
                    <div className="rating__star">
                        <ul className="star__list">
                            <li className="item__star"></li>
                            <li className="item__star"></li>
                            <li className="item__star"></li>
                            <li className="item__star"></li>
                            <li className="item__star"></li>
                        </ul>
                    </div>
                    <div className="title__comment">
                        Tôi hoàn toàn thư giãn khi ở đây
                    </div>
                    <p className="content__comment">
                        "<span>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</span>"
                    </p>
                    <p className="see__more">Xem thêm <FontAwesomeIcon className="icon__sort" icon={faSortDown} /></p>
                </div>
                <div className="wrapper__comment--item">
                    <h4 className="name__title">
                        Lê Nguyên
                    </h4>
                    <div className="rating__star">
                        <ul className="star__list">
                            <li className="item__star"></li>
                            <li className="item__star"></li>
                            <li className="item__star"></li>
                            <li className="item__star"></li>
                            <li className="item__star"></li>
                        </ul>
                    </div>
                    <div className="title__comment">
                        Tôi hoàn toàn thư giãn khi ở đây
                    </div>
                    <p className="content__comment">
                        "<span>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</span>"
                    </p>
                    <p className="see__more">Xem thêm <FontAwesomeIcon className="icon__sort" icon={faSortDown} /></p>
                </div>
                <div className="section__nav--bottom">
                    <NavBottom />
                </div>
            </div>
        </>
    )
}