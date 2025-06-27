import React from 'react';
import '../../style/login.scss'
import Login from '../login/Login'
import imgBG from '../../image/login/travel.jpg'

export default function SliderLog({handleLogin}) {
    return (
        <>
            <div className="wrapper__slider">
                <img src={imgBG} alt="" />
                
                <Login handleLogin={handleLogin} />
            </div>
        </>
    )   
}