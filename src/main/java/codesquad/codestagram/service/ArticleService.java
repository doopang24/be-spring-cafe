package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.repository.SpringDataJpaArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final SpringDataJpaArticleRepository articleRepository;

    public ArticleService(SpringDataJpaArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // 게시글 등록
    public Long save(Article article) {
        articleRepository.save(article);
        return article.getId();
    }

    public Optional<Article> findOneArticle(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findAllArticle() {
        return articleRepository.findByDeletedFalse();
    }

    public void deleteArticle(Long id) {
        Article article = findOneArticle(id).get();
        article.markAsDeleted();
    }

    // JPA 가 엔티티의 변경 사항을 DB 에 자동으로 반영하려면 @Transactional 이 필요
    @Transactional
    public void update(Long id, String title, String contents) {
        Article article = articleRepository.findById(id).get(); // 이때 article 은 영속 상태가 된다
        article.update(title, contents);                        // 영속 상태이기 때문에 save 따로 안 해도 반영된다
    }
}
