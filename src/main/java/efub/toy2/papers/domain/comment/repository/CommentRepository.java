package efub.toy2.papers.domain.comment.repository;

import efub.toy2.papers.domain.comment.domain.Comment;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment , Long> {

    List<Comment> findAllByScrapOrderByCreatedAt(Scrap scrap);
    List<Comment> findAllByScrap(Scrap scrap);

}
