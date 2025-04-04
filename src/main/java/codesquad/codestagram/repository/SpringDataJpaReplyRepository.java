package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findByIdAndWriterId(Long replyId, Long writerId);
}
