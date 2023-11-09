package efub.toy2.papers.domain.reply.dto;

import efub.toy2.papers.domain.reply.domain.Reply;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponseDto {
    private Long replyId;
    private String replyWriterNickname;
    private String replyWriterProfileImg;
    private String replyContent;
    private Long commentId;
    public LocalDateTime createdAt;
    public Boolean replyIsMine;

    @Builder
    public ReplyResponseDto(Reply reply , Boolean replyIsMine , String profileImg){
        this.replyId= reply.getReplyId();
        this.replyWriterNickname = reply.getReplyWriter().getNickname();
        this.replyWriterProfileImg = profileImg;
        this.replyContent = reply.getReplyContent();
        this.commentId = reply.getComment().getCommentId();
        this.createdAt = reply.getCreatedAt();
        this.replyIsMine = replyIsMine;
    }
}
