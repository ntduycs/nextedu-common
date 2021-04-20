package io.github.thanhduybk.common.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {
    private ResponseCode code;
    private String message;
    private T data;
    private String exception;
    private String path;
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    private final Map<String, Object> errors = new HashMap<>();

    public ResponseCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getException() {
        return exception;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public static <T> Response<T> getInstance() {
        return new Response<>();
    }

    public Response<T> code(ResponseCode code) {
        this.code = code;
        return this;
    }

    public Response<T> message(String message) {
        this.message = message;
        return this;
    }

    public Response<T> data(T data) {
        this.data = data;
        return this;
    }

    public Response<T> exception(String exName) {
        this.exception = exName;
        return this;
    }

    public Response<T> exception(Throwable ex) {
        this.exception = ex.getClass().getSimpleName();
        return this;
    }

    public Response<T> path(String path) {
        this.path = path;
        return this;
    }

    public Response<T> errors(Map<String, Object> errors) {
        this.errors.putAll(errors);
        return this;
    }

    public Response<T> error(String err, String message) {
        this.errors.put(err, message);
        return this;
    }
}
