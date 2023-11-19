package efub.toy2.papers.domain.member.repository;

import efub.toy2.papers.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member , Long> {
   Optional<Member> findByEmail(String email);

   Boolean existsMemberByEmail(String email);

    Boolean existsMemberByNickname(String nickname);



    Optional<Member> findByNickname(String nickname);
}
