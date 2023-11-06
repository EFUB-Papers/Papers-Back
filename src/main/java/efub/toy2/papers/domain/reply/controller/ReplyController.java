package efub.toy2.papers.domain.reply.controller;

import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.reply.dto.ReplyRequestDto;
import efub.toy2.papers.domain.reply.dto.ReplyResponseDto;
import efub.toy2.papers.domain.reply.service.ReplyService;
import efub.toy2.papers.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {
    private final ReplyService replyService;

    /* 대댓글 생성 */
    @PostMapping
    public ReplyResponseDto createReply(@AuthUser Member member, @RequestBody ReplyRequestDto requestDto){
        return replyService.createReply(member,requestDto);
    }

    /* 대댓글 삭제 */
    @DeleteMapping("/{replyId}")
    public String deleteReply(@AuthUser Member member , @PathVariable Long replyId){
        return replyService.deleteReply(member,replyId);
    }

}
