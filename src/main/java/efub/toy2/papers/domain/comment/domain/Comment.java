package efub.toy2.papers.domain.comment.domain;

import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long commentId;

    @Column(nullable = false)
    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Scrap scrap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_writer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member commentWriter;

    @Builder
    public Comment(String commentContent , Scrap scrap , Member commentWriter){
        this.commentContent =commentContent;
        this.scrap =scrap;
        this.commentWriter = commentWriter;
    }
}
