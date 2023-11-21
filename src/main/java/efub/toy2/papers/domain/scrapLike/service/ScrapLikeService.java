package efub.toy2.papers.domain.scrapLike.service;

import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.domain.scrap.repository.ScrapRepository;
import efub.toy2.papers.domain.scrapLike.domain.ScrapLike;
import efub.toy2.papers.domain.scrapLike.repository.ScrapLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ScrapLikeService {

    private final ScrapLikeRepository scrapLikeRepository;
    private final ScrapRepository scrapRepository;

    // 좋아요 추가
    public void addScrapLike(Member member, Long scrapId) {
        Scrap foundScrap = scrapRepository.findById(scrapId).get();
        scrapLikeRepository.save(
                ScrapLike.builder()
                        .scrap(foundScrap)
                        .member(member)
                        .build()
        );
    }

    // 좋아요 삭제
    public void deleteScrapLike(Member member, Long scrapId) {
        Scrap foundScrap = scrapRepository.findById(scrapId).get();
        ScrapLike foundScrapLike = scrapLikeRepository.findByScrapAndMember(foundScrap, member).get();
        scrapLikeRepository.delete(foundScrapLike);
    }

}
