package com.errorfreetext.service;

import com.errorfreetext.domain.Language;
import com.errorfreetext.exception.SpellerApiException;
import com.errorfreetext.speller.SpellerOptionsCalculator;
import com.errorfreetext.speller.SpellCorrectionApplier;
import com.errorfreetext.speller.TextChunkSplitter;
import com.errorfreetext.speller.YandexSpellerClient;
import com.errorfreetext.speller.dto.SpellError;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TextCorrectionService {

    private static final Logger log = LoggerFactory.getLogger(TextCorrectionService.class);

    private final TextChunkSplitter textChunkSplitter;
    private final SpellerOptionsCalculator spellerOptionsCalculator;
    private final YandexSpellerClient yandexSpellerClient;
    private final SpellCorrectionApplier spellCorrectionApplier;

    public TextCorrectionService(
            TextChunkSplitter textChunkSplitter,
            SpellerOptionsCalculator spellerOptionsCalculator,
            YandexSpellerClient yandexSpellerClient,
            SpellCorrectionApplier spellCorrectionApplier
    ) {
        this.textChunkSplitter = textChunkSplitter;
        this.spellerOptionsCalculator = spellerOptionsCalculator;
        this.yandexSpellerClient = yandexSpellerClient;
        this.spellCorrectionApplier = spellCorrectionApplier;
    }

    public String correct(String text, Language language) {
        List<String> chunks = textChunkSplitter.split(text);
        int options = spellerOptionsCalculator.calculate(text);
        String lang = language.toSpellerLang();

        log.info("Correcting text in {} chunk(s), language={}, options={}", chunks.size(), lang, options);

        StringBuilder result = new StringBuilder(text.length());
        for (String chunk : chunks) {
            try {
                List<SpellError> errors = yandexSpellerClient.checkText(chunk, lang, options);
                result.append(spellCorrectionApplier.apply(chunk, errors));
            } catch (SpellerApiException ex) {
                log.error("Yandex Speller failed for chunk", ex);
                throw ex;
            }
        }
        return result.toString();
    }
}
