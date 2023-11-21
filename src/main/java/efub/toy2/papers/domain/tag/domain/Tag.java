package efub.toy2.papers.domain.tag.domain;

import efub.toy2.papers.domain.scrapTag.domain.ScrapTag;
import efub.toy2.papers.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long tagId;

    @Column
    private String tagName;

    @OneToMany(mappedBy = "tag")
    private List<ScrapTag> scraps;

    @Builder
    public Tag (String tagName) {
        this.tagName = tagName;
        this.scraps = new ArrayList<>();
    }

}
