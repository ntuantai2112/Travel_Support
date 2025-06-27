package travs.controller.post;


import travs.utils.FileStore;
import travs.utils.HelperUtils;
import travs.request.PostRequest;
import travs.response.ApiResponse;
import travs.response.post.PostResponse;
import travs.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = -1)
@Log4j2
@AllArgsConstructor
public class PostController {

    private PostService postService;

    // trả về list bởi customer
    @GetMapping("/customer/post/search")
    public ResponseEntity<?> searchPost(@RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNo,
                                        @RequestParam(name = "title", required = false, defaultValue = "") String title) {
        log.debug("searchEmployee request : " + title);
        Integer pageSize = 5;

        title = HelperUtils.unAccent(title);

        ApiResponse apiResponse = postService.searchByTitle(title.trim(), pageNo, pageSize);
        ResponseEntity<ApiResponse> response = new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);

        log.debug("searchPost response : " + title);
        return response;
    }

    // trả về list tất cả post
    @GetMapping("/customer/post/search/all")
    public ResponseEntity<?> searchPostAll() {
        ApiResponse apiResponse = postService.searchAll();
        ResponseEntity<ApiResponse> response = new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
        return response;
    }

    // thêm bài viết
    @PostMapping("/content/post/add")
    public ResponseEntity<?> addPost(@ModelAttribute PostRequest postRequest) {
        postRequest.setImage(FileStore.getFilePath(postRequest.getMultipartFile(), "-post"));
        postService.add(postRequest);
        ResponseEntity<String> response = new ResponseEntity<>("Add post thành công", HttpStatus.OK);
        return response;
    }

    // update bài viết
    @PutMapping("/content/post/update")
    public ResponseEntity<?> updatePost(@ModelAttribute PostRequest postRequest,
                                        @RequestParam(name = "postId", required = true) String postId) {
        postRequest.setImage(FileStore.getFilePath(postRequest.getMultipartFile(), "-post"));
        postService.update(postId, postRequest);
        ResponseEntity<String> response = new ResponseEntity<>("update post thành công", HttpStatus.OK);
        return response;
    }

    // get detail bài viết
    @GetMapping("/customer/post/detail")
    public ResponseEntity<?> updatePost(
            @RequestParam(name = "postId", required = true) String postId) {
        PostResponse postResponse = postService.getById(postId);
        ResponseEntity<PostResponse> response = new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
        return response;
    }

    // delete  bài viết
    @DeleteMapping("/content/post/delete")
    public ResponseEntity<?> deletePost(@RequestParam(name = "postId", required = true) String postId) {
        postService.delete(postId);
        ResponseEntity<String> response = new ResponseEntity<>("delete post thành công", HttpStatus.OK);
        return response;
    }
}
