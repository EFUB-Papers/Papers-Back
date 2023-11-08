package efub.toy2.papers.domain.member.service;

import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.domain.folder.dto.FolderResponseDto;
import efub.toy2.papers.domain.folder.repository.FolderRepository;
import efub.toy2.papers.domain.folder.service.FolderService;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.domain.Role;
import efub.toy2.papers.domain.member.dto.ProfileRequestDto;
import efub.toy2.papers.domain.member.dto.response.MemberInfoDto;
import efub.toy2.papers.domain.member.oauth.GoogleUser;
import efub.toy2.papers.domain.member.repository.MemberRepository;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import efub.toy2.papers.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final FolderRepository folderRepository;
    public final FolderService folderService;
    public final S3Service s3Service;

    /* 멤버 생성 */
    public Member saveMember(@RequestBody  GoogleUser googleUser) {
        Member member = Member.builder()
                .email(googleUser.getEmail())
                .nickname(googleUser.getEmail())
                .role(Role.ADMIN)
                .build();
        memberRepository.save(member);

        Folder folder = folderService.createDefaultFolder(member);
        member.setDefaultFolder(folder);

        return member;
    }

    /* 신규 회원인지 조사 */
    @Transactional(readOnly = true)
    public Boolean checkJoined(String email) {
        System.out.println("checkJoined emailL "+email);
        Boolean isJoined = memberRepository.existsMemberByEmail(email);
        return isJoined;
    }

    /* 이메일로 멤버 조회 */
    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(ErrorCode.NO_MEMBER_EXIST));
    }

    /* 닉네임 중복 조회 */
    @Transactional(readOnly = true)
    public Boolean isNicknameExist(String nickname) {
        return memberRepository.existsMemberByNickname(nickname);
    }

    /* 닉네임으로 멤버 조회 */
    @Transactional(readOnly = true)
    public Member findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(()->new CustomException(ErrorCode.NO_MEMBER_EXIST));
    }

    /* 멤버 프로필 설정 */
    public MemberInfoDto setProfile(Member member, ProfileRequestDto requestDto, List<MultipartFile> images) {
        List<String> imgPaths = s3Service.upload(images);
        member.setMemberInfo(requestDto.getNickname() , requestDto.getIntroduce() , imgPaths.get(0));
        return new MemberInfoDto(member);
    }

    /* 회원 별 폴더 목록 조회 */
    public List<FolderResponseDto> findFolderListByMember(Member member) {
        List<Folder> folderList = folderService.findFolderListByOwner(member);
        List<FolderResponseDto> responseDtoList = new ArrayList<>();
        for(Folder folder : folderList){
            responseDtoList.add(new FolderResponseDto(folder));
        }
        return responseDtoList;
    }

    /* 닉네임 제외한 회원 정보 수정 */
    public MemberInfoDto updateProfile(Member member, String introduce, List<MultipartFile> images) throws IOException {
        if(images != null){
            s3Service.deleteImage(member.getProfileImgUrl());
            List<String> imgPaths = s3Service.upload(images);
            member.updateProfileImgUrl(imgPaths.get(0));
        }
        if(introduce != null){
            member.updateIntroduce(introduce);
        }
        return new MemberInfoDto(member);
    }

    /* 로그인한 유저인지 검사 */
    public Boolean isAdminMember(Member member){
        return (member.getRole() == Role.ADMIN);
    }
}
