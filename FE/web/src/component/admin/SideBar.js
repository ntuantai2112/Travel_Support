import React from 'react';
export function SideBar({ handlePage }) {
    let isPage = localStorage.getItem("page")
    let handleStyle = (page) => {
        if (page === isPage) {
            return "sidebar__admin--menu--item li__active"
        } else {
            return "sidebar__admin--menu--item"
        }
    }
    const roleId = localStorage.getItem("roleId");
    return (
        <>
            <div className="sidebar__admin">
                <div className="sidebar__admin--menu">
                    {roleId == 1 && <div className={handleStyle("account")}
                        onClick={() => handlePage('account')}

                    >
                        <span>Tài khoản</span>
                    </div>}

                    <hr />
                    {(roleId == 1 || roleId == 3) &&
                        <>
                            <div className={handleStyle("post")}
                                onClick={() => handlePage('post')}

                            >
                                <span>Bài viết</span>
                            </div>
                            <hr />
                        </>
                    }
                    {(roleId == 1 || roleId == 2 || roleId == 4) &&
                        <>
                            <div className={handleStyle("hotel")}
                                onClick={() => handlePage('hotel')}

                            >
                                <span>Khách sạn</span>
                            </div>
                            <hr />
                        </>
                    }
                    {(roleId == 4 || roleId == 5) &&
                        <>
                            <div className={handleStyle("booking-hotel")}
                                onClick={() => handlePage('booking-hotel')}

                            >
                                <span>Danh sách đặt phòng</span>
                            </div>
                            <hr />
                        </>
                    }
                    {(roleId == 1 || roleId == 2 || roleId == 4) &&
                        <>
                            <div className={handleStyle("activity")}
                                onClick={() => handlePage('activity')}

                            >
                                <span>Khu vui chơi</span>
                            </div>
                            <hr />
                        </>
                    }
                    {(roleId == 4 || roleId == 5) &&
                        <>
                            <div className={handleStyle("booking-activity")}
                                onClick={() => handlePage('booking-activity')}

                            >
                                <span>Danh sách đặt vé</span>
                            </div>
                            <hr />
                        </>
                    }
                    {(roleId == 1 || roleId == 2 || roleId == 4) &&
                        <>
                            <div className={handleStyle("restaurant")}
                                onClick={() => handlePage('restaurant')}

                            >
                                <span>Nhà hàng</span>
                            </div>
                            <hr />
                        </>
                    }
                    {(roleId == 4 || roleId == 5) &&
                        <>
                            <div className={handleStyle("booking-restaurant")}
                                onClick={() => handlePage('booking-restaurant')}

                            >
                                <span>Danh sách đặt bàn</span>
                            </div>
                        </>
                    }


                </div>
            </div>

        </>
    )
}

