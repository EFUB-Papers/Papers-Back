package efub.toy2.papers.domain.scrap.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ScrapListResponseDto {
    private Long thisPage;
    private Long lastPage;
    List<ScrapSimpleResponseDto> scraps;

    @Builder
    public ScrapListResponseDto (Long thisPage, Long lastPage, List<ScrapSimpleResponseDto> scraps) {
        this.thisPage = thisPage;
        this.lastPage = lastPage;
        this.scraps = scraps;
    }
}
