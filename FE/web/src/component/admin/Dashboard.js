import React from 'react';
import Account from './account/Account';
import Activity from './activity/Activity';
import BookingActivity from './booking-activity/Booking';
import BookingRestaurant from './booking-restaurant/Booking';
import Booking from './booking/Booking';
import Hotel from './hotel/Hotel';
import Post from './post/Post';
import Profile from './profile/Profile';
import Restaurant from './restaurant/Restaurant';
import HotelPartner from '../partner/hotel/Hotel';
import ActivityPartner from '../partner/activity/Activity';
import RestaurantPartner from '../partner/restaurant/Restaurant';
import BookingHotelPartner from '../partner/booking/Booking';
import BookingRestaurantPartner from '../partner/booking-restaurant/Booking';
import BookingActivityPartner from '../partner/booking-activity/Booking';
import BookingMemberHotel from '../member/booking-hotel/Booking';
import BookingMemberActivity from '../member/booking-activity/Booking';
import BookingMemberRestaurant from '../member/booking-restaurant/Booking'
export default function Dashboard({ isPage }) {
    const page = localStorage.getItem("page");
    const roleId = localStorage.getItem("roleId");
    // if (page == "hotel") {
    //     if ((roleId == 1 || roleId == 2)) {
    //         return (
    //             <Hotel />
    //         )
    //     } else {
    //         return (
    //             <HotelPartner />
    //         )
    //     }
    // }
    return (
        <div className="wrapper__admin">
            {page === "post" && <Post />}
            {page === "account" && <Account />}
            {page === "profile" && <Profile />}

            {(page == "hotel" && (roleId == 1 || roleId == 2)) && <Hotel />}
            {(page == "hotel" && (roleId == 4)) && <HotelPartner />}
            {(page == "activity" && (roleId == 1 || roleId == 2)) && <Activity />}
            {(page == "activity" && (roleId == 4)) && <ActivityPartner />}
            {(page == "restaurant" && (roleId == 1 || roleId == 2)) && <Restaurant />}
            {(page == "restaurant" && (roleId == 4)) && <RestaurantPartner />}

            {(page == "booking-hotel" && (roleId == 4)) && <BookingHotelPartner />}
            {(page == "booking-hotel" && (roleId == 5)) && <BookingMemberHotel />}

            {(page == "booking-activity" && (roleId == 4)) && <BookingActivityPartner />}
            {(page == "booking-activity" && (roleId == 5)) && <BookingMemberActivity />}

            {(page == "booking-restaurant" && (roleId == 4)) && <BookingRestaurantPartner />}
            {(page == "booking-restaurant" && (roleId == 5)) && <BookingMemberRestaurant />}

          
          

           
        </div>
    )
}