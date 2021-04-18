package io.github.thanhduybk.common.factory;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {
    private final MessageSource messageSource;

    public MessageFactory(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key, Object... params) {
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }

    public String getMessageRecursive(String key, String subKey) {
        return getMessage(key, getMessage(subKey));
    }
}
