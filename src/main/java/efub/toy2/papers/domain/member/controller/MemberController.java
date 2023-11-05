package efub.toy2.papers.domain.member.controller;

import efub.toy2.papers.domain.member.dto.request.LoginRequestDto;
import efub.toy2.papers.domain.member.dto.response.LoginResponseDto;
import efub.toy2.papers.domain.member.service.AuthService;
import efub.toy2.papers.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/auth/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto) throws IOException{
        LoginResponseDto loginResPonseDto= authService.googleLogin(requestDto.getCode());
        return loginResPonseDto;
    }
}
