package io.github.thanhduybk.common.payload.response;

import java.io.Serializable;

public class SimpleResponse implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
