import React from 'react';
import imgBG from '../../image/login/travel.jpg';
import '../../style/login.scss';
import ForgotPassword from './ForgotPassword';

export default function ForgotPasswordBg() {
    return (
        <>
            <div className="wrapper__slider">
                <img alt="" src={imgBG} alt="" />
                
                <ForgotPassword  />
            </div>
        </>
    )   
}