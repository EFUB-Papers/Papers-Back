package efub.toy2.papers.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 예외 처리 예시 */
    NO_MEMBER_EXIST(HttpStatus.BAD_REQUEST , "가입되지 않은 회원입니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST,"이미 좋아요를 누른 스크랩입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST,"만료된 토큰입니다." ),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"유효하지 않은 토큰입니다." ),
    NON_LOGIN(HttpStatus.BAD_REQUEST,"로그인이 필요합니다." ),
    INVALID_MEMBER(HttpStatus.BAD_REQUEST,"접근 권한이 없는 회원입니다."),

    NO_SCRAP_EXIST(HttpStatus.BAD_REQUEST,"존재하지 않는 스크랩입니다."),

    NO_COMMENT_EXIST(HttpStatus.BAD_REQUEST,"존재하지 않는 댓글입니다."),

    NO_REPLY_EXIST(HttpStatus.BAD_REQUEST,"존재하지 않는 대댓글입니다.")
    ;




    private final HttpStatus status;
    private final String message;

}
