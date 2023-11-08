package efub.toy2.papers.domain.reply.controller;

import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.service.MemberService;
import efub.toy2.papers.domain.reply.dto.ReplyRequestDto;
import efub.toy2.papers.domain.reply.dto.ReplyResponseDto;
import efub.toy2.papers.domain.reply.service.ReplyService;
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
@RequestMapping
public class ReplyController {
    private final ReplyService replyService;
    private final MemberService memberService;

    /* 대댓글 생성 */
    @PostMapping("/replies")
    public ReplyResponseDto createReply(@AuthUser Member member, @RequestBody ReplyRequestDto requestDto){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return replyService.createReply(member,requestDto);
    }

    /* 대댓글 삭제 */
    @DeleteMapping("/replies/{replyId}")
    public String deleteReply(@AuthUser Member member , @PathVariable Long replyId){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return replyService.deleteReply(member,replyId);
    }

    /* 댓글의 대댓글 목록 조회 */
    @GetMapping("/comments/{commentId}/replies")
    public List<ReplyResponseDto> getCommentReplyList(@AuthUser Member member , @PathVariable Long commentId){
        return replyService.findReplyListByCommentId(member,commentId);
    }
}
