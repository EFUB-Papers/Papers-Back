package efub.toy2.papers.domain.follow.domain;

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
public class Follow extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member following;

    @Builder
    public Follow(Member follower , Member following){
        this.follower = follower;
        this.following = following;
    }
}
