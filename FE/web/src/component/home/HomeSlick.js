import React, { useState } from 'react';
import slick3 from '../../image/slick/image 10.png';
import slick4 from '../../image/slick/image 11.png';
import slick5 from '../../image/slick/image 12.png';
import slick6 from '../../image/slick/image 13.png';
import slick1 from '../../image/slick/image 8.png';
import slick2 from '../../image/slick/image 9.png';
import '../../style/home.scss';

export default function HomeSlick() {
    const [imag,setImg] = useState(slick1);
    return (
        <>
            <div className="wrapper__slick container">
                <div className="row row__hero">
                    <div className="hero__image">
                        <div className="col-12 col__hero">
                            <img alt="a" src={imag} />
                        </div>
                    </div>
                </div>
                <div className="row row__slick">
                    <div className="slick__image">
                        <div className="col col-item">
                            <img alt="img1" src={slick2}
                            onClick={() => setImg(slick2)}
                            />
                        </div>
                        <div className="col col-item">
                            <img alt="img2" src={slick3}
                             onClick={() => setImg(slick3)}
                            />
                        </div>
                        <div className="col col-item">
                            <img alt="img3" src={slick4}
                             onClick={() => setImg(slick4)}
                            />
                        </div>
                        <div className="col col-item">
                            <img alt="img4" src={slick5}
                             onClick={() => setImg(slick5)}
                            />
                        </div>
                        <div className="col col-item">
                            <img alt="img5" src={slick6}
                             onClick={() => setImg(slick6)} />
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}