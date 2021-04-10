package io.github.thanhduybk.common.payload.response;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class RejectResponse implements Serializable {
    private String message;
    private String exception;
    private String path;
    private String debugMessage;
    private Map<String, String> errors = new LinkedHashMap<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
