package efub.toy2.papers.domain.scrapTag.repository;

import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.domain.scrapTag.domain.ScrapTag;
import efub.toy2.papers.domain.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapTagRepository extends JpaRepository<ScrapTag, Long> {
    Optional<ScrapTag> findByTag (Tag tag);
    Optional<ScrapTag> findByScrap (Scrap scrap);
}
