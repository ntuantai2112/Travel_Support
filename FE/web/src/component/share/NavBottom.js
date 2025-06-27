import React from 'react';
import '../../style/navbottom.scss';

export default function NavBottom() {
    return (
        <>
            <div className="wrapper__nav">
                <div className="previous__btn">
                    Trước
                </div>
                <div className="number__page">
                    <ul className="list__page">
                        <li className="item active">1</li>
                        <li className="item">2</li>
                        <li className="item">3</li>
                        <li className="item">4</li>
                    </ul>
                </div>
                <div className="next__btn">
                    Tiếp theo
                </div>
            </div>
        </>
    )
}