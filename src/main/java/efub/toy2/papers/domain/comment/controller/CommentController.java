package efub.toy2.papers.domain.comment.controller;

import efub.toy2.papers.domain.comment.dto.CommentRequestDto;
import efub.toy2.papers.domain.comment.dto.CommentResponseDto;
import efub.toy2.papers.domain.comment.service.CommentService;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.service.MemberService;
import efub.toy2.papers.global.config.AuthUser;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final MemberService memberService;


    /* 댓글 생성 */
    @PostMapping("/comments")
    public CommentResponseDto createComment(@AuthUser Member member, @RequestBody CommentRequestDto requestDto){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return commentService.createComment(member,requestDto);
    }

    /* 댓글 삭제 */
    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@AuthUser Member member ,@PathVariable Long commentId){
        if(!memberService.isAdminMember(member)) throw new CustomException(ErrorCode.NON_LOGIN);
        return commentService.deleteComment(member,commentId);
    }

    /* 스크랩의 댓글 목록 조회 */
    @GetMapping("/scraps/{scrapId}/comments")
    public List<CommentResponseDto> getScrapCommentList(@AuthUser Member member , @PathVariable Long scrapId){
        return commentService.findCommentListByScrapId(member,scrapId);
    }
}
