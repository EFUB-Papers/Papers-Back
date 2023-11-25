package efub.toy2.papers.domain.scrap.dto.request;

import efub.toy2.papers.domain.tag.dto.request.TagWriteRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ScrapWriteRequestDto {
    private String scrapTitle;
    private String scrapLink;
    private String scrapContent;
    private String category;
    private Long folderId;
    private final List<TagWriteRequestDto> tags = new ArrayList<>();
}
