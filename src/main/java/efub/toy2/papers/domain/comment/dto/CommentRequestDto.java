package efub.toy2.papers.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long scrapId;
    private String commentContent;
}
