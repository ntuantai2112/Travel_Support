package travs.service;


import travs.request.FilterRequest;
import travs.request.SearchRequest;
import travs.response.hotel.SearchResponse;

public interface SearchService {

    SearchResponse searchFilter(SearchRequest searchRequest, FilterRequest filterRequest);

//    SearchCustomerResponse searchFilterCustomer(SearchRequest searchRequest ,FilterRequest  filterRequest );

}