import React from 'react';
import '../../style/postdetail.scss';
import image1 from '../../image/slick/image 8.png'
import { useLocation } from 'react-router-dom';
import { BASE_URL_DOWNLOAD } from '../../lib/API';

export default function PostDetail() {
    let location = useLocation();
    let data = location.state
   
    return (
        <>
            <div className="wrapper__post-detail">
                <h1 className="title__blog">
                    {data?.title}
                </h1>
                <p className="content" dangerouslySetInnerHTML={{__html:data?.content}}>
                </p>
                <p className="image">
                    <img alt="" src={`${BASE_URL_DOWNLOAD}${data?.image}`} />
                </p>
                {/* <h3 className="title__content">
                    Giới thiệu chung về The Aratana Đà Lạt
                </h3>
                <p className="content">
                    Là một boutique Villa mới xây dựng, tọa lạc cách chợ Đà Lạt 6 phút đi xe, The Aratana Villa có vị trí đắc địa không xa trung tâm nhưng vẫn giữ được vẻ yên bình vốn có. Từ Villa, bạn có thể thuê xe máy, đi thăm thú hồ Tuyền Lâm, Dinh 3, hay các quán café cực chill trong vòng bán kính 10 phút một cách dễ dàng.
                </p>
                <p className="image">
                    <img alt="" src={image1} />
                </p>
                <p className="describe__image">
                    Những góc nhỏ nên thơ của The Aratana Villa in Dalat
                </p>
                <p className="content">
                    Còn nếu bạn muốn một chuyến đi nghỉ dưỡng sau những ngày làm việc vất vả và bận rộn, The Aratana Villa không mong mỏi gì hơn ngoài mang đến cho bạn sự thanh thản trong tâm hồn. Được thiết kế theo phong cách Wabi-Sabi Nhật Bản, tôn lên vẻ đẹp tự nhiên của vật liệu, Villa hướng ra thung lũng An Bình, vừa đủ để bạn đón bình minh lên lúc ban sớm và chiêm ngưỡng bầu trời màu hồng lúc hoàng hôn.
                </p>
                <p className="content">
                    Mỗi sáng tỉnh dậy, có trà ấm và cà phê thơm, có tiếng chim hót ngoài ban công và ánh mặt trời rực rỡ qua màn sương, đó là điều mà ai cũng cần sau những ngày tháng vội vã nơi thành thị.
                </p>
                <p className="content">
                    Cho dù bạn tới Đà Lạt để rong chơi hay nghỉ dưỡng, cùng gia đình, nhóm bạn thân hay người thương, The Aratana vẫn là lựa chọn tuyệt vời dành cho bạn.
                </p> */}

            </div>
        </>
    )
}