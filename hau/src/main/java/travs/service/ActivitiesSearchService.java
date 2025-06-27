package travs.service;


import travs.response.activities.ActivitiesSearchResponse;
import travs.request.SearchRequest;
import travs.request.activities.FilterRequestActivities;

public interface ActivitiesSearchService {

    ActivitiesSearchResponse searchFilter(SearchRequest searchRequest, FilterRequestActivities filterRequest);

}