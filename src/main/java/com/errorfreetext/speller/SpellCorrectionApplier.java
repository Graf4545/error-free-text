package com.errorfreetext.speller;

import com.errorfreetext.speller.dto.SpellError;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SpellCorrectionApplier {

    public String apply(String text, List<SpellError> errors) {
        if (errors == null || errors.isEmpty()) {
            return text;
        }

        StringBuilder corrected = new StringBuilder(text);
        errors.stream()
                .filter(error -> error.s() != null && !error.s().isEmpty())
                .sorted(Comparator.comparingInt(SpellError::pos).reversed())
                .forEach(error -> {
                    int start = error.pos();
                    int end = start + error.len();
                    if (start >= 0 && end <= corrected.length()) {
                        corrected.replace(start, end, error.s().get(0));
                    }
                });

        return corrected.toString();
    }
}
