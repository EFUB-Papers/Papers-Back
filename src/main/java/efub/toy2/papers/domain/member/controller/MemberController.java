package efub.toy2.papers.domain.member.controller;

import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.dto.request.LoginRequestDto;
import efub.toy2.papers.domain.member.dto.request.NicknameRequestDto;
import efub.toy2.papers.domain.member.dto.response.LoginResponseDto;
import efub.toy2.papers.domain.member.dto.response.MemberInfoDto;
import efub.toy2.papers.domain.member.dto.response.ReissueResponseDto;
import efub.toy2.papers.domain.member.service.AuthService;
import efub.toy2.papers.domain.member.service.MemberService;
import efub.toy2.papers.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;

    /* 로그인 */
    @PostMapping("/auth/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto) throws IOException{
        LoginResponseDto loginResPonseDto= authService.googleLogin(requestDto.getCode());
        return loginResPonseDto;
    }

    /* 토큰 재발급 */
    @PostMapping("/auth/reissue")
    public ReissueResponseDto tokenReissue(HttpServletRequest httpServletRequest){
        return authService.reissue(httpServletRequest);
    }

    /* 닉네임 중복 조회 */
    @PostMapping("/members/nickname/isExist")
    public Boolean isNicknameExist(@RequestBody NicknameRequestDto requestDto){
        return memberService.isNicknameExist(requestDto.getNickname());
    }


    /* 멤버 조회 */
    @PostMapping("/members/search")
    public MemberInfoDto memberFindByNickname(@RequestBody NicknameRequestDto requestDto){
        return new MemberInfoDto(memberService.findMemberByNickname(requestDto.getNickname()));
    }

    /* 이건 이후 지우기 */
    @GetMapping("/members/test")
    public MemberInfoDto memberTest(@AuthUser Member member){
        return new MemberInfoDto(member);
    }

}
