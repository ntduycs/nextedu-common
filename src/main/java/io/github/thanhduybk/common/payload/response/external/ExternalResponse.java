package io.github.thanhduybk.common.payload.response.external;

import io.github.thanhduybk.common.payload.response.ResponseCode;

import java.util.LinkedHashMap;

public class ExternalResponse {
    private ResponseCode code;
    private String message;
    private LinkedHashMap<String, Object> originalResponse = new LinkedHashMap<>();

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LinkedHashMap<String, Object> getOriginalResponse() {
        return originalResponse;
    }

    public void setOriginalResponse(LinkedHashMap<String, Object> originalResponse) {
        this.originalResponse = originalResponse;
    }
}
