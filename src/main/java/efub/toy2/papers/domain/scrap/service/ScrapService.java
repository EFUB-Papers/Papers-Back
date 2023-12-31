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
    public void addScrap(Member member, List<MultipartFile> thumbnail, ScrapWriteRequestDto requestDto) throws IOException {
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

    }

    // 스크랩 수정 (썸네일 변경 없음)
    public void updateScrap(Member member, ScrapUpdateRequestDto requestDto, Long scrapId) {
        Scrap savedScrap = scrapRepository.findById(scrapId).get();

        // 해당 스크랩의 작성자 본인인지 확인
        if(member.getMemberId() != savedScrap.getScrapWriter().getMemberId())
            throw new CustomException(ErrorCode.INVALID_MEMBER);

        Folder folder;
        if(requestDto.getFolderId() != null) folder = folderRepository.findById(requestDto.getFolderId()).get();
        else folder = scrapRepository.findById(scrapId).get().getFolder();
        Category category;
        if(requestDto.getCategory() != null) category = categoryRepository.findByCategoryName(requestDto.getCategory()).get();
        else category = scrapRepository.findById(scrapId).get().getCategory();


        // 태그 다대다 관계 갱신
        if(requestDto.getTags() != null) {  // 전달된 태그 정보가 null이 아닌 경우
            // 본래 있던 ScrapTag 모두 삭제
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
        }

        savedScrap.updateScrap(requestDto, folder, category);
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
    public List<ScrapSimpleResponseDto> getRecommendScrap() {
        // 모든 스크랩 리스트를 시간순으로 받아오기
        List<Scrap> scraps= scrapRepository.findAllByOrderByCreatedAtDesc();
        List<ScrapSimpleResponseDto> result = new ArrayList<>();
        int end = 9;
        if(scraps.size()<9) end = scraps.size();
        for(int i=0; i<end; i++) {
            Scrap s = scraps.get(i);
            int heartCount = scrapLikeRepository.findAllByScrap(s).size();
            int commentCount = commentRepository.findAllByScrap(s).size();
            result.add(
                    ScrapSimpleResponseDto.builder()
                            .scrap(s)
                            .heartCount(heartCount)
                            .commentCount(commentCount)
                            .build()
            );
        }
        return result;
    }

    // 스크랩 검색
    public List<ScrapSimpleResponseDto> searchScraps(String searchby, String category, ScrapSearchRequestDto requestDto) {
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
        } else if (searchby.equals("all")) {
            // DB에서 쿼리 문자열을 포함하는 태그 리스트 가져오기
            List<Tag> foundTags = tagRepository.findByTagNameContaining(query);
            // ScrapTag를 참조하여 해당 태그들이 붙은 스크랩 리스트 가져오기
            List<ScrapTag> foundScrapTags = new ArrayList<>();
            for(Tag t : foundTags)
                foundScrapTags.add(scrapTagRepository.findByTag(t).get());
            for(ScrapTag st : foundScrapTags)
                result.add(scrapRepository.findById(st.getScrap().getScrapId()).get());

            // 내용과 제목에 검색어가 포함된 스크랩 목록 가져오기
            List<Scrap> titleResult = scrapRepository.findScrapsByTitleContaining(query);
            List<Scrap> contentResult = scrapRepository.findScrapsByScrapContentContaining(query);
            result.addAll(titleResult);
            result.addAll(contentResult);
        } else {
            System.out.println("요청받은 검색기준:" + searchby);
            throw new CustomException(ErrorCode.INVALID_SEARCHBY);
        }

        // 카테고리 기준에 맞는 것들만 골라내기
        List<Scrap> scraps = new ArrayList<>();
        if(!category.equals("all")){    // 카테고리 기준이 all이 아닐 경우에만 골라내기 수행
            Category foundCategory = categoryRepository.findByCategoryName(category).get();
            for(Scrap s : result) {
                if(s.getCategory().getCategoryId() == foundCategory.getCategoryId())
                    scraps.add(s);
            }
        } else {    // 카테고리 기준이 all일 경우 모두 넣기
            for(Scrap s : result) scraps.add(s);
        }

        List<ScrapSimpleResponseDto> dtos = new ArrayList<>();
        for(Scrap s : scraps) {
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
        return dtos;
    }

    // 카테고리별 스크랩 목록 조회
    public List<ScrapSimpleResponseDto> getScrapsFromCategory(String category) {
        Category foundCategory = categoryRepository.findByCategoryName(category).get();
        List<Scrap> scraps= scrapRepository.findAllByCategory(foundCategory);

        List<ScrapSimpleResponseDto> result = new ArrayList<>();
        for(Scrap s : scraps) {
            int heartCount = scrapLikeRepository.findAllByScrap(s).size();
            int commentCount = commentRepository.findAllByScrap(s).size();
            result.add(
                    ScrapSimpleResponseDto.builder()
                            .scrap(s)
                            .heartCount(heartCount)
                            .commentCount(commentCount)
                            .build()
            );
        }
        return result;
    }

    // 로그인한 사용자가 좋아요를 누른 스크랩 목록 조회
    public List<ScrapSimpleResponseDto> getLikedScraps(Member member) {
        // ScrapLike DB에서 해당 사용자를 기준으로 데이터 가져오기
        List<ScrapLike> foundLikes = scrapLikeRepository.findAllByMember(member);

        // 가져온 ScrapLike를 사용하여 Scrap DB에서 해당하는 스크랩 데이터 가져오기
        List<Scrap> scraps = new ArrayList<>();
        for(ScrapLike like : foundLikes)
            scraps.add(scrapRepository.findById(like.getScrap().getScrapId()).get());

        List<ScrapSimpleResponseDto> result = new ArrayList<>();
        for(Scrap s : scraps) {
            int heartCount = scrapLikeRepository.findAllByScrap(s).size();
            int commentCount = commentRepository.findAllByScrap(s).size();
            result.add(
                    ScrapSimpleResponseDto.builder()
                            .scrap(s)
                            .heartCount(heartCount)
                            .commentCount(commentCount)
                            .build()
            );
        }
        return result;
    }




}
