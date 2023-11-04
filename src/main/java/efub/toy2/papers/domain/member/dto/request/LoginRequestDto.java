package efub.toy2.papers.domain.member.dto.request;


import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LoginRequestDto {
    @NotNull
    private String code;
}
