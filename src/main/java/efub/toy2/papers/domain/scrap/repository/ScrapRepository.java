package efub.toy2.papers.domain.scrap.repository;

import efub.toy2.papers.domain.category.domain.Category;
import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap , Long>, JpaSpecificationExecutor<Scrap> {
    List<Scrap> findAllByCategory(Category category);
    List<Scrap> findAllByFolderOrderByCreatedAtDesc(Folder folder);
    List<Scrap> findAllOrderByCreatedAt();
    List<Scrap> findScrapsByTitleContaining(String query);
    List<Scrap> findScrapsByScrapContentContaining(String query);
}
