package com.shop_app.configs.local;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocalizationUtils {
    private static MessageSource messageSource;

    public LocalizationUtils(MessageSource messageSource) {
        LocalizationUtils.messageSource = messageSource;
    }

    public static String getLocalizedMessage(String messageKey, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, args, locale);
    }
}