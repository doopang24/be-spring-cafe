package codesquad.codestagram.exception;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException() {
        super("해당 게시물을 찾을 수 없습니다.");
    }
}
