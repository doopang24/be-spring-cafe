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
}
