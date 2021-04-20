package io.github.thanhduybk.common.payload.response;

import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum ResponseCode {
    // 2xx
    OK(200),
    ACCEPTED(202),
    NO_CONTENT(204),

    // 4xx
    NOT_FOUND(404),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    BAD_REQUEST(400),

    // 5xx
    UNEXPECTED_ERROR(500),
    ;

    private final int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus toHttpStatus() {
        return HttpStatus.valueOf(code);
    }

    public static boolean isErrorCode(ResponseCode responseCode) {
        return responseCode.getCode() >= 400;
    }

    public static ResponseCode fromHttpStatus(int statusCode) {
        return Arrays.stream(values())
                .filter(responseCode -> responseCode.getCode() == statusCode)
                .findFirst()
                .orElse(null);
    }
}
