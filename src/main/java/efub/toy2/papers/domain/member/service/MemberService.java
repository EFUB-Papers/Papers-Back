package efub.toy2.papers.domain.member.service;

import efub.toy2.papers.domain.comment.repository.CommentRepository;
import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.domain.folder.dto.FolderResponseDto;
import efub.toy2.papers.domain.folder.service.FolderService;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.domain.Role;
import efub.toy2.papers.domain.member.dto.ProfileRequestDto;
import efub.toy2.papers.domain.member.dto.response.MemberInfoDto;
import efub.toy2.papers.domain.member.oauth.GoogleUser;
import efub.toy2.papers.domain.member.repository.MemberRepository;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.domain.scrap.dto.response.ScrapSimpleResponseDto;
import efub.toy2.papers.domain.scrap.repository.ScrapRepository;
import efub.toy2.papers.domain.scrapLike.repository.ScrapLikeRepository;
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
    public final FolderService folderService;
    public final S3Service s3Service;
    private final ScrapRepository scrapRepository;
    private final ScrapLikeRepository scrapLikeRepository;
    private final CommentRepository commentRepository;

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
    public Boolean isNicknameExist(Member member, String nickname) {
        /* 본인 닉네임은 중복 조회에서 제외시킴. */
        if(member.getNickname().equals(nickname)){
            return false;
        }
        return memberRepository.existsMemberByNickname(nickname);
    }

    /* 닉네임으로 멤버 조회 */
    @Transactional(readOnly = true)
    public Member findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(()->new CustomException(ErrorCode.NO_MEMBER_EXIST));
    }

    /* 멤버 프로필 설정 */
    public MemberInfoDto setProfile(Member member, ProfileRequestDto requestDto, List<MultipartFile> images) throws IOException {
        /* 닉네임 중복 조회 */
        if(isNicknameExist(member, requestDto.getNickname())) throw new CustomException(ErrorCode.ALREADY_EXIST_NICKNAME);

        /* 이미 설정되어있는 이미지가 있는 경우, 기존 프로필 이미지 삭제 */
        if(member.getProfileImgUrl() != null){ s3Service.deleteImage(member.getProfileImgUrl()); }

        /* 프로필 설정 */
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

    // 멤버별 스크랩 목록 조회
    public List<ScrapSimpleResponseDto> getMembersScraps(Long memberId, Long page) {
        Member writer = memberRepository.findById(memberId).get();
        List<Scrap> scraps = scrapRepository.findAllByScrapWriter(writer);
        List<ScrapSimpleResponseDto> result = new ArrayList<>();
        for(Scrap s : scraps) {
            int heartCount = scrapLikeRepository.findAllByScrap(s).size();
            int commentCount = commentRepository.findAllByScrap(s).size();
            result.add(
                    ScrapSimpleResponseDto.builder()
                            .scrap(s)
                            .heartCount(heartCount)
                            .commentCount(commentCount)
                            .build()
            );
        }
        return result;
    }

    /* 로그인한 유저인지 검사 */
    public Boolean isAdminMember(Member member){
        return (member.getRole() == Role.ADMIN);
    }

    /* 닉네임으로 회원 프로필 이미지 조회 */
    public String getProfileImg(Member member){
        return member.getProfileImgUrl();
    }



    /* 랜덤 회원 목록 조회 : 일단 멤버 리스트 앞에서 3개 자르기
    public List<MemberSearchResponseDto> findRandomMemberList(Member member) {
        List<Member> randomMemberList;
        if(member.getRole().equals("ADMIN")) randomMemberList = memberRepository.findAllByMemberIdIsNot(member.getMemberId());
        else randomMemberList = memberRepository.findAll();

        List<MemberSearchResponseDto> searchResponseDtoList = new ArrayList<>();
        for(Member randomMember : randomMemberList){
            searchResponseDtoList.add(new MemberSearchResponseDto(randomMember));
        }
        if(searchResponseDtoList.size() > 3) return searchResponseDtoList.subList(0,2);
        else return searchResponseDtoList;
    }

     */


    /* 닉네임 제외한 회원 정보 수정
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
    */
}
