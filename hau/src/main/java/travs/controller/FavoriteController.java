package travs.controller;

import travs.common.FavoriteType;
import travs.model.FavoriteDTO;
import travs.exception.GeneralException;
import travs.response.BaseResponse;
import travs.service.FavoriteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@AllArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    //api trả về list yêu thích
    @GetMapping("/list")
    public BaseResponse getListUserFavorite(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(value = "per_page", required = false, defaultValue = "15") Integer perPage) throws GeneralException {
        return this.favoriteService.getListFavoriteByUserId(page, perPage);
    }
    //api thêm yêu thích
    @PostMapping("/add")
    public BaseResponse addFavoriteActivity(@RequestBody() FavoriteDTO requestBody) throws GeneralException {
        return this.favoriteService.addFavorite(requestBody);
    }

    // xóa yêu thích
    @GetMapping("/delete")
    public BaseResponse removeFavoriteActivity(@RequestParam("slug") String slug,
                                               @RequestParam(value = "type", required = false) FavoriteType type) throws GeneralException {
        return this.favoriteService.removeFavorBySlug(slug, type);
    }


}
