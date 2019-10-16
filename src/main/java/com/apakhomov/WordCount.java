package com.apakhomov;

import com.google.common.annotations.VisibleForTesting;
import one.util.streamex.StreamEx;

import java.sql.Connection;
import java.util.Collections;
import java.util.Map;

public class WordCount {
    private TextProvider provider;

    public WordCount(TextProvider provider) {
        this.provider = provider;
    }

    public Map<String, Integer> countWords() {
        String text = provider.getText();
        Map<String, Integer> map = countWords(text);
//        map.put("ooo", 1);
        provider.close(text);
        return map;
    }

    @VisibleForTesting
    Map<String, Integer> countWords(String text) {
        if (text == null) {
            return null;
        }

        if (text.isEmpty()) {
            return Collections.emptyMap();
        }

        String withoutPunctuals = text.replaceAll("\\p{P}", "");
        String[] words = withoutPunctuals.split("\\s+");

        return countWords(words);
    }

    private Map<String, Integer> countWords(final String[] words) {
        return StreamEx.of(words)
                       .toMap(
                               w -> w,
                               w -> 1,
                               Integer::sum
                       );
    }
}
