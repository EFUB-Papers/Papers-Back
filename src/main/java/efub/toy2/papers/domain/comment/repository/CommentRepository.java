package efub.toy2.papers.domain.comment.repository;

import efub.toy2.papers.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment , Long> {

}
