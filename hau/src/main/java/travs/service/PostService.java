package travs.service;

import travs.response.ApiResponse;
import travs.response.post.PostResponse;
import travs.request.PostRequest;

public interface PostService {

    ApiResponse searchByTitle(String title , Integer page, Integer size);

    ApiResponse searchAll();

    void add(PostRequest postRequest);

    void update(String PostId, PostRequest postRequest);

    void delete(String id);

    PostResponse getById(String id);
}
