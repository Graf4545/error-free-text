package com.errorfreetext.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CorrectionTextValidatorTest {

    private final CorrectionTextValidator validator = new CorrectionTextValidator();

    @Test
    void acceptsValidText() {
        assertThat(validator.isValid("hello world", null)).isTrue();
    }

    @Test
    void rejectsTooShortText() {
        assertThat(validator.isValid("ab", null)).isFalse();
    }

    @Test
    void rejectsOnlyDigitsAndSpecialCharacters() {
        assertThat(validator.isValid("123!!!", null)).isFalse();
    }

    @Test
    void acceptsNull() {
        assertThat(validator.isValid(null, null)).isTrue();
    }
}
