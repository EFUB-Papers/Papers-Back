package efub.toy2.papers.domain.member.dto.response;

import efub.toy2.papers.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberInfoDto {
    public String nickname;
    public String email;
    public String introduce;
    public String profileImgUrl;
    public Long defaultFolderId;

    @Builder
    public MemberInfoDto(Member member){
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.introduce =member.getIntroduce();
        this.profileImgUrl =member.getProfileImgUrl();
        this.defaultFolderId = member.getDefaultFolder().getFolderId();
    }
}
