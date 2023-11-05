package efub.toy2.papers.domain.scrap.domain;

import efub.toy2.papers.domain.category.domain.Category;
import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_writer_id")
    private Member scrapWriter;

    @OneToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
