package efub.toy2.papers.domain.follow.controller;

import efub.toy2.papers.domain.follow.dto.FollowResponseDto;
import efub.toy2.papers.domain.follow.service.FollowService;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.service.MemberService;
import efub.toy2.papers.global.config.AuthUser;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final MemberService memberService;

    /* 팔로우 걸기 */
    @PostMapping("/follows/{nickname}")
    public FollowResponseDto follow(@AuthUser Member member , @PathVariable String nickname){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return followService.saveFollow(member,nickname);
    }

    /* 팔로우 취소 */
    @DeleteMapping("/follows/{nickname}")
    public String unfollow(@AuthUser Member member , @PathVariable String nickname){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return followService.deleteFollow(member,nickname);
    }

    /* 내가 팔로우하고 있는 팔로잉 목록 조회 */
    @GetMapping("/members/followings")
    public List<FollowResponseDto> getFollowingList(@AuthUser Member member){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return followService.getFollowingListByMember(member);
    }

    /* 나를 팔로우하고 있는 팔로워 목록 조회 */
    @GetMapping("/members/followers")
    public List<FollowResponseDto> getFollowerList(@AuthUser Member member){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return followService.getFollowerListByMember(member);
    }

}
