package efub.toy2.papers.domain.comment.service;

import efub.toy2.papers.domain.comment.domain.Comment;
import efub.toy2.papers.domain.comment.dto.CommentRequestDto;
import efub.toy2.papers.domain.comment.dto.CommentResponseDto;
import efub.toy2.papers.domain.comment.repository.CommentRepository;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.repository.MemberRepository;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.domain.scrap.repository.ScrapRepository;
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
public class CommentService {

    private final ScrapRepository scrapRepository;
    private final CommentRepository commentRepository;

    /* 댓글 생성 */
    public CommentResponseDto createComment(Member member, CommentRequestDto requestDto) {
        Scrap scrap = scrapRepository.findById(requestDto.getScrapId())
                .orElseThrow(()->new CustomException(ErrorCode.NO_SCRAP_EXIST));
        Comment comment = Comment.builder()
                .commentWriter(member)
                .commentContent(requestDto.getCommentContent())
                .scrap(scrap)
                .build();
        commentRepository.save(comment);
        Boolean isMine = commentIsMine(member,comment);
        return new CommentResponseDto(comment,isMine);
    }

    /* 댓글 삭제 */
    public String deleteComment(Member member, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new CustomException(ErrorCode.NO_COMMENT_EXIST));
        if(comment.getCommentWriter().getMemberId() != member.getMemberId()) throw new CustomException(ErrorCode.INVALID_MEMBER);
        commentRepository.delete(comment);
        return "댓글이 삭제되었습니다.";
    }

    /* 스크랩의 댓글 목록 조회 */
    public List<CommentResponseDto> findCommentListByScrapId(Member member, Long scrapId) {
        Scrap scrap = scrapRepository.findById(scrapId)
                .orElseThrow(()->new CustomException(ErrorCode.NO_SCRAP_EXIST));
        List<Comment> commentList = commentRepository.findAllByScrapOrderByCreatedAt(scrap);
        List<CommentResponseDto> commentResponseDtoList =new ArrayList<>();
        for(Comment comment : commentList){
            Boolean isMine = commentIsMine(member,comment);
            commentResponseDtoList.add(new CommentResponseDto(comment , isMine));
        }
        return commentResponseDtoList;
    }

    /* 댓글 작성자 확인 */
    public Boolean commentIsMine(Member member , Comment comment) {
        Boolean isMine;
        if (comment.getCommentWriter().getMemberId() == member.getMemberId()) isMine = true;
        else isMine = false;
        return isMine;
    }
}
