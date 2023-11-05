package efub.toy2.papers.domain.folder.service;

import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.domain.folder.repository.FolderRepository;
import efub.toy2.papers.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {

    public final FolderRepository folderRepository;

    public Folder createDefaultFolder(Member member) {
        Folder folder = Folder.builder()
                .folderName("default_folder")
                .folderOwner(member)
                .build();
        folderRepository.save(folder);

        return folder;
    }
}
