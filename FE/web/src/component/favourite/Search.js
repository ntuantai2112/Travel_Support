import React from 'react';
import '../../style/search.scss';
import Activity from './Activity';
import Hotel from './Hotel';
import Restaurant from './Restaurant';

export default function Search({ data }) {
    return (
        <>
            <div className="wrapper__search container">
               
                {data?.hotel?.length > 0 && <Hotel data={data?.hotel} />}
                {data?.restaurant?.length > 0 && <Restaurant data={data?.restaurant} />}
                {data?.activities?.length > 0 && <Activity data={data?.activities} />}




            </div>
        </>
    )
}