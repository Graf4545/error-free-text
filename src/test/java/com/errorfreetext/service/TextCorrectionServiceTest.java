package com.errorfreetext.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.errorfreetext.domain.Language;
import com.errorfreetext.speller.SpellCorrectionApplier;
import com.errorfreetext.speller.SpellerOptionsCalculator;
import com.errorfreetext.speller.TextChunkSplitter;
import com.errorfreetext.speller.YandexSpellerClient;
import com.errorfreetext.speller.dto.SpellError;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TextCorrectionServiceTest {

    @Mock
    private TextChunkSplitter textChunkSplitter;
    @Mock
    private SpellerOptionsCalculator spellerOptionsCalculator;
    @Mock
    private YandexSpellerClient yandexSpellerClient;
    @Mock
    private SpellCorrectionApplier spellCorrectionApplier;

    @InjectMocks
    private TextCorrectionService textCorrectionService;

    @Test
    void correctsEachChunkAndConcatenatesResult() {
        when(textChunkSplitter.split("one two")).thenReturn(List.of("one", " two"));
        when(spellerOptionsCalculator.calculate("one two")).thenReturn(0);
        when(yandexSpellerClient.checkText("one", "ru", 0)).thenReturn(List.of());
        when(yandexSpellerClient.checkText(" two", "ru", 0)).thenReturn(List.of());
        when(spellCorrectionApplier.apply("one", List.of())).thenReturn("one");
        when(spellCorrectionApplier.apply(" two", List.of())).thenReturn(" two");

        String result = textCorrectionService.correct("one two", Language.RU);

        assertThat(result).isEqualTo("one two");
        verify(yandexSpellerClient).checkText(eq("one"), eq("ru"), eq(0));
    }
}
