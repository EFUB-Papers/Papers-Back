package efub.toy2.papers.domain.scrap.dto.response;

import efub.toy2.papers.domain.comment.dto.CommentResponseDto;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.domain.scrapTag.domain.ScrapTag;
import efub.toy2.papers.domain.tag.dto.response.TagResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ScrapResponseDto {
    private Long scrapId;
    private Long folderId;
    private String imgUrl;
    private String scrapTitle;
    private String scrapContent;
    private String link;
    private String writerNickname;  // 작성자의 닉네임
    private String writerPhoto; // 작성자 프로필 사진 URL
    private String folderName;  // 폴더명
    private String categoryName;    // 카테고리명
    private Boolean liked;  // 로그인된 유저가 좋아요를 눌렀는지 여부
    private int heartCount;  // 해당 스크랩의 좋아요 개수
    private int commentCount;
    private List<CommentResponseDto> comments;
    private List<TagResponseDto> tags;
    private LocalDateTime createdAt;

    @Builder
    public ScrapResponseDto (Scrap scrap, Boolean liked, int likeCount, List<CommentResponseDto> comments) {
        this.scrapId = scrap.getScrapId();
        this.folderId = scrap.getFolder().getFolderId();
        this.imgUrl = scrap.getThumbnailUrl();
        this.scrapTitle = scrap.getTitle();
        this.scrapContent = scrap.getScrapContent();
        this.link = scrap.getLink();
        this.writerNickname = scrap.getScrapWriter().getNickname();
        this.writerPhoto = scrap.getScrapWriter().getProfileImgUrl();
        this.folderName = scrap.getFolder().getFolderName();
        this.categoryName = scrap.getCategory().getCategoryName();
        this.liked = liked;
        this.heartCount = likeCount;
        this.commentCount = comments.size();
        this.comments = comments;
        this.tags = new ArrayList<>();
        for(ScrapTag tag : scrap.getTags()) {
            TagResponseDto tagDto = new TagResponseDto();
            tagDto.setTagName(tag.getTag().getTagName());
            tags.add(tagDto);
        }
        this.createdAt = scrap.getCreatedAt();
    }
}
