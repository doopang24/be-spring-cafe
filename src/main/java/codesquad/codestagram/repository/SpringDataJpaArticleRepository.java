package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByDeletedFalse();

    @Query("""
        SELECT DISTINCT a FROM Article a
        LEFT JOIN FETCH a.replies
        WHERE a.id = :id AND a.deleted = false
    """)
    Optional<Article> findByIdWithReplies(@Param("id") Long id);
    // LEFT JOIN FETCH 쓰면 댓글이 없어도 게시글 정상적으로 조회
}
