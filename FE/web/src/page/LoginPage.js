import React from 'react';
import SliderLog from '../component/login/Slider-bg';
import '../style/login.scss';

export default function LoginPage({ handleLogin }) {
    return (
        <>
            <SliderLog handleLogin={handleLogin} />
        </>
    )
}