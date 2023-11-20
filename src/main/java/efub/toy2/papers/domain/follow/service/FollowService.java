package efub.toy2.papers.domain.follow.service;

import efub.toy2.papers.domain.follow.domain.Follow;
import efub.toy2.papers.domain.follow.dto.FollowResponseDto;
import efub.toy2.papers.domain.follow.repository.FollowRepository;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.service.MemberService;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {
    private final MemberService memberService;
    private final FollowRepository followRepository;

    /* 팔로우 생성 */
    public FollowResponseDto saveFollow(Member member, String nickname) {
        Member toFollow = memberService.findMemberByNickname(nickname);
        if(isAlreadyFollowed(member,toFollow)) throw new CustomException(ErrorCode.ALREADY_FOLLOWED);
        Follow follow = Follow.builder()
                .follower(member)
                .following(toFollow)
                .build();
        followRepository.save(follow);
        return new FollowResponseDto(follow ,
                memberService.getProfileImg(follow.getFollower()) , memberService.getProfileImg(follow.getFollowing()));
    }

    /* 팔로우 삭제 */
    public String deleteFollow(Member member, String nickname) {
        Member following = memberService.findMemberByNickname(nickname);
        Follow follow = findFollowByFollowerAndFollowing(member,following);
        followRepository.delete(follow);
        return nickname+"에 대한 팔로우를 취소했습니다.";
    }

    /* 회원의 팔로잉 목록 조회 : 본인이 팔로워 */
    public List<FollowResponseDto> getFollowingListByMember(Member member) {
        List<Follow> followList = findFollowListByFollower(member);
        List<FollowResponseDto> responseDtoList = new ArrayList<>();
        for(Follow follow : followList){
            responseDtoList.add(new FollowResponseDto(follow,
                    memberService.getProfileImg(follow.getFollower()) , memberService.getProfileImg(follow.getFollowing())));
        }
        return responseDtoList;
    }

    /* 회원의 팔로워 목록 조회 : 본인이 팔로잉 */
    public List<FollowResponseDto> getFollowerListByMember(Member member) {
        List<Follow> followList = findFollowListByFollowing(member);
        List<FollowResponseDto> responseDtoList = new ArrayList<>();
        for(Follow follow:followList){
            responseDtoList.add(new FollowResponseDto(follow,
                    memberService.getProfileImg(follow.getFollower()) , memberService.getProfileImg(follow.getFollowing())));
        }
        return responseDtoList;
    }


    /* 이미 팔로우되어있는가 조회 */
    public Boolean isAlreadyFollowed(Member follower, Member following){
        if(follower.getRole().equals("ADMIN")) {
            Boolean isFollowed = followRepository.existsByFollowerAndFollowing(follower, following);
            return isFollowed;
        }
        else return false;
    }


    /* 팔로워와 팔로잉으로 팔로우 조회 */
    public Follow findFollowByFollowerAndFollowing(Member follower, Member following){
        return followRepository.findByFollowerAndFollowing(follower,following)
                .orElseThrow(()->new CustomException(ErrorCode.NO_FOLLOW_EXIST));
    }

    /* 팔로워로 팔로우 조회 */
    public List<Follow> findFollowListByFollower(Member member){
        return followRepository.findAllByFollower(member);
    }

    /* 팔로잉으로 팔로우 조회 */
    public List<Follow> findFollowListByFollowing(Member member){
        return followRepository.findAllByFollowing(member);
    }


    /* 팔로우하고 있지 않은 멤버 조회 조회 */
    public List<Follow> findFollowListByNotFollower(Member member){
        return followRepository.findAllByFollowerIsNot(member);
    }
}
