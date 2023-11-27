package efub.toy2.papers.domain.scrap.domain;

import efub.toy2.papers.domain.category.domain.Category;
import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.scrap.dto.request.ScrapUpdateRequestDto;
import efub.toy2.papers.domain.scrap.dto.request.ScrapWriteRequestDto;
import efub.toy2.papers.domain.scrapTag.domain.ScrapTag;
import efub.toy2.papers.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Scrap extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long scrapId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String scrapContent;

    @Column(nullable = false)
    private String link;

    @Column
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_writer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member scrapWriter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @OneToMany(mappedBy = "scrap")
    private List<ScrapTag> tags;

    @Builder
    public Scrap (ScrapWriteRequestDto requestDto, String thumbnailUrl, Member scrapWriter, Folder folder, Category category) {
        this.title = requestDto.getScrapTitle();
        this.scrapContent = requestDto.getScrapContent();
        this.link = requestDto.getScrapLink();
        this.thumbnailUrl = thumbnailUrl;
        this.scrapWriter = scrapWriter;
        this.folder = folder;
        this.category = category;
        this.tags = new ArrayList<>();
    }

    // 수정 (썸네일 변경 없음)
    public void updateScrap (ScrapUpdateRequestDto requestDto, Folder folder, Category category) {
        if(requestDto.getScrapTitle() != null) this.title = requestDto.getScrapTitle();
        if(requestDto.getScrapContent() != null) this.scrapContent = requestDto.getScrapContent();
        if(requestDto.getScrapLink() != null) this.link = requestDto.getScrapLink();
        this.folder = folder;
        this.category = category;
        this.tags = new ArrayList<>();
    }

}
