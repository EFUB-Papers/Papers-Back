package efub.toy2.papers.domain.scrap.dto.response;

import efub.toy2.papers.domain.scrap.domain.Scrap;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScrapSimpleResponseDto {
    private Long scrapId;
    private Long folderId;
    private String scrapLink;
    private String scrapTitle;
    private String writerNickname;
    private String writerProfile;
    private String scrapContent;
    private int heartCount;
    private int commentCount;
    private String imgUrl;
    private LocalDateTime createdAt;

    @Builder
    public ScrapSimpleResponseDto (Scrap scrap, int heartCount, int commentCount) {
        this.scrapId = scrap.getScrapId();
        this.folderId = scrap.getFolder().getFolderId();
        this.scrapLink = scrap.getLink();
        this.scrapTitle = scrap.getTitle();
        this.writerNickname = scrap.getScrapWriter().getNickname();
        this.writerProfile = scrap.getScrapWriter().getProfileImgUrl();
        String originalContent = scrap.getScrapContent();
        if(originalContent.length()>60) this.scrapContent = originalContent.substring(0,60);    // 내용이 60자 이상일 경우 자르기
        else this.scrapContent = originalContent;
        this.heartCount = heartCount;
        this.commentCount = commentCount;
        this.imgUrl = scrap.getThumbnailUrl();
        this.createdAt = scrap.getCreatedAt();
    }
}
