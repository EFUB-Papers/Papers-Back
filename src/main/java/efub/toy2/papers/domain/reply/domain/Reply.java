package efub.toy2.papers.domain.reply.domain;

import efub.toy2.papers.domain.comment.domain.Comment;
import efub.toy2.papers.domain.member.domain.Member;
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
public class Reply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long replyId;

    @Column
    private String replyContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_writer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member replyWriter;

    @Builder
    public Reply(String replyContent, Comment comment, Member member){
        this.replyContent = replyContent;
        this.comment = comment;
        this.replyWriter = member;
    }
}
