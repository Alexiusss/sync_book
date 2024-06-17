package com.example.sync_book.model;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

public enum Genre {
    FICTION,
    NON_FICTION,
    SCIENCE,
    HISTORY,
    FANTASY,
    DETECTIVE,
    ADVENTURE,
    GUIDE;

    private static ResourceBundleMessageSource messageSource;

    public static void setMessageSource(ResourceBundleMessageSource messageSource) {
        Genre.messageSource = messageSource;
    }

    public String getLocalizedName() {
        return messageSource.getMessage("genre." + this.name().toLowerCase(), null, LocaleContextHolder.getLocale());
    }
}