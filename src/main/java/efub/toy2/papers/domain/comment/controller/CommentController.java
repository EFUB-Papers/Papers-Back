package efub.toy2.papers.domain.comment.controller;

import efub.toy2.papers.domain.comment.dto.CommentRequestDto;
import efub.toy2.papers.domain.comment.dto.CommentResponseDto;
import efub.toy2.papers.domain.comment.service.CommentService;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.global.config.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    /* 댓글 생성 */
    @PostMapping("/comments")
    public CommentResponseDto createComment(@AuthUser Member member, @RequestBody CommentRequestDto requestDto){
        return new CommentResponseDto(commentService.createComment(member,requestDto));
    }

    /* 댓글 삭제 */
    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@AuthUser Member member ,@PathVariable Long commentId){
        return commentService.deleteComment(member,commentId);
    }

}
