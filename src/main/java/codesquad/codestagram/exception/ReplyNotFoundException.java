package codesquad.codestagram.exception;

public class ReplyNotFoundException extends RuntimeException {
    public ReplyNotFoundException() {
        super("해당 댓글을 찾을 수 없습니다.");
    }
}
