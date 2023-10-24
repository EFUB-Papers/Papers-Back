package efub.toy2.papers.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 예외 처리 예시 */
    NO_MEMBER_EXIST(HttpStatus.BAD_REQUEST , "가입되지 않은 회원입니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST,"이미 좋아요를 누른 스크랩입니다.");




    private final HttpStatus status;
    private final String message;

}
