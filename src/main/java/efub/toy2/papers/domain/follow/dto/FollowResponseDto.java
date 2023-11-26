package efub.toy2.papers.domain.follow.dto;

import efub.toy2.papers.domain.follow.domain.Follow;
import efub.toy2.papers.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowResponseDto {
    public Long followId;
    public String followerNickname;
    public String followerProfileImg;
    public String followingNickname;
    public String followingProfileImg;
    public String followingIntroduce;

    @Builder
    public FollowResponseDto(Follow follow){
        this.followId = follow.getFollowId();
        this.followerNickname = follow.getFollower().getNickname();
        this.followingNickname = follow.getFollowing().getNickname();
        this.followerProfileImg = follow.getFollower().getProfileImgUrl();
        this.followingProfileImg = follow.getFollowing().getProfileImgUrl();
        this.followingIntroduce = follow.getFollowing().getIntroduce();
    }
}
