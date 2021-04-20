package io.github.thanhduybk.common.factory;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageFactory {
    private final MessageSource messageSource;

    public MessageFactory(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key, Object... params) {
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }

    public String getMessageRecursive(String key, Object... subKeys) {
        if (subKeys != null && subKeys.length > 0) {
            Object[] params = new Object[subKeys.length];

            for (int i = 0, subKeysLength = subKeys.length; i < subKeysLength; i++) {
                String subKey = (String) subKeys[i];
                params[i] = getMessage(subKey);
            }

            return getMessage(key, params);
        }

        return getMessage(key);
    }
}
