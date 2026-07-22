package com.errorfreetext.dto;

import com.errorfreetext.domain.Language;
import com.errorfreetext.validation.ValidCorrectionText;
import jakarta.validation.constraints.NotNull;

public class CreateTaskRequest {

    @NotNull(message = "Text is required")
    @ValidCorrectionText
    private String text;

    @NotNull(message = "Language is required")
    private Language language;

    public CreateTaskRequest() {
    }

    public CreateTaskRequest(String text, Language language) {
        this.text = text;
        this.language = language;
    }

    public String text() {
        return text;
    }

    public Language language() {
        return language;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
