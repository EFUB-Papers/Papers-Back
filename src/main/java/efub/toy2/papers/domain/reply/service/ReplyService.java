package efub.toy2.papers.domain.reply.service;

import efub.toy2.papers.domain.comment.domain.Comment;
import efub.toy2.papers.domain.comment.repository.CommentRepository;
import efub.toy2.papers.domain.comment.service.CommentService;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.service.MemberService;
import efub.toy2.papers.domain.reply.domain.Reply;
import efub.toy2.papers.domain.reply.dto.ReplyRequestDto;
import efub.toy2.papers.domain.reply.dto.ReplyResponseDto;
import efub.toy2.papers.domain.reply.repository.ReplyRepository;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    private final MemberService memberService;
    private final CommentService commentService;
    private final ReplyRepository replyRepository;

    public ReplyResponseDto createReply(Member member, ReplyRequestDto requestDto) {
        Comment comment = commentService.findCommentBuCommentId(requestDto.getCommentId());
        Reply reply = Reply.builder()
                .replyContent(requestDto.getReplyContent())
                .comment(comment)
                .member(member)
                .build();
        replyRepository.save(reply);
        Boolean isMine = replyIsMine(reply,member);
        return new ReplyResponseDto(reply,isMine,memberService.getProfileImg(reply.getReplyWriter()));
    }

    public String deleteReply(Member member, Long replyId) {
        Reply reply = findReplyByReplyId(replyId);
        if(!replyIsMine(reply,member)) throw new CustomException(ErrorCode.INVALID_MEMBER);
        replyRepository.delete(reply);
        return "대댓글이 삭제되었습니다.";
    }

    public Boolean replyIsMine(Reply reply,Member member){
        Boolean isMine;
        if(reply.getReplyWriter().getMemberId() == member.getMemberId()) isMine = true;
        else isMine = false;
        return isMine;
    }

    public List<ReplyResponseDto> findReplyListByCommentId(Member member, Long commentId) {
        Comment comment = commentService.findCommentBuCommentId(commentId);
        List<Reply> replyList = replyRepository.findAllByCommentOrderByCreatedAt(comment);
        List<ReplyResponseDto> responseDtoList = new ArrayList<>();
        for(Reply reply : replyList){
            Boolean isMine = replyIsMine(reply,member);
            responseDtoList.add(new ReplyResponseDto(reply,isMine,memberService.getProfileImg(reply.getReplyWriter())));
        }
        return responseDtoList;
    }

    /* 대댓글 id 로 대댓글 조회 */
    public Reply findReplyByReplyId(Long replyId){
        return replyRepository.findById(replyId)
                .orElseThrow(()->new CustomException(ErrorCode.NO_REPLY_EXIST));
    }
}
