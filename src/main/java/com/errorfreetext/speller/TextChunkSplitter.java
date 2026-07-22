package com.errorfreetext.speller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TextChunkSplitter {

    public static final int MAX_CHUNK_SIZE = 10_000;

    public List<String> split(String text) {
        if (text.length() <= MAX_CHUNK_SIZE) {
            return List.of(text);
        }

        List<String> chunks = new ArrayList<>();
        int offset = 0;
        while (offset < text.length()) {
            int end = Math.min(offset + MAX_CHUNK_SIZE, text.length());
            if (end < text.length()) {
                int splitAt = findSplitIndex(text, offset, end);
                chunks.add(text.substring(offset, splitAt));
                offset = splitAt;
            } else {
                chunks.add(text.substring(offset, end));
                offset = end;
            }
        }
        return chunks;
    }

    private int findSplitIndex(String text, int offset, int end) {
        for (int i = end; i > offset; i--) {
            if (Character.isWhitespace(text.charAt(i - 1))) {
                return i;
            }
        }
        return end;
    }
}
