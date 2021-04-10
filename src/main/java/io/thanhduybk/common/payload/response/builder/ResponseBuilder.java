package io.thanhduybk.common.payload.response.builder;

import io.thanhduybk.common.payload.response.RejectResponse;
import io.thanhduybk.common.payload.response.ResponseCode;
import io.thanhduybk.common.payload.response.ResponseWrapper;
import io.thanhduybk.common.payload.response.SimpleResponse;

public class ResponseBuilder {
    /**
     * Construct the common response wrapper with populated data and code
     *
     * @param data response data
     * @param code response code
     * @param <T>  response data type
     * @return response wrapper
     */
    private static <T> ResponseWrapper<T> build(T data, ResponseCode code) {
        ResponseWrapper<T> response = new ResponseWrapper<>();

        response.setCode(code);
        response.setData(data);

        return response;
    }

    public static ResponseWrapper<RejectResponse> reject(RejectResponse data, ResponseCode code) {
        if (!ResponseCode.isErrorCode(code)) {
            throw new IllegalArgumentException("Invalid rejected code was provided " + code + " - " + code.getCode());
        }

        return build(data, code);
    }

    public static <T> ResponseWrapper<T> accept(T data, ResponseCode code) {
        if (ResponseCode.isErrorCode(code)) {
            throw new IllegalArgumentException("Invalid accepted code was provided " + code + " - " + code.getCode());
        }

        return build(data, code);
    }

    public static ResponseWrapper<SimpleResponse> accept(String message, ResponseCode code) {
        SimpleResponse response = new SimpleResponse();

        response.setMessage(message);

        return build(response, code);
    }
}
