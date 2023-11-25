package efub.toy2.papers.domain.scrap.controller;

import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.service.MemberService;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.domain.scrap.dto.request.ScrapSearchRequestDto;
import efub.toy2.papers.domain.scrap.dto.request.ScrapUpdateRequestDto;
import efub.toy2.papers.domain.scrap.dto.response.ScrapListResponseDto;
import efub.toy2.papers.domain.scrap.dto.response.ScrapResponseDto;
import efub.toy2.papers.domain.scrap.dto.request.ScrapWriteRequestDto;
import efub.toy2.papers.domain.scrap.service.ScrapService;
import efub.toy2.papers.global.config.AuthUser;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/scraps")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;
    private final MemberService memberService;

    // 접속 테스트
    @GetMapping("/test")
    public String testMethod () {
        return "Test successed";
    }

    // 스크랩 작성
    @PostMapping
    public ScrapResponseDto addScrap (@AuthUser Member member,
                                      @RequestPart(value="thumbnail") List<MultipartFile> thumbnail, @RequestPart(value="dto") ScrapWriteRequestDto dto) throws IOException {
        // 로그인된 상태인지 확인
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        Scrap scrap = scrapService.addScrap(member, thumbnail, dto);
        return ScrapResponseDto.builder().scrap(scrap).build();
    }

    // 스크랩 수정
    @PatchMapping("/{scrapId}")
    public ScrapResponseDto updateScrap (@AuthUser Member member,
                                         @RequestPart(value="thumbnail") List<MultipartFile> thumbnail, @RequestPart(value="dto") ScrapUpdateRequestDto dto, @PathVariable Long scrapId) throws IOException {
        // 로그인된 상태인지 확인
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        Scrap scrap = scrapService.updateScrap(member, thumbnail, dto, scrapId);
        return ScrapResponseDto.builder().scrap(scrap).build();
    }

    // 스크랩 조회
    @GetMapping("/{scrapId}")
    public ScrapResponseDto getScrap (@AuthUser Member member, @PathVariable Long scrapId) {
        return scrapService.getScrap(member, scrapId);
    }

    //  스크랩 삭제
    @DeleteMapping("/{scrapId}")
    public String deleteScrap (@AuthUser Member member, @PathVariable Long scrapId) {
        // 로그인된 상태인지 확인
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        scrapService.deleteScrap(member, scrapId);
        return "Success";
    }

    // 추천 스크랩 목록 조회
    @GetMapping("/recommend")
    public ScrapListResponseDto getRecommendScrap (@RequestParam(value = "page") Long page) {
        return scrapService.getRecommendScrap(page);
    }


    // 스크랩 검색
    @PostMapping("/search")
    public ScrapListResponseDto searchScraps (@PathVariable String searchby, @PathVariable String category, @PathVariable Long page, @RequestBody ScrapSearchRequestDto requestDto) {
        return scrapService.searchScraps(searchby, category, page, requestDto);
    }

    // 카테고리별 스크랩 목록 조회
    @GetMapping("/category")
    public ScrapListResponseDto getScrapsFromCategory (@PathVariable String category, @PathVariable Long page) {
        return scrapService.getScrapsFromCategory(category, page);
    }

    // 로그인한 멤버가 좋아요를 누른 스크랩 목록 조회
    @GetMapping("/liked")
    public ScrapListResponseDto getLikedScraps (@AuthUser Member member, @PathVariable Long page) {
        // 로그인된 상태인지 확인
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return scrapService.getLikedScraps(member, page);
    }





}
