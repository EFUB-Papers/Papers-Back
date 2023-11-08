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

    @Builder
    public FollowResponseDto(Follow follow , String followerProfileImg , String followingProfileImg){
        this.followId = follow.getFollowId();
        this.followerNickname = follow.getFollower().getNickname();
        this.followingNickname = follow.getFollowing().getNickname();
        this.followerProfileImg = followerProfileImg;
        this.followingProfileImg = followingProfileImg;
    }
}
