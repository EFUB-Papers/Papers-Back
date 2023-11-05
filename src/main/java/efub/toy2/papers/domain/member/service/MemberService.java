package efub.toy2.papers.domain.member.service;

import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.oauth.GoogleUser;
import efub.toy2.papers.domain.member.repository.MemberRepository;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member saveMember(@RequestBody  GoogleUser googleUser) {
        Member member = Member.builder()
                .email(googleUser.getEmail())
                .nickname(googleUser.getEmail())
                .build();
        memberRepository.save(member);
        /* 기본 폴더 생성 코드도 추가하기  앵간하면 이 함수 호출된 서비스에..? 흐음 */
        return member;
    }

    public Boolean checkJoined(String email) {
        System.out.println("checkJoined emailL "+email);
        Boolean isJoined = memberRepository.existsMemberByEmail(email);
        return isJoined;
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(ErrorCode.NO_MEMBER_EXIST));
    }
}
