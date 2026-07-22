package com.errorfreetext.speller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TextChunkSplitterTest {

    private final TextChunkSplitter splitter = new TextChunkSplitter();

    @Test
    void returnsSingleChunkForShortText() {
        assertThat(splitter.split("short text")).containsExactly("short text");
    }

    @Test
    void splitsLongTextIntoChunksNotExceedingLimit() {
        String word = "a".repeat(100);
        String text = (word + " ").repeat(120).trim();

        var chunks = splitter.split(text);

        assertThat(chunks).hasSizeGreaterThan(1);
        assertThat(chunks).allSatisfy(chunk -> assertThat(chunk.length()).isLessThanOrEqualTo(TextChunkSplitter.MAX_CHUNK_SIZE));
        assertThat(String.join("", chunks).replace(" ", "")).hasSize(text.replace(" ", "").length());
    }
}
