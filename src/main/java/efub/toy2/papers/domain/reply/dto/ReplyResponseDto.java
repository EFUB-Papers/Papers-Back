package efub.toy2.papers.domain.reply.dto;

import efub.toy2.papers.domain.reply.domain.Reply;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReplyResponseDto {
    private Long replyId;
    private String replyWriterNickname;
    private String replyContent;
    private Long commentId;

    @Builder
    public ReplyResponseDto(Reply reply){
        this.replyId= reply.getReplyId();
        this.replyWriterNickname = reply.getReplyWriter().getNickname();
        this.replyContent = reply.getReplyContent();
        this.commentId = reply.getComment().getCommentId();
    }
}
