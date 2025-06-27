package travs.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import travs.dao.PostDAO;
import travs.entity.post.Post;
import travs.exception.RestApiException;
import travs.request.PostRequest;
import travs.response.ApiResponse;
import travs.response.post.PostResponse;
import travs.utils.FileStore;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostDAO mockPostDAO;

    @InjectMocks
    private PostServiceImpl postServiceImplUnderTest;

    @Test
    void testSearchByTitle_Success_Test() {
        // Configure PostDAO.searchByTitle(...).
        final List<Post> posts1 = List.of(new Post("id", "content", "title", "image"));
        when(mockPostDAO.searchByTitle(any(), any(Pageable.class))).thenReturn(posts1);

        when(mockPostDAO.countAllByContent(any())).thenReturn(0L);

        // Run the test
        final ApiResponse result = postServiceImplUnderTest.searchByTitle("title", 2, 55);
        final ApiResponse resultIsBlank = postServiceImplUnderTest.searchByTitle("", 2, 55);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getData()).isNotNull();
        Assertions.assertThat(result.getData()).isInstanceOf(List.class);
        Assertions.assertThat(resultIsBlank).isNotNull();
        Assertions.assertThat(resultIsBlank.getData()).isNotNull();
        Assertions.assertThat(resultIsBlank.getData()).isInstanceOf(List.class);
    }

    @Test
    void testSearchAll_Success_Test() {
        // Setup
        // Configure PostDAO.findAll(...).
        final List<Post> posts = List.of(new Post("id", "content", "title", "image"));
        when(mockPostDAO.findAll()).thenReturn(posts);

        // Run the test
        final ApiResponse result = postServiceImplUnderTest.searchAll();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getData()).isNotNull();
        Assertions.assertThat(result.getData()).isInstanceOf(List.class);
    }

    @Test
    void testSearchAll_PostDAOReturnsNoItems() {
        // Setup
        when(mockPostDAO.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final ApiResponse result = postServiceImplUnderTest.searchAll();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getData()).isNotNull();
        Assertions.assertThat(result.getData()).isInstanceOf(List.class);
    }

    @Test
    void testAdd_Success_Test() {
        // Setup
        final PostRequest postRequest = new PostRequest(0L, "content", "title", "image", null);

        // Configure PostDAO.save(...).
        final Post post = new Post("id", "content", "title", "image");
        when(mockPostDAO.save(any(Post.class))).thenReturn(post);

        // Run the test
        postServiceImplUnderTest.add(postRequest);

        // Verify the results
        verify(mockPostDAO).save(any(Post.class));
    }

    @Test
    void testUpdate_Success_Test() {
        try (MockedStatic<FileStore> mockedStatic = mockStatic(FileStore.class)) {
            // Setup
            final PostRequest postRequest = new PostRequest(0L, "content", "title", "image", null);
            final Post post = new Post("id", "content", "title", "image");
            mockedStatic.when(() -> FileStore.getFilePath(any(), any())).thenAnswer(invocationOnMock -> "image");
            when(mockPostDAO.getPostById(any())).thenReturn(post);

            // Configure PostDAO.save(...).
            final Post post1 = new Post("id", "content", "title", "image");
            when(mockPostDAO.save(any(Post.class))).thenReturn(post1);

            // Run the test
            postServiceImplUnderTest.update("PostId", postRequest);

            // Verify the results
            verify(mockPostDAO).save(any(Post.class));
        }
    }

    @Test
    void testUpdate_Fail_Test() {
        when(mockPostDAO.getPostById(any())).thenReturn(null);
        org.junit.jupiter.api.Assertions.assertThrows(RestApiException.class, () -> postServiceImplUnderTest.update("PostId", null));
    }

    @Test
    void testDelete_Success_Test() {
        // Setup
        // Run the test
        postServiceImplUnderTest.delete("postId");

        // Verify the results
        verify(mockPostDAO).deleteAllById("postId");
    }

    @Test
    void testDelete_Fail_Test() {
        org.junit.jupiter.api.Assertions.assertThrows(RestApiException.class, () -> postServiceImplUnderTest.delete(""));
    }

    @Test
    void testGetById_Success_Test() {
        // Setup
        // Configure PostDAO.getPostById(...).
        final Post post = new Post("id", "content", "title", "image");
        when(mockPostDAO.getPostById(any())).thenReturn(post);

        // Run the test
        final PostResponse result = postServiceImplUnderTest.getById("postId");
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isNotNull();
        Assertions.assertThat(result.getContent()).isNotNull();
    }

    @Test
    void testGetById_PostNull_Fall_Test() {
        when(mockPostDAO.getPostById(any())).thenReturn(null);
        // Run the test
        org.junit.jupiter.api.Assertions.assertThrows(RestApiException.class, () -> postServiceImplUnderTest.getById("postId"));
    }

    @Test
    void testGetById_Fall_Test() {
        // Run the test
        org.junit.jupiter.api.Assertions.assertThrows(RestApiException.class, () -> postServiceImplUnderTest.getById(""));
    }
}
