import React, { useState } from 'react';

import Dashboard from '../component/admin/Dashboard';
import HeaderAdmin from '../component/admin/HeaderAdmin';
import { SideBar } from '../component/admin/SideBar';
import '../style/admin.scss'
export default function Manage({ isLogin }) {
    const [isPage, setIsPage] = useState("account")
    let handlePage = (page) => {
        setIsPage(page)
        localStorage.setItem("page", page)
    }
    return (
        <>
            <div className="wrapper-adminPage">
                <HeaderAdmin handlePage={handlePage} isLogin={isLogin} />
                <div style={{ display: 'flex', width: '100%' }}>
                    <SideBar handlePage={handlePage} isPage={isPage} />
                    <Dashboard isPage={isPage} />
                </div>
            </div>
        </>
    )
}