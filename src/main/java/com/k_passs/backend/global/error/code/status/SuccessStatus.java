package com.k_passs.backend.global.error.code.status;

import com.k_passs.backend.global.error.code.BaseCode;
import com.k_passs.backend.global.error.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    //Common
    OK(HttpStatus.OK, "COMMON_200", "성공입니다."),

    // Bookmark
    BOOKMARK_STATUS_CHANGED(HttpStatus.OK, "BOOKMARK_200","북마크 상태가 성공적으로 변경되었습니다."),

    // Tip
    TIP_SUCCESS(HttpStatus.OK,"TIP_200", "꿀팁이 성공적으로 조회되었습니다."),
    TIP_ALL_SUCCESS(HttpStatus.OK,"TIP_200", "전체 꿀팁이 성공적으로 조회되었습니다."),

    // User
    USER_GET_SUCCESS(HttpStatus.OK,"USER_200","회원 정보 조회가 성공적으로 조회되었습니다."),
    USER_UPDATE_NAME(HttpStatus.CREATED, "USER_201","회원 닉네임 수정이 성공적으로 수정되었습니다."),
    USER_GET_TIPS(HttpStatus.OK, "USER_202", "회원이 찜한 꿀팁이 조회되었습니다."),
    USER_GET_CHALLENGE(HttpStatus.OK,"USER_203","회원이 완료한 챌린지가 조회되었습니다.")
    ;



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
