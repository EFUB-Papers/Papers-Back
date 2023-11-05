package efub.toy2.papers.domain.folder.domain;

import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Folder extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long folderId;

    @Column
    private String folderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_owner_id", updatable = false)
    private Member folderOwner;

    @Builder
    public Folder(String folderName, Member folderOwner){
        this.folderName =folderName;
        this.folderOwner=folderOwner;
    }
}
