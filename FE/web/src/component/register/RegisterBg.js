import React from 'react';
import imgBG from '../../image/login/travel.jpg';
import '../../style/login.scss';
import Register from './Register';

export default function RegisterBg() {
    return (
        <>
            <div className="wrapper__slider">
                <img alt="" src={imgBG} alt="" />
                
                <Register />
            </div>
        </>
    )   
}