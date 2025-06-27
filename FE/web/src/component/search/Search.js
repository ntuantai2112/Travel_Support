import React from 'react';
import '../../style/search.scss';
import img1 from '../../image/slick/image 8.png';
import img2 from '../../image/unique/item2.png'

export default function Search() {
    return (
        <>
            <div className="wrapper__search container">
               
                <div className="row row__search row__entertainment">
                    <div className="col-3 col-item col__content">
                        <h4 className="title">
                            Vui Chơi
                        </h4>
                        <p className="content">
                            Những địa điểm tham quan, hoạt động khám phá và trải nghiệm
                        </p>
                        <span>
                            Xem Tất Cả
                        </span>
                    </div>
                    <div className="col-3 col-item">
                        <div className="wrapper__item">
                            <div className="image">
                                <img alt="" src={img1} />
                            </div>
                            <div className="content">
                                Nhà Hàng Duy Trịnh
                            </div>
                        </div>
                    </div>
                    <div className="col-3 col-item">
                        <div className="wrapper__item">
                            <div className="image">
                                <img alt="" src={img2} />
                            </div>
                            <div className="content">
                                Nhà Hàng Duy Trịnh
                            </div>
                        </div>
                    </div>
                    <div className="col-3 col-item">
                        <div className="wrapper__item">
                            <div className="image">
                                <img alt="" src={img1} />
                            </div>
                            <div className="content">
                                Nhà Hàng Duy Trịnh
                            </div>
                        </div>
                    </div>
                </div>
                <div className="row row__search row__stay">
                    <div className="col-3 col-item col__content">
                        <h4 className="title">
                            Lưu Trú
                        </h4>
                        <p className="content">
                            Những địa điểm tham quan, hoạt động khám phá và trải nghiệm
                        </p>
                        <span>
                            Xem Tất Cả
                        </span>
                    </div>
                    <div className="col-3 col-item">
                        <div className="wrapper__item">
                            <div className="image">
                                <img alt="" src={img1} />
                            </div>
                            <div className="content">
                                Nhà Hàng Duy Trịnh
                            </div>
                        </div>
                    </div>
                    <div className="col-3 col-item">
                        <div className="wrapper__item">
                            <div className="image">
                                <img alt="" src={img2} />
                            </div>
                            <div className="content">
                                Nhà Hàng Duy Trịnh
                            </div>
                        </div>
                    </div>
                    <div className="col-3 col-item">
                        <div className="wrapper__item">
                            <div className="image">
                                <img alt="" src={img1} />
                            </div>
                            <div className="content">
                                Nhà Hàng Duy Trịnh
                            </div>
                        </div>
                    </div>
                </div>
                <div className="row row__search row__meal">
                    <div className="col-3 col-item col__content">
                        <h4 className="title">
                            Dùng Bữa
                        </h4>
                        <p className="content">
                            Những địa điểm tham quan, hoạt động khám phá và trải nghiệm
                        </p>
                        <span>
                            Xem Tất Cả
                        </span>
                    </div>
                    <div className="col-3 col-item">
                        <div className="wrapper__item">
                            <div className="image">
                                <img alt="" src={img1} />
                            </div>
                            <div className="content">
                                Nhà Hàng Duy Trịnh
                            </div>
                        </div>
                    </div>
                    <div className="col-3 col-item">
                        <div className="wrapper__item">
                            <div className="image">
                                <img alt="" src={img2} />
                            </div>
                            <div className="content">
                                Nhà Hàng Duy Trịnh
                            </div>
                        </div>
                    </div>
                    <div className="col-3 col-item">
                        <div className="wrapper__item">
                            <div className="image">
                                <img alt="" src={img1} />
                            </div>
                            <div className="content">
                                Nhà Hàng Duy Trịnh
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}
