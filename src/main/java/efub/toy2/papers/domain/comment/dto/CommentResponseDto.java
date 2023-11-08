package efub.toy2.papers.domain.comment.dto;

import efub.toy2.papers.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    public Long commentId;
    public Long scrapId;
    public String commentContent;
    public String writerNickname;
    public String writerProfileImgUrl;
    public LocalDateTime createdAt;
    public Boolean commentIsMine;

    @Builder
    public CommentResponseDto(Comment comment , Boolean commentIsMine , String profileImgUrl){
        this.commentId = comment.getCommentId();
        this.scrapId = comment.getScrap().getScrapId();
        this.commentContent = comment.getCommentContent();
        this.writerNickname = comment.getCommentWriter().getNickname();
        this.createdAt = comment.getCreatedAt();
        this.commentIsMine = commentIsMine;
        this.writerProfileImgUrl = profileImgUrl;
    }
}
