import React, { useState } from 'react';
import '../../style/style.scss';
import '../../style/searchplace.scss'
import { useHistory } from 'react-router-dom';


export default function SearchHome() {
    let history = useHistory();
    let [text, setText] = useState("")
    return (
        <>
            <div className="wrapper__search--place container">
                <div className="row">
                    <div className="col-12 col__search">
                        <div className="wrapper">
                            <div className="col-9 input__search">
                                <input placeholder="Bạn Muốn Đi Đâu?"
                                    value={text}
                                    onChange={e => setText(e.target.value)}
                                />
                                <i className="fa fa-search"></i>
                            </div>
                            <div className="col-3 wrapper__btn"
                                style={{ cursor: 'pointer', }}
                                onClick={() => {
                                    history.push({
                                        pathname: '/search',
                                        state: text
                                    })
                                }}
                            >
                                <span className="search__btn"

                                >
                                    Tìm Kiếm
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )

}