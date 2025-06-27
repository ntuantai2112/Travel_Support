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

export default function Dashboard({ isPage }) {
    const page = localStorage.getItem("page");
   
    return (
        <div className="wrapper__admin">
            {page === "post" && <Post />}
            {page === "account" && <Account />}
            {page === "profile" && <Profile />}
            {page === "hotel" && <Hotel />}
            {page === "booking-hotel" && <Booking />}
            {page === "activity" && <Activity />}
            {page === "booking-activity" && <BookingActivity />}
            {page === "restaurant" && <Restaurant />}
            {page === "booking-restaurant" && <BookingRestaurant />}
        </div>
    )
}