package codesquad.codestagram.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ARTICLES")
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;
    private String title;
    private String contents;
    private boolean deleted;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    // mappedBy = "article" : Reply 클래스에 있는 article 필드가 이 관계의 주인이다
    // cascade = CascadeType.ALL : Article을 저장, 삭제 업데이트 할 때 그에 딸린 Reply 들도 함께 처리된다
    // orphanRemoval = true : replies 리스트에서 Reply 를 제거하면 해당 Reply 는 DB 에서도 제거된다

    protected Article() {}

    public Article(User writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void markAsDeleted() {
        this.deleted = true;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void addReply(Reply reply) {
        this.replies.add(reply);
        reply.setArticle(this);
    }
}
