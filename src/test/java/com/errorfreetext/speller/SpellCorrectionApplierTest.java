package com.errorfreetext.speller;

import static org.assertj.core.api.Assertions.assertThat;

import com.errorfreetext.speller.dto.SpellError;
import java.util.List;
import org.junit.jupiter.api.Test;

class SpellCorrectionApplierTest {

    private final SpellCorrectionApplier applier = new SpellCorrectionApplier();

    @Test
    void appliesFirstSuggestionFromEndToStart() {
        List<SpellError> errors = List.of(
                new SpellError(1, 0, 0, 0, 14, "синхрафазатрон", List.of("синхрофазотрон"))
        );

        String result = applier.apply("синхрафазатрон", errors);

        assertThat(result).isEqualTo("синхрофазотрон");
    }

    @Test
    void returnsOriginalTextWhenNoErrors() {
        assertThat(applier.apply("correct text", List.of())).isEqualTo("correct text");
    }
}
