import React from 'react';
import imgBG from '../../image/login/travel.jpg';
import '../../style/login.scss';
import ResetPassword from './ResetPassword';

export default function ResetPasswordBg() {
    return (
        <>
            <div className="wrapper__slider">
                <img alt="" src={imgBG} alt="" />
                
                <ResetPassword  />
            </div>
        </>
    )   
}