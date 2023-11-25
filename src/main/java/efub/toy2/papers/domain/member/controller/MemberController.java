package efub.toy2.papers.domain.member.controller;

import efub.toy2.papers.domain.folder.dto.FolderResponseDto;
import efub.toy2.papers.domain.follow.service.FollowService;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.dto.ProfileRequestDto;
import efub.toy2.papers.domain.member.dto.request.LoginRequestDto;
import efub.toy2.papers.domain.member.dto.request.NicknameRequestDto;
import efub.toy2.papers.domain.member.dto.response.LoginResponseDto;
import efub.toy2.papers.domain.member.dto.response.MemberInfoDto;
import efub.toy2.papers.domain.member.dto.response.MemberSearchResponseDto;
import efub.toy2.papers.domain.member.dto.response.ReissueResponseDto;
import efub.toy2.papers.domain.member.service.AuthService;
import efub.toy2.papers.domain.member.service.MemberService;
import efub.toy2.papers.domain.scrap.dto.response.ScrapSimpleResponseDto;
import efub.toy2.papers.global.config.AuthUser;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;
    private final FollowService followService;

    /* 로그인 */
    @PostMapping("/auth/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto) throws IOException{
        LoginResponseDto loginResPonseDto= authService.googleLogin(requestDto.getCode());
        return loginResPonseDto;
    }

    /* 토큰 재발급 */
    @GetMapping("/auth/reissue")
    public ReissueResponseDto tokenReissue(HttpServletRequest httpServletRequest){
        return authService.reissue(httpServletRequest);
    }

    /* 닉네임 중복 조회 */
    @PostMapping("/members/nickname/isExist")
    public Boolean isNicknameExist(@AuthUser Member member, @RequestBody NicknameRequestDto requestDto){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return memberService.isNicknameExist(member,requestDto.getNickname());
    }

    /* 프로필 설정 */
    @PostMapping ("/members/profile")
    public MemberInfoDto setProfile(@AuthUser Member member,
                                       @RequestPart(value="dto") ProfileRequestDto requestDto ,
                                       @RequestPart(value="profileImg") List<MultipartFile> images) throws IOException{
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return memberService.setProfile(member,requestDto,images);
    }

    /* 멤버 조회 */
    @GetMapping("/members/search/{nickname}")
    public MemberInfoDto memberFindByNickname(@PathVariable String nickname){
        return new MemberInfoDto(memberService.findMemberByNickname(nickname));
    }

    /* 회원 별 폴더 조회 */
    @GetMapping("/members/{nickname}/folders")
    public List<FolderResponseDto> getMemberFolderList(@PathVariable String nickname){
        Member member = memberService.findMemberByNickname(nickname);
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return memberService.findFolderListByMember(member);
    }

    /* 랜덤 회원 목록 리스트 조회 : 우선 팔로우하지 않은 회원 목록 조회하기로... 이후 찾아보고 수정. */
    @GetMapping("/members/random-list")
    public List<MemberSearchResponseDto> getRandomMemberList(@AuthUser Member member){
        return followService.findRandomMemberList(member);
    }

    // 회원별 스크랩 조회
    @GetMapping("/members/{memberId}/scraps")
    public List<ScrapSimpleResponseDto> getMembersScraps(@PathVariable Long memberId, @RequestParam(value = "page") Long page) {
        return memberService.getMembersScraps(memberId, page);
    }



}
