package efub.toy2.papers.domain.scrapLike.controller;

import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.scrapLike.repository.ScrapLikeRepository;
import efub.toy2.papers.domain.scrapLike.service.ScrapLikeService;
import efub.toy2.papers.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class ScrapLikeController {

    private final ScrapLikeService scrapLikeService;

    @PostMapping("/{scrapId}")
    public String addScrapLike (@AuthUser Member member, @PathVariable Long scrapId) {
        scrapLikeService.addScrapLike(member, scrapId);
        return "Success";
    }

    @DeleteMapping("/{scrapId}")
    public String deleteScrapLike (@AuthUser Member member, @PathVariable Long scrapId) {
        scrapLikeService.deleteScrapLike(member, scrapId);
        return "Success";
    }

}
