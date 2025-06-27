package travs.service.impl;

import travs.constant.StatusCode;
import travs.dao.PostDAO;
import travs.exception.RestApiException;
import travs.response.ApiResponse;
import travs.response.post.PostResponse;
import travs.utils.FileStore;
import travs.utils.HelperUtils;
import travs.entity.post.Post;
import travs.request.PostRequest;
import travs.service.PostService;
import travs.utils.MappingUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostDAO postDAO;
    // chả về list danh sách bài viết
    @Override
    public ApiResponse searchByTitle(String title, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<PostResponse> postResponses = new ArrayList<>();
        if (StringUtils.isBlank(title)) {
            List<Post> posts = postDAO.findAllByOrderByCreatedAtDesc(pageable);
            postResponses = MappingUtils.map(posts, PostResponse.class);

        } else {
            title = "%" + title + "%";
            List<Post> posts = postDAO.searchByTitle(title, pageable);
            postResponses = MappingUtils.map(posts, PostResponse.class);
        }
        title = HelperUtils.unAccent(title);
        Long totalPage = postDAO.countAllByContent(title);

        Map<String , PostResponse> mapPost = postResponses.stream().collect(Collectors.toMap(PostResponse::getId , Function.identity()));
        //List<PostResponse> =


        ApiResponse response = ApiResponse.builder().data(postResponses).totalElement(totalPage)
                .build();

        return response;
    }

    @Override
    public ApiResponse searchAll() {
        List<Post> postList = postDAO.findAll();
        List<PostResponse> postResponses = MappingUtils.map(postList, PostResponse.class);

        ApiResponse response = ApiResponse.builder().data(postResponses)
                .build();
        return response;
    }

    //  thêm bài viết
    @Override
    public void add(PostRequest postRequest) {
        Post post = new Post();
        post.setContent(postRequest.getContent());
        post.setImage(postRequest.getImage());
        post.setTitle(postRequest.getTitle());
        postDAO.save(post);
    }

    //  update bài viết
    @Override
    public void update(String PostId, PostRequest postRequest) {

        Post post = postDAO.getPostById(PostId);
        if (Objects.isNull(post)) {
            throw new RestApiException(StatusCode.POST_NOT_EXIST);
        }

        String image1 = FileStore.getFilePath(postRequest.getMultipartFile(), "-user");
        if (image1 != null) {
            postRequest.setImage(image1);
        }

        if (postRequest.getTitle() != null) post.setTitle(postRequest.getTitle());
        if (postRequest.getContent() != null) post.setContent(postRequest.getContent());
        if (image1 != null) {
            if (post.getImage() != null) {
                String image = post.getImage();
                FileStore.deleteFile(image);
            }
            post.setImage(postRequest.getImage());
        }
        postDAO.save(post);
    }

    //xóa bài viết
    @Override
    public void delete(String postId) {
        if (StringUtils.isEmpty(postId)) {
            throw new RestApiException(StatusCode.DATA_EMPTY);
        }
        postDAO.deleteAllById(postId);
    }

    // trả về 1 bài viết theo id
    @Override
    public PostResponse getById(String postId) {

        if (StringUtils.isEmpty(postId)) {
            throw new RestApiException(StatusCode.DATA_EMPTY);
        }
        Post post = postDAO.getPostById(postId);
        if (Objects.isNull(post)) {
            throw new RestApiException(StatusCode.POST_NOT_EXIST);
        }
        PostResponse response = MappingUtils.map(post, PostResponse.class);
        return response;
    }
}
