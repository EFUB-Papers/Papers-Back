package efub.toy2.papers.domain.scrapTag.domain;

import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.domain.tag.domain.Tag;
import efub.toy2.papers.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScrapTag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long scrapTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_id")
    private Scrap scrap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public ScrapTag (Tag tag, Scrap scrap) {
        this.tag = tag;
        this.scrap = scrap;
    }
}
