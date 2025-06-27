import React from 'react';
import loadingSvg from '../image/index.svg'
export default function LoadingProgress() {
    return (
        <div style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <img src={loadingSvg} style={{ width: 70, height: 70 }} />
        </div>
    )
}