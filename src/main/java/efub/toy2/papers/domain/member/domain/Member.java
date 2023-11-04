package efub.toy2.papers.domain.member.domain;

import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String introduce;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @OneToOne
    @JoinColumn(name = "default_folder_id")
    private Folder defaultFolder;

    @Builder
    public Member(String email, String nickname , String introduce, Folder defaultFolder){
        this.email = email;
        this.nickname = nickname;
        this.introduce = introduce;
        this.defaultFolder = defaultFolder;
    }

}
