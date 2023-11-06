package efub.toy2.papers.domain.scrap.repository;

import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap , Long> {
    List<Scrap> findAllByFolderOrderByCreatedAtDesc(Folder folder);
}
