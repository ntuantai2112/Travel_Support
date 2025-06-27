/* eslint-disable jsx-a11y/anchor-is-valid */
import { useState } from "react";
import {
  BrowserRouter as Router, Route, Switch
} from "react-router-dom";
import Activity from "./component/activity/Activity";
import ForgotPasswordBg from "./component/forgot/ForgotPasswordBg";
import ResetPasswordBg from "./component/forgot/ResetPasswordBg";
import Hotel from "./component/hotel/Hotel";
import RegisterBg from "./component/register/RegisterBg";
import Admin from "./page/Admin";
import BookingRestaurantPage from "./page/booking-page/BookingRestaurantPage";
import BookingRestaurantResultPage from "./page/booking-page/BookingRestaurantResultPage";
import BookingRoomPage from "./page/booking-page/BookingRoomPage";
import BookingRoomResultPage from "./page/booking-page/BookingRoomResultPage";
import BookingTablePage from "./page/booking-page/BookingTablePage";
import BookingTableResultPage from "./page/booking-page/BookingTableResultPage";
import BookingTicketPage from "./page/booking-page/BookingTicketPage";
import BookingTicketResultPage from "./page/booking-page/BookingTicketResultPage";
import Home from './page/Home';
import LoginPage from "./page/LoginPage";
import PostDetailPage from "./page/PostDetailPage";
import PostPage from "./page/PostPage";
import SearchDetailEntertainmentPage from "./page/SearchDetailEntertainmentPage";
import SearchEntertainmentPage from "./page/SearchEntertainmentPage";
import SearchHotelDetailPage from "./page/SearchHotelDetailPage";
import SearchHotelPage from "./page/SearchHotelPage";
import SearchPage from "./page/SearchPage";
import SearchRestaurantDetailPage from "./page/SearchRestaurantDetailPage";
import SearchRestaurantPage from "./page/SearchRestaurantPage";
import Restaurant from "./component/restaurant/Restaurant";
import HomeSearch from "./component/home-search/HomeSearch";
import Favourite from "./component/favourite/Favourite";
function Navigation() {
  const [isLogin, setIsLogin] = useState(false);
  let handleLogin = () => {
    setIsLogin(!isLogin)
  }
 
  return (
    <>
   
      <Router>
        <Switch>
          <Route exact path="/">
            <Home isLogin={isLogin} />
          </Route>
          <Route exact path="/login">
            <LoginPage handleLogin={handleLogin} />
          </Route>
          <Route exact path="/post">
            <PostPage  />
          </Route>
          <Route exact path="/post-detail" >
            <PostDetailPage />
          </Route>
          <Route exact path="/search" >
            <HomeSearch />
          </Route>
          <Route exact path="/search-hotel" >
            <Hotel />
          </Route>
          <Route exact path="/search-restaurant" >
            <Restaurant />
          </Route>
          <Route exact path="/search-entertainment" >
            <Activity />
          </Route>
          <Route exact path="/search-entertainment-detail" >
            <SearchDetailEntertainmentPage />
          </Route>
          <Route exact path="/search-restaurant-detail" >
            <SearchRestaurantDetailPage />
          </Route>
          <Route exact path="/search-hotel-detail" >
            <SearchHotelDetailPage />
          </Route>
          <Route exact path="/forward-password">
            <ResetPasswordBg />
          </Route>
          <Route exact path="/forgot">
            <ForgotPasswordBg />
          </Route>
          <Route exact path="/register">
            <RegisterBg />
          </Route>
          <Route exact path="/post" >
            <PostPage />
          </Route>
          <Route exact path="/favourite" >
            <Favourite />
          </Route>
          <Route exact path="/admin" >
            <Admin  isLogin={isLogin} />
          </Route>
         

          {/* booking */}
          <Route exact path="/booking-ticket" >
            <BookingTicketPage />
          </Route>
          <Route exact path="/booking-table" >
            <BookingTablePage />
          </Route>
          <Route exact path="/booking-restaurant" >
            <BookingRestaurantPage />
          </Route>
          <Route exact path="/booking-room" >
            <BookingRoomPage />
          </Route>
          <Route exact path="/booking-room-result" >
            <BookingRoomResultPage />
          </Route>
          <Route exact path="/booking-ticket-result" >
            <BookingTicketResultPage />
          </Route>
          <Route exact path="/booking-table-result" >
            <BookingTableResultPage />
          </Route>
          <Route exact path="/booking-restaurant-result" >
            <BookingRestaurantResultPage />
          </Route>
        </Switch>
      </Router>
     
    </>

  );
}


export default Navigation;
