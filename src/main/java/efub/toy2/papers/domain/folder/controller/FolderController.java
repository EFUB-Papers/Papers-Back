package efub.toy2.papers.domain.folder.controller;


import efub.toy2.papers.domain.folder.dto.FolderRequestDto;
import efub.toy2.papers.domain.folder.dto.FolderResponseDto;
import efub.toy2.papers.domain.folder.service.FolderService;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.global.config.AuthUser;
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

    /* 폴더 생성 */
    @PostMapping
    public FolderResponseDto createFolder(@AuthUser Member member , @RequestBody FolderRequestDto requestDto){
        return folderService.createFolder(member,requestDto);
    }

    /* 폴더 삭제 */
    @DeleteMapping("/{folderId}")
    public String deleteFolder(@AuthUser Member member, @PathVariable Long folderId){
        return folderService.deleteFolder(member,folderId);
    }

    /* 폴더의 스크랩 목록 조회 -> 이건 이후에 스크랩 기능 완료된 후 작성 가능. */
    @GetMapping("/{folderId}/scraps")
    public List<FolderResponseDto> getFolderScrapList(@AuthUser Member member , @PathVariable Long folderId){
        return folderService.findScrapListByFolderId(member,folderId);
    }

    /* 폴더 이름 변경 */
    @PutMapping("/{folderId}")
    public FolderResponseDto updateFolderName(@AuthUser Member member ,
                                              @PathVariable Long folderId, @RequestBody FolderRequestDto requestDto){
        return folderService.updateFolderName(member,folderId , requestDto);
    }
}
