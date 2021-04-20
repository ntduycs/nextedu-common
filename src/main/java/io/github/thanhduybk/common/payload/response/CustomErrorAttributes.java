package io.github.thanhduybk.common.payload.response;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        final Map<String, Object> defaults = super.getErrorAttributes(webRequest, options);

        final Map<String, Object> customs = new HashMap<>();

        int status = (int) defaults.get("status");

        customs.put("code", status < 500 ? ResponseCode.BAD_REQUEST : ResponseCode.UNEXPECTED_ERROR);
        customs.put("message", defaults.get("message"));

        final String exception = (String) defaults.get("exception");

        if (defaults.containsKey("exception")) {
            customs.put("exception", exception.substring(exception.lastIndexOf(".") + 1));
        }

        customs.put("path", defaults.get("path"));

        return customs;
    }
}
