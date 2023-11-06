package efub.toy2.papers.domain.reply.dto;

import lombok.Getter;

@Getter
public class ReplyRequestDto {
    private Long commentId;
    private String replyContent;
}
