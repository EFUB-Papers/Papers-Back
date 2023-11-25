package efub.toy2.papers.domain.folder.controller;


import efub.toy2.papers.domain.folder.dto.FolderRequestDto;
import efub.toy2.papers.domain.folder.dto.FolderResponseDto;
import efub.toy2.papers.domain.folder.service.FolderService;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.service.MemberService;
import efub.toy2.papers.domain.scrap.dto.response.ScrapSimpleResponseDto;
import efub.toy2.papers.global.config.AuthUser;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;
    private final MemberService memberService;

    /* 폴더 생성 */
    @PostMapping
    public FolderResponseDto createFolder(@AuthUser Member member , @RequestBody FolderRequestDto requestDto){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return folderService.createFolder(member,requestDto);
    }

    /* 폴더 삭제 */
    @DeleteMapping("/{folderId}")
    public String deleteFolder(@AuthUser Member member, @PathVariable Long folderId){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return folderService.deleteFolder(member,folderId);
    }

    // 폴더별 스크랩 목록 조회
    @GetMapping("/{folderId}/scraps")
    public List<ScrapSimpleResponseDto> getFolderScrapList(@AuthUser Member member , @PathVariable Long folderId, @RequestParam(value = "page") Long page){
        return folderService.findScrapListByFolderId(member,folderId, page);
    }

    /* 폴더 이름 변경 */
    @PutMapping("/{folderId}")
    public FolderResponseDto updateFolderName(@AuthUser Member member ,
                                              @PathVariable Long folderId, @RequestBody FolderRequestDto requestDto){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return folderService.updateFolderName(member,folderId , requestDto);
    }
}
