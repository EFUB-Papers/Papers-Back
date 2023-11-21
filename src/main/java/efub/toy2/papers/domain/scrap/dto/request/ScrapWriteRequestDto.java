package efub.toy2.papers.domain.scrap.dto.request;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ScrapWriteRequestDto {
    private String writerNickname;
    private String scrapTitle;
    private String scrapLink;
    private String scrapContent;
    private String category;
    private Long folderId;
    private final List<String> tags = new ArrayList<>();
}
