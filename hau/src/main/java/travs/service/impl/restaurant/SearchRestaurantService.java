package travs.service.impl.restaurant;


import travs.response.restaurant.SearchRestaurantResponse;
import travs.request.SearchRequest;
import travs.request.restaurant.FilterRequestRestaurant;

public interface SearchRestaurantService {
    SearchRestaurantResponse searchFilter(SearchRequest searchRequest, FilterRequestRestaurant filterRequestRestaurant);
}