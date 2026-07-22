package com.errorfreetext.speller;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class SpellerOptionsCalculator {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "(?i)\\b((https?://|www\\.)\\S+|\\S+@\\S+\\.\\S+)"
    );

    public int calculate(String text) {
        int options = 0;
        if (containsDigit(text)) {
            options |= SpellerOptions.IGNORE_DIGITS;
        }
        if (containsUrl(text)) {
            options |= SpellerOptions.IGNORE_URLS;
        }
        return options;
    }

    private boolean containsDigit(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isDigit(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsUrl(String text) {
        return URL_PATTERN.matcher(text).find();
    }
}
