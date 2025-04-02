package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByDeletedFalse();
}
