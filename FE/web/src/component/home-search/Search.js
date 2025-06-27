import React from 'react';
import '../../style/search.scss';
import Activity from './Activity';
import Hotel from './Hotel';
import Restaurant from './Restaurant';

export default function Search({ data }) {
    return (
        <>
            <div className="wrapper__search container">
                <h3 className="title">
                    Khám phá
                </h3>
                {data?.hotels?.length > 0 && <Hotel data={data?.hotels} />}
                {data?.restaurants?.length > 0 && <Restaurant data={data?.restaurants} />}
                {data?.activities?.length > 0 && <Activity data={data?.activities} />}
            </div>
        </>
    )
}