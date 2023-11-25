package efub.toy2.papers.domain.folder.service;

import efub.toy2.papers.domain.comment.repository.CommentRepository;
import efub.toy2.papers.domain.folder.domain.Folder;
import efub.toy2.papers.domain.folder.dto.FolderRequestDto;
import efub.toy2.papers.domain.folder.dto.FolderResponseDto;
import efub.toy2.papers.domain.folder.repository.FolderRepository;
import efub.toy2.papers.domain.member.domain.Member;
import efub.toy2.papers.domain.scrap.domain.Scrap;
import efub.toy2.papers.domain.scrap.dto.response.ScrapListResponseDto;
import efub.toy2.papers.domain.scrap.dto.response.ScrapSimpleResponseDto;
import efub.toy2.papers.domain.scrap.repository.ScrapRepository;
import efub.toy2.papers.domain.scrap.service.ScrapService;
import efub.toy2.papers.domain.scrapLike.repository.ScrapLikeRepository;
import efub.toy2.papers.global.exception.CustomException;
import efub.toy2.papers.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FolderService {

    public final FolderRepository folderRepository;
    private final ScrapRepository scrapRepository;
    private final ScrapLikeRepository scrapLikeRepository;
    private final CommentRepository commentRepository;

    /* 기본 폴더 생성 */
    public Folder createDefaultFolder(Member member) {
        Folder folder = Folder.builder()
                .folderName("default_folder")
                .folderOwner(member)
                .build();
        folderRepository.save(folder);

        return folder;
    }

    /* 폴더 생성 */
    public FolderResponseDto createFolder(Member member, FolderRequestDto requestDto) {
        Folder folder = Folder.builder()
                .folderName(requestDto.getFolderName())
                .folderOwner(member)
                .build();
        folderRepository.save(folder);
        return new FolderResponseDto(folder);
    }

    /* 폴더 삭제 */
    public String deleteFolder(Member member, Long folderId) {
        Folder folder = findFolderByFolderId(folderId);
        if(!isFolderOwner(member,folder)) throw new CustomException(ErrorCode.INVALID_MEMBER);
        if(folder.getFolderName().equals("default_folder")) throw new CustomException(ErrorCode.DEFAULT_FOLDER_CANNOT_DELETE);
        folderRepository.delete(folder);
        return "폴더가 삭제되었습니다.";
    }

    /* 폴더 id 로 폴더 조회 */
    public Folder findFolderByFolderId(Long folderId){
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(()->new CustomException(ErrorCode.NO_FOLDER_EXIST));
        return folder;
    }

    /* 폴더의 스크랩 목록 조회 */
    public ScrapListResponseDto findScrapListByFolderId(Member member, Long folderId, Long page) {
        Folder folder = findFolderByFolderId(folderId);
        List<Scrap> scrapList = scrapRepository.findAllByFolderOrderByCreatedAtDesc(folder);
        return paging(scrapList, page, 10);
    }

    /* 폴더의 이름 변경 */
    public FolderResponseDto updateFolderName(Member member, Long folderId , FolderRequestDto requestDto) {
        Folder folder = findFolderByFolderId(folderId);
        if(!isFolderOwner(member,folder)) throw new CustomException(ErrorCode.INVALID_MEMBER);
        if(folder.getFolderName().equals("default_folder")) throw new CustomException(ErrorCode.DEFAULT_FOLDER_CANNOT_CHANGE);
        folder.updateFolderName(requestDto.getFolderName());
        return new FolderResponseDto(folder);
    }


    /* 회원이 폴더의 주인인지 검사 */
    public Boolean isFolderOwner(Member member , Folder folder){
        Boolean isOwner;
        if(folder.getFolderOwner().getMemberId() == member.getMemberId()) isOwner = true;
        else isOwner = false;
        return isOwner;
    }

    /* 회원 별 폴더 목록 검색 */
    public List<Folder> findFolderListByOwner(Member member){
        return folderRepository.findAllByFolderOwner(member);
    }

    // 페이징 함수 (limit= 한 페이지당 스크랩 수)
    private ScrapListResponseDto paging (List<Scrap> scraps, Long page, int limit) {
        int size = scraps.size();

        // 총 페이지 개수 계산
        Long lastPage = size/10L;
        if(size%limit != 0) lastPage++;

        // 전달받은 페이지에 맞게 리스트 생성
        int start = (int)((page-1)*limit);
        List<Scrap> result = new ArrayList<>();
        try{
            if(page == lastPage)
                for(int i=start; i<size; i++) result.add(scraps.get(i));
            else
                for(int i= start; i<start+limit; i++) result.add(scraps.get(i));
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("########## size: " + size + ", thisPage: " + page + "lastPage: " + lastPage);
        }

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
