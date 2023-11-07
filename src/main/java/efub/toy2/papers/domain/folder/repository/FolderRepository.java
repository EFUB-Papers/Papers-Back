package efub.toy2.papers.domain.folder.repository;

import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder ,Long> {
    List<Folder> findAllByFolderOwner(Member member);
}
