package efub.toy2.papers.domain.scrap.dto.response;

import efub.toy2.papers.domain.scrap.domain.Scrap;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ScrapSimpleResponseDto {
    private Long scrapId;
    private String scrapLink;
    private String scrapTitle;
    private String writerNickname;
    private String writerProfile;

    @Builder
    public ScrapSimpleResponseDto (Scrap scrap) {
        this.scrapId = scrap.getScrapId();
        this.scrapLink = scrap.getLink();
        this.scrapTitle = scrap.getTitle();
        this.writerNickname = scrap.getScrapWriter().getNickname();
        this.writerProfile = scrap.getScrapWriter().getProfileImgUrl();
    }
}
