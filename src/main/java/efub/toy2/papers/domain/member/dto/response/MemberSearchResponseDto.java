package efub.toy2.papers.domain.member.dto.response;

import efub.toy2.papers.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSearchResponseDto {
    public String nickname;
    public String introduce;
    public String profileImgUrl;
    public Boolean isFollower;

    @Builder
    public MemberSearchResponseDto(Member member , Boolean isFollower){
        this.nickname = member.getNickname();
        this.introduce = member.getIntroduce();
        this.profileImgUrl = member.getProfileImgUrl();
        this.isFollower = isFollower;

    }
}
