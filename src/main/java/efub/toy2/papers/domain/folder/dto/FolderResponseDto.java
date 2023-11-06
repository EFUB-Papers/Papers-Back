package efub.toy2.papers.domain.folder.dto;

import efub.toy2.papers.domain.folder.domain.Folder;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FolderResponseDto {
    private Long folderId;
    private String folderName;
    private String folderOwnerNickname;

    @Builder
    public FolderResponseDto(Folder folder){
        this.folderId = folder.getFolderId();
        this.folderName = folder.getFolderName();
        this.folderOwnerNickname = folder.getFolderOwner().getNickname();
    }
}
