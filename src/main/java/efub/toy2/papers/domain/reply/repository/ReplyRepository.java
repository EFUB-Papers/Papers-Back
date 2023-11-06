package efub.toy2.papers.domain.reply.repository;

import efub.toy2.papers.domain.comment.domain.Comment;
import efub.toy2.papers.domain.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    List<Reply> findAllByCommentOrderByCreatedAt(Comment comment);
}
