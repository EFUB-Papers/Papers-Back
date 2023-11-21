package efub.toy2.papers.domain.scrapLike.repository;

import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.domain.scrapLike.domain.ScrapLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapLikeRepository extends JpaRepository <ScrapLike, Long> {
    Boolean existsScrapLikeByScrap (Scrap scrap);
    List<ScrapLike> findAllByScrap (Scrap scrap);
    List<ScrapLike> findAllByMember (Member member);
}
