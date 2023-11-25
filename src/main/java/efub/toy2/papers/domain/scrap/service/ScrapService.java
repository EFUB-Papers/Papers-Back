package efub.toy2.papers.domain.scrap.service;


import efub.toy2.papers.domain.category.domain.Category;
import efub.toy2.papers.domain.category.repository.CategoryRepository;
import efub.toy2.papers.domain.comment.domain.Comment;
import efub.toy2.papers.domain.comment.dto.CommentResponseDto;
import efub.toy2.papers.domain.comment.repository.CommentRepository;
import efub.toy2.papers.domain.comment.service.CommentService;
import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.domain.folder.repository.FolderRepository;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.member.repository.MemberRepository;
import efub.toy2.papers.domain.member.service.MemberService;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.domain.scrap.dto.request.ScrapSearchRequestDto;
import efub.toy2.papers.domain.scrap.dto.request.ScrapUpdateRequestDto;
import efub.toy2.papers.domain.scrap.dto.request.ScrapWriteRequestDto;
import efub.toy2.papers.domain.scrap.dto.response.ScrapListResponseDto;
import efub.toy2.papers.domain.scrap.dto.response.ScrapResponseDto;
import efub.toy2.papers.domain.scrap.dto.response.ScrapSimpleResponseDto;
import efub.toy2.papers.domain.scrap.repository.ScrapRepository;
import efub.toy2.papers.domain.scrapLike.domain.ScrapLike;
import efub.toy2.papers.domain.scrapLike.repository.ScrapLikeRepository;
import efub.toy2.papers.domain.scrapTag.domain.ScrapTag;
import efub.toy2.papers.domain.scrapTag.repository.ScrapTagRepository;
import efub.toy2.papers.domain.tag.domain.Tag;
import efub.toy2.papers.domain.tag.dto.request.TagWriteRequestDto;
import efub.toy2.papers.domain.tag.repository.TagRepository;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import efub.toy2.papers.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final FolderRepository folderRepository;
    private final CategoryRepository categoryRepository;
    private final ScrapTagRepository scrapTagRepository;
    private final TagRepository tagRepository;
    private final ScrapLikeRepository scrapLikeRepository;
    private final CommentRepository commentRepository;
    private final S3Service s3Service;
    private final MemberService memberService;
    private final CommentService commentService;

    // 새 스크랩 저장
    public Scrap addScrap(Member member, List<MultipartFile> thumbnail, ScrapWriteRequestDto requestDto) throws IOException {
        // 새 스크랩 생성 및 저장
        List<String> imgPaths = new ArrayList<>();
        if(thumbnail.isEmpty()){    // 썸네일이 없을 경우
            imgPaths.add(null);
        } else {    // 썸네일이 존재할 경우
            imgPaths = s3Service.uploadThumbnail(thumbnail);
        }
        Member writer = memberRepository.findByNickname(member.getNickname()).get();
        Folder folder = folderRepository.findById(requestDto.getFolderId()).get();
        Category category = categoryRepository.findByCategoryName(requestDto.getCategory()).get();
        Scrap savedScrap = scrapRepository.save(
                Scrap.builder()
                        .requestDto(requestDto)
                        .thumbnailUrl(imgPaths.get(0))
                        .scrapWriter(writer)
                        .folder(folder)
                        .category(category)
                        .build()
        );
        // 태그 다대다 관계 저장
        for (TagWriteRequestDto tagDto : requestDto.getTags()) {
            // 존재하지 않는 태그일 경우 새로 태그를 DB에 추가
            String tagName = tagDto.getTagName();
            Tag foundTag;
            if(!tagRepository.existsTagByTagName(tagName)) {
                foundTag = tagRepository.save(new Tag(tagName));
            } else {    // 존재하는 태그일 경우 DB에서 찾아오기
                foundTag = tagRepository.findByTagName(tagName).get();
            }
            // 새 ScrapTag를 DB에 저장
            scrapTagRepository.save(new ScrapTag(foundTag, savedScrap));
        }

        return savedScrap;
    }


    // 스크랩 수정
    public Scrap updateScrap(Member member, List<MultipartFile> thumbnail, ScrapUpdateRequestDto requestDto, Long scrapId) throws IOException {
        // 해당 스크랩의 작성자 본인인지 확인
        if(member.getMemberId() != scrapRepository.findById(scrapId).get().getScrapWriter().getMemberId())
            throw new CustomException(ErrorCode.INVALID_MEMBER);

        // 태그를 제외한 데이터 수정
        List<String> imgPaths = new ArrayList<>();
        if(thumbnail.isEmpty()){    // 썸네일이 없을 경우
            imgPaths.add(null);
        } else {    // 썸네일이 존재할 경우
            imgPaths = s3Service.uploadThumbnail(thumbnail);
        }
        Folder folder = folderRepository.findById(requestDto.getFolderId()).get();
        Category category = categoryRepository.findByCategoryName(requestDto.getCategory()).get();
        Scrap savedScrap = scrapRepository.findById(scrapId).get();
        savedScrap.updateScrap(requestDto, imgPaths.get(0), folder, category);

        // 태그 다대다 관계 갱신
        // 본래 있던 ScrapTag 삭제
        List<ScrapTag> originalScrapTags = scrapTagRepository.findAllByScrap(savedScrap);
        for(ScrapTag scrapTag : originalScrapTags) scrapTagRepository.delete(scrapTag);

        // 수정 데이터로 들어온 태그들을 추가
        for (TagWriteRequestDto tagDto : requestDto.getTags()) {
            // 존재하지 않는 태그일 경우 새로 태그를 DB에 추가
            String tagName = tagDto.getTagName();
            Tag foundTag;
            if(!tagRepository.existsTagByTagName(tagName)) {
                foundTag = tagRepository.save(new Tag(tagName));
            } else {    // 존재하는 태그일 경우 DB에서 찾아오기
                foundTag = tagRepository.findByTagName(tagName).get();
            }
            // 새 ScrapTag를 DB에 저장
            scrapTagRepository.save(new ScrapTag(foundTag, savedScrap));
        }
        return scrapRepository.findById(scrapId).get();
    }


    // 스크랩 하나 조회
    public ScrapResponseDto getScrap(Member member, Long scrapId) {
        // 해당 스크랩 정보 받아오기
        Scrap foundScrap = scrapRepository.findById(scrapId).get();

        // 로그인된 유저가 해당 스크랩에 좋아요를 눌렀는지 여부 받아오기
        Boolean liked = scrapLikeRepository.existsScrapLikeByScrap(foundScrap);

        // 해당 스크랩의 좋아요 개수 받아오기
        int likeCount = scrapLikeRepository.findAllByScrap(foundScrap).size();

        // 해당 스크랩에 달린 댓글 가져오기
        String profileImgUrl = memberService.getProfileImg(member);
        List<Comment> comments = commentRepository.findAllByScrapOrderByCreatedAt(foundScrap);
        List<CommentResponseDto> commentDtos = new ArrayList<>();
        for(Comment c : comments) {
            Boolean isMine = commentService.commentIsMine(member,c);
            commentDtos.add(
                    CommentResponseDto.builder()
                            .comment(c)
                            .commentIsMine(isMine)
                            .profileImgUrl(profileImgUrl)
                            .build()
            );
        }

        // 결과 리턴
        return ScrapResponseDto.builder()
                .scrap(foundScrap)
                .liked(liked)
                .likeCount(likeCount)
                .comments(commentDtos)
                .build();
    }

    // 스크랩 삭제
    public void deleteScrap(Member member, Long scrapId) {
        // 해당 스크랩의 작성자 본인인지 확인
        if(member.getMemberId() != scrapRepository.findById(scrapId).get().getScrapWriter().getMemberId())
            throw new CustomException(ErrorCode.INVALID_MEMBER);

        // 스크랩 삭제
        Scrap scrap = scrapRepository.findById(scrapId).get();
        scrapRepository.delete(scrap);
    }

    // 추천 스크랩 리스트 조회 (최신 스크랩 목록)
    public ScrapListResponseDto getRecommendScrap(Long page) {
        // 모든 스크랩 리스트를 시간순으로 받아오기
        List<Scrap> scraps= scrapRepository.findAllByOrderByCreatedAtDesc();
        return paging(scraps, page, 9);
    }

    // 스크랩 검색
    public ScrapListResponseDto searchScraps(String searchby, String category, Long page, ScrapSearchRequestDto requestDto) {
        String query = requestDto.getQuery();
        // searchby: 태그(tag), 제목+내용(titleContent)
        // category: 시사(news), 문화(culture), 여행(tour), IT(it), 라이프(life), 지식(knowledge), 기타(etc)
        // 기준별 조건문
        Set<Scrap> result = new HashSet<>();    // 중복제거를 위해 Set 사용
        if(searchby.equals("tag")) {
            // DB에서 쿼리 문자열을 포함하는 태그 리스트 가져오기
            List<Tag> foundTags = tagRepository.findByTagNameContaining(query);
            // ScrapTag를 참조하여 해당 태그들이 붙은 스크랩 리스트 가져오기
            List<ScrapTag> foundScrapTags = new ArrayList<>();
            for(Tag t : foundTags)
                foundScrapTags.add(scrapTagRepository.findByTag(t).get());
            for(ScrapTag st : foundScrapTags)
                result.add(scrapRepository.findById(st.getScrap().getScrapId()).get());

        } else if (searchby.equals("titleContent")) {
            List<Scrap> titleResult = scrapRepository.findScrapsByTitleContaining(query);
            List<Scrap> contentResult = scrapRepository.findScrapsByScrapContentContaining(query);
            result.addAll(titleResult);
            result.addAll(contentResult);
        } else {
            throw new CustomException(ErrorCode.INVALID_SEARCHBY);
        }

        // 카테고리 기준에 맞는 것들만 골라내기
        Category foundCategory = categoryRepository.findByCategoryName(category).get();
        List<Scrap> scraps = new ArrayList<>();
        for(Scrap s : result) {
            if(s.getCategory().getCategoryId() == foundCategory.getCategoryId())
                scraps.add(s);
        }

        return paging(scraps, page, 10);
    }

    // 카테고리별 스크랩 목록 조회
    public ScrapListResponseDto getScrapsFromCategory(String category, Long page) {
        Category foundCategory = categoryRepository.findByCategoryName(category).get();
        List<Scrap> scraps= scrapRepository.findAllByCategory(foundCategory);

        return paging(scraps, page, 10);
    }

    // 로그인한 사용자가 좋아요를 누른 스크랩 목록 조회
    public ScrapListResponseDto getLikedScraps(Member member, Long page) {
        // ScrapLike DB에서 해당 사용자를 기준으로 데이터 가져오기
        List<ScrapLike> foundLikes = scrapLikeRepository.findAllByMember(member);

        // 가져온 ScrapLike를 사용하여 Scrap DB에서 해당하는 스크랩 데이터 가져오기
        List<Scrap> scraps = new ArrayList<>();
        for(ScrapLike like : foundLikes)
            scraps.add(scrapRepository.findById(like.getScrap().getScrapId()).get());

        return paging(scraps, page, 10);
    }


    // 페이징 함수 (limit= 한 페이지당 스크랩 수)
    public ScrapListResponseDto paging (List<Scrap> scraps, Long page, int limit) {
        int size = scraps.size();

        // 총 페이지 개수 계산
        Long lastPage = size/10L;
        if(size%limit != 0) lastPage++;

        // 전달받은 페이지에 맞게 리스트 생성
        int start = (int)((page-1)*limit);
        List<Scrap> result = new ArrayList<>();
        if(page == lastPage)
            for(int i=start; i<size; i++) result.add(scraps.get(i));
        else
            for(int i= start; i<start+limit; i++) result.add(scraps.get(i));

        // Dto로 변환하여 리턴
        List<ScrapSimpleResponseDto> dtos = new ArrayList<>();
        for(Scrap s : result) {
            int heartCount = scrapLikeRepository.findAllByScrap(s).size();
            int commentCount = commentRepository.findAllByScrap(s).size();
            dtos.add(
                    ScrapSimpleResponseDto.builder()
                            .scrap(s)
                            .heartCount(heartCount)
                            .commentCount(commentCount)
                            .build()
            );
        }
        return ScrapListResponseDto.builder()
                .thisPage(page)
                .lastPage(lastPage)
                .scraps(dtos)
                .build();
    }


}
