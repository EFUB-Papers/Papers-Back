package efub.toy2.papers.domain.scrap.repository;

import efub.toy2.papers.domain.scrap.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap , Long> {
}
