package efub.toy2.papers.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* member */
    NO_MEMBER_EXIST(HttpStatus.BAD_REQUEST , "가입되지 않은 회원입니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST,"이미 좋아요를 누른 스크랩입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다." ),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰입니다." ),
    NON_LOGIN(HttpStatus.BAD_REQUEST,"로그인이 필요합니다." ),
    INVALID_MEMBER(HttpStatus.BAD_REQUEST,"접근 권한이 없는 회원입니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),

    /* scrap */
    NO_SCRAP_EXIST(HttpStatus.BAD_REQUEST,"존재하지 않는 스크랩입니다."),
    INVALID_SEARCHBY(HttpStatus.BAD_REQUEST , "유효하지 않는 검색기준입니다."),

    /* comment */
    NO_COMMENT_EXIST(HttpStatus.BAD_REQUEST,"존재하지 않는 댓글입니다."),

    /* reply */
    NO_REPLY_EXIST(HttpStatus.BAD_REQUEST,"존재하지 않는 대댓글입니다."),

    /* folder */
    NO_FOLDER_EXIST(HttpStatus.BAD_REQUEST,"존재하지 않는 폴더입니다."),
    DEFAULT_FOLDER_CANNOT_DELETE(HttpStatus.BAD_REQUEST,"기본 폴더는 삭제할 수 없습니다."),
    DEFAULT_FOLDER_CANNOT_CHANGE(HttpStatus.BAD_REQUEST,"기본 폴더의 이름은 변경할 수 없습니다."),
    FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST,"파일 업로드에 실패하였습니다." ),
    FILE_DELETE_ERROR(HttpStatus.BAD_REQUEST,"파일 삭제에 실패하였습니다." ),
    FILE_CONVERT_ERROR(HttpStatus.BAD_REQUEST,"파일 변한에 실패하였습니다." ),

    /* follow */
    NO_FOLLOW_EXIST(HttpStatus.BAD_REQUEST , "존재하지 않는 팔로우입니다."),
    ALREADY_FOLLOWED(HttpStatus.BAD_REQUEST,"이미 팔로우되어있는 회원입니다."),
    INVALID_FOLLOW(HttpStatus.BAD_REQUEST,"본인을 팔로우할 수 없습니다.")
    ;



    private final HttpStatus status;
    private final String message;

}
