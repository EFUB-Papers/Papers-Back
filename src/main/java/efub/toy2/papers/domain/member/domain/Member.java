package efub.toy2.papers.domain.member.domain;

import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.global.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
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

    @Column
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @OneToOne
    @JoinColumn(name = "default_folder_id")
    private Folder defaultFolder;

    @Builder
    public Member(String email, String nickname , String introduce, Folder defaultFolder , Role role){
        this.email = email;
        this.nickname = nickname;
        this.introduce = introduce;
        this.defaultFolder = defaultFolder;
        this.role = role;
    }

    /* 유저 정보 설정 */
    public void setMemberInfo(String nickname , String introduce , String profileImgUrl){
        this.nickname = nickname;
        this.introduce = introduce;
        this.profileImgUrl = profileImgUrl;
    }

    /* 한 줄 소개 수정 */
    public void updateIntroduce(String introduce){
        this.introduce = introduce;
    }

    /* 프로필 이미지 수정 */
    public void updateProfileImgUrl(String profileImgUrl){
        this.profileImgUrl = profileImgUrl;
    }

}
