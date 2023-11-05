package efub.toy2.papers.domain.folder.repository;

import efub.toy2.papers.domain.folder.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder ,Long> {
}
