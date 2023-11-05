package efub.toy2.papers.domain.comment.dto;

import efub.toy2.papers.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    public Long commentId;
    public Long scrapId;
    public String commentContent;
    public String writerNickname;

    @Builder
    public CommentResponseDto(Comment comment){
        this.commentId = comment.getCommentId();
        this.scrapId = comment.getScrap().getScrapId();
        this.commentContent = comment.getCommentContent();
        this.writerNickname = comment.getCommentWriter().getNickname();
    }

}
