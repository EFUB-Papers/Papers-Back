package efub.toy2.papers.domain.reply.repository;

import efub.toy2.papers.domain.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
}
