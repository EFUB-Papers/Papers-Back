package efub.toy2.papers.domain.member.controller;

import efub.toy2.papers.domain.folder.dto.FolderResponseDto;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.dto.ProfileRequestDto;
import efub.toy2.papers.domain.member.dto.request.LoginRequestDto;
import efub.toy2.papers.domain.member.dto.request.NicknameRequestDto;
import efub.toy2.papers.domain.member.dto.response.LoginResponseDto;
import efub.toy2.papers.domain.member.dto.response.MemberInfoDto;
import efub.toy2.papers.domain.member.dto.response.ReissueResponseDto;
import efub.toy2.papers.domain.member.service.AuthService;
import efub.toy2.papers.domain.member.service.MemberService;
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

    /* 프로필 설정 */
    @PostMapping("/members/profile")
    public MemberInfoDto setProfile(@AuthUser Member member,
                                       @RequestPart(value="dto") ProfileRequestDto requestDto ,
                                       @RequestPart(value="profileImg") List<MultipartFile> images) throws IOException{
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return memberService.setProfile(member,requestDto,images);
    }

    /* 회원 별 폴더 조회 */
    @GetMapping("/members/folders")
    public List<FolderResponseDto> getMemberFolderList(@AuthUser Member member){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return memberService.findFolderListByMember(member);
    }

    /* 프로필 수정 : 닉네임 제외 */
    @PutMapping("/members/profile/update")
    public MemberInfoDto updateProfile(@AuthUser Member member,
                                       @RequestPart(value = "introduce" , required = false) String introduce,
                                       @RequestPart(value="profileImg" , required = false) List<MultipartFile> images) throws IOException{
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return memberService.updateProfile(member,introduce,images);
    }



    /* 이건 이후 지우기 */
    @GetMapping("/members/test")
    public MemberInfoDto memberTest(@AuthUser Member member){
        return new MemberInfoDto(member);
    }

}
