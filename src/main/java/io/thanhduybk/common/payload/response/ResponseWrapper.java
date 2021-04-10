package io.thanhduybk.common.payload.response;

import java.io.Serializable;

/**
 * The response wrapper that used commonly thru whole NextEDU system
 *
 * @param <T> data type
 */
public class ResponseWrapper<T> implements Serializable {
    private ResponseCode code;
    private T data;

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
