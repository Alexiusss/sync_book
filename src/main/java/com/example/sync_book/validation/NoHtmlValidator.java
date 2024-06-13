package com.example.sync_book.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class NoHtmlValidator implements ConstraintValidator<NoHtml, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // https://www.tutorialspoint.com/jsoup/jsoup_sanitize_html.htm
        return value == null || Jsoup.isValid(value, Safelist.none());
    }
}