package efub.toy2.papers.domain.follow.repository;

import efub.toy2.papers.domain.follow.domain.Follow;
import efub.toy2.papers.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    Boolean existsByFollowerAndFollowing(Member follower , Member following);
    Optional<Follow> findByFollowerAndFollowing(Member follower , Member following);
    List<Follow> findAllByFollower(Member member);
    List<Follow> findAllByFollowing(Member member);
    List<Follow> findAllByFollowerIsNot(Member member);
}
