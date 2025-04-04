package codesquad.codestagram.service;

import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.exception.ReplyNotFoundException;
import codesquad.codestagram.repository.SpringDataJpaReplyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReplyService {

    private final SpringDataJpaReplyRepository replyRepository;

    public ReplyService(SpringDataJpaReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Optional<Reply> findOneReply(Long id) {
        return replyRepository.findById(id);
    }

    public Long save(Reply reply) {
        replyRepository.save(reply);
        return reply.getId();
    }

    public void updateReply(Long replyId, String contents, User user) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException());
        if(!reply.getWriter().getId().equals(user.getId())) {
            throw new IllegalStateException("수정 권한 없음");
        }
        reply.setContents(contents);
        replyRepository.save(reply);
    }

    public void deleteReply(Long replyId, Long loginUserId) {
        Reply reply = replyRepository.findByIdAndWriterId(replyId, loginUserId).get();
//                .orElseThrow(() -> new ReplyNotFoundException());
        reply.markAsDeleted();
        replyRepository.save(reply);
    }
}
