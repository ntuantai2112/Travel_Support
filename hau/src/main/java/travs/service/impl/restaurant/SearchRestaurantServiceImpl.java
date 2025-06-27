package travs.service.impl.restaurant;

import travs.common.ConstantValue;
import travs.common.SQLQueryParam;
import travs.entity.restaurant.Restaurant;
import travs.entity.restaurant.RestaurantImage;
import travs.entity.restaurant.RestaurantMenu;
import travs.repository.restaurant.RestaurantRepository;
import travs.utils.AuthenticationUtils;
import travs.utils.EntityManagerUtils;
import travs.utils.HelperUtils;
import travs.utils.MappingUtils;
import travs.repository.FavoriteRepository;
import travs.repository.restaurant.FeatureRepository;
import travs.repository.restaurant.RestaurantImageRepository;
import travs.repository.restaurant.RestaurantMenuRepository;
import travs.request.SearchRequest;
import travs.request.restaurant.FilterRequestRestaurant;
import travs.response.restaurant.RestaurantDTO;
import travs.response.restaurant.RestaurantMenuDTO;
import travs.response.restaurant.SearchRestaurantResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Log4j2
public class SearchRestaurantServiceImpl implements SearchRestaurantService {

    private FavoriteRepository favoriteRepository;
    private EntityManager entityManager;
    private RestaurantRepository restaurantRepository;
    private FeatureRepository featureRepository;
    private RestaurantImageRepository restaurantImageRepository;
    private RestaurantMenuRepository restaurantMenuRepository;

    @Override
    public SearchRestaurantResponse searchFilter(SearchRequest searchRequest, FilterRequestRestaurant filterRequestRestaurant) {
        if (searchRequest.getPerPage() > ConstantValue.MAX_PER_PAGE) {
            searchRequest.setPerPage(ConstantValue.MAX_PER_PAGE);
        }
        SQLQueryParam request = generateQueryGetHotels(searchRequest, filterRequestRestaurant);
        return searchFilterByProdCodes(request, searchRequest);
    }

    private SQLQueryParam generateQueryGetHotels(SearchRequest searchRequest, FilterRequestRestaurant filterRequestRestaurant) {
        SQLQueryParam restaurantCodeQuery = getHotelCodesQuery(searchRequest, filterRequestRestaurant);
        return new SQLQueryParam(restaurantCodeQuery.getSql(), restaurantCodeQuery.getParams());
    }

    private SearchRestaurantResponse searchFilterByProdCodes(SQLQueryParam productCodeRequest, SearchRequest searchRequest) {
        List<String> restaurantCodes = EntityManagerUtils.buildQuery(entityManager, productCodeRequest).getResultList();
        List<Restaurant> restaurantList = restaurantRepository.findByCodeInOrderByCreatedAtDesc(restaurantCodes, PageRequest.of(searchRequest.getPage() - 1, searchRequest.getPerPage()));
        List<RestaurantDTO> restaurantDTOS = MappingUtils.map(restaurantList, RestaurantDTO.class);

        List<RestaurantImage> restaurantImages = restaurantImageRepository.findUniqueImage(restaurantCodes);
        Map<String, String> mapImage = restaurantImages.stream().collect(Collectors.toMap(RestaurantImage::getRestaurantCode, RestaurantImage::getUrl));

        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAllByRestaurantCodeIn(restaurantDTOS.stream().map(RestaurantDTO::getCode).collect(Collectors.toList()));

        Map<String, List<RestaurantMenuDTO>> menuMaps = new HashMap<>();
        restaurantMenuList.forEach(restaurantMenu -> {
            RestaurantMenuDTO menuDTO = MappingUtils.map(restaurantMenu, RestaurantMenuDTO.class);
            if (menuMaps.get(restaurantMenu.getRestaurantCode()) == null || menuMaps.get(restaurantMenu.getRestaurantCode()).isEmpty()) {
                List<RestaurantMenuDTO> dtoList = new ArrayList<>();
                dtoList.add(menuDTO);
                menuMaps.put(restaurantMenu.getRestaurantCode(), dtoList);
            } else {
                List<RestaurantMenuDTO> dtoList = menuMaps.get(restaurantMenu.getRestaurantCode());
                dtoList.add(menuDTO);
                menuMaps.put(restaurantMenu.getRestaurantCode(), dtoList);
            }
        });

        restaurantDTOS.forEach(restaurants -> {
            String imageUrl = mapImage.get(restaurants.getCode());
            if (imageUrl != null) {
                restaurants.setImage(imageUrl);
            }
            restaurants.setRestaurantMenuDTOList(menuMaps.get(restaurants.getCode()));
        });

        restaurantDTOS.forEach(restaurants -> restaurants.setFavor(checkFavoriteRestaurant(restaurants.getSlug())));
        return SearchRestaurantResponse.builder()
                .restaurants(restaurantDTOS)
                .total(restaurantCodes.size())
                .build();
    }

    private boolean checkFavoriteRestaurant(String slug) {
        String userId = AuthenticationUtils.getUserId();
        if (userId != null) {
            return favoriteRepository.findByUserIdAndSlug(userId, slug).isPresent();
        }
        return false;
    }

    private SQLQueryParam getHotelCodesQuery(SearchRequest searchRequest, FilterRequestRestaurant filterRequestRestaurant) {
        Map<String, Object> sqlParams = new HashMap<>();
        String query = null;
        if (searchRequest.getQ() == null || searchRequest.getQ().isEmpty()) {
            query = " select code from restaurant where approve_status = 'APPROVED'  ";
        } else {
            query = " select code from restaurant where ts_search @@ plainto_tsquery(:q) and approve_status = 'APPROVED' ";
            sqlParams.put("q", HelperUtils.unAccent((searchRequest.getQ())));
        }
        if (!filterRequestRestaurant.isNotProductFilter()) {
            if (!filterRequestRestaurant.getType().isEmpty()) {
                query = " select code from restaurant where code in (" + query + ") and restaurant_type = (:restaurantType) ";
                sqlParams.put("restaurantType", filterRequestRestaurant.getType());
            }
//            if (filterRequest.getStar() != null) {
//                query = " select code from restaurant where code in (" + query + ") and star = (:star) ";
//                sqlParams.put("star", filterRequest.getStar());
//            }
        }
        //phân trang thủ công
//        Integer page = searchRequest.getPage();
//        Integer perPage = searchRequest.getPerPage();
//        Integer offsetPage = (page - 1) * perPage;
//        query = query + " limit (:perPage) offset (:offsetPage)";
//        sqlParams.put("perPage", perPage);
//        sqlParams.put("offsetPage", offsetPage);
        return new SQLQueryParam(query, sqlParams);
    }

    private boolean checkFavorite(String slug) {
        String userId = AuthenticationUtils.getUserId();
        if (userId != null) {
            return favoriteRepository.findByUserIdAndSlug(userId, slug).isPresent();
        }
        return false;
    }

}
