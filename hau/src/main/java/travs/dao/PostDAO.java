package travs.dao;

import travs.entity.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PostDAO extends JpaRepository<Post, Long> {

    // trả về list danh sách post
    @Query(value = " SELECT * FROM Post  WHERE  vn_unaccent(title) LIKE CONCAT('%',:title,'%') ORDER BY created_at desc "  , nativeQuery = true)
    List<Post> searchByTitle(@Param("title") String title, Pageable pageable);

//    List<Post> findAllByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable );

    List<Post> findAllByOrderByCreatedAtDesc( Pageable pageable );

    @Query(value = " SELECT * FROM Post  WHERE  vn_unaccent(content) LIKE CONCAT('%',:content,'%')  "  , nativeQuery = true)
    List<Post> searchByContent(String content, Pageable pageable);
    List<Post> findAll();

    Post getPostById(String id);

    void deleteAllById(String id);

    @Query(value = "SELECT  count(p.*) FROM Post p WHERE  vn_unaccent(title) LIKE CONCAT('%',:title,'%') ", nativeQuery = true)
    Long countAllByContent(@Param("title") String title);

}
