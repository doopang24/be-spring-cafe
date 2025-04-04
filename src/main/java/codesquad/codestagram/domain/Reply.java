package codesquad.codestagram.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "REPLIES")
public class Reply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User writer;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    private String contents;
    private boolean deleted;

    protected Reply() {}

    public Reply(User writer, Article article, String contents) {
        this.writer = writer;
        this.article = article;
        this.contents = contents;
        this.deleted = false;
    }

    public void markAsDeleted() {
        this.deleted = true;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
