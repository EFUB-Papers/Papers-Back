package efub.toy2.papers.domain.scrap.dto.request;

import efub.toy2.papers.domain.tag.dto.request.TagWriteRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reactor.util.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ScrapUpdateRequestDto {
    @Nullable
    private String scrapTitle;
    @Nullable
    private String scrapLink;
    @Nullable
    private String scrapContent;
    @Nullable
    private String category;
    @Nullable
    private Long folderId;
    @Nullable
    private final List<TagWriteRequestDto> tags = new ArrayList<>();
}
