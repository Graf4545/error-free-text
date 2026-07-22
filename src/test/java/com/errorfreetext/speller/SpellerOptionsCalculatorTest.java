package com.errorfreetext.speller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SpellerOptionsCalculatorTest {

    private final SpellerOptionsCalculator calculator = new SpellerOptionsCalculator();

    @Test
    void returnsZeroWhenNoDigitsOrUrls() {
        assertThat(calculator.calculate("hello world")).isZero();
    }

    @Test
    void enablesIgnoreDigitsWhenTextContainsDigits() {
        assertThat(calculator.calculate("room 42")).isEqualTo(SpellerOptions.IGNORE_DIGITS);
    }

    @Test
    void enablesIgnoreUrlsWhenTextContainsUrl() {
        assertThat(calculator.calculate("see https://example.com now"))
                .isEqualTo(SpellerOptions.IGNORE_URLS);
    }

    @Test
    void combinesOptionsWhenDigitsAndUrlsPresent() {
        assertThat(calculator.calculate("visit https://example.com page 2"))
                .isEqualTo(SpellerOptions.IGNORE_DIGITS | SpellerOptions.IGNORE_URLS);
    }
}
