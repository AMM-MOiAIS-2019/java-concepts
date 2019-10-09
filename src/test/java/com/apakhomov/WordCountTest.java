package com.apakhomov;

import com.google.common.collect.ImmutableMap;
import one.util.streamex.EntryStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordCountTest {
    WordCount wc;

    @Mock
    TextProvider provider;

    @BeforeEach
    void setUp() {
        wc = new WordCount(provider);
    }

    @Test
    @DisplayName("NPE won't be thrown")
    void nullString() {
        String given = null;

        assertDoesNotThrow(() -> wc.countWords(given));
    }

    @ParameterizedTest
    @MethodSource("provide")
    @DisplayName("Count words with provider dependency")
    void withProvider(String given, Map<String, Integer> expected) {
        when(provider.getText()).thenReturn(given);

        Map<String, Integer> actual = wc.countWords();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provide")
    @DisplayName("Count words single method")
    void countWords(String given, Map<String, Integer> expected) {
        Map<String, Integer> actual = wc.countWords(given);

        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> provide() {
        //key - given string,
        // value - expected Map<String, Integer>
        return EntryStream.of(
                "",
                ImmutableMap.of(),

                null,
                null,

                "my test words my",
                ImmutableMap.of("my", 2, "test", 1, "words", 1),

                "test",
                ImmutableMap.of("test", 1),

                "test \n test \t 1",
                ImmutableMap.of("test", 2, "1", 1),

                "test, test!!! 1",
                ImmutableMap.of("test", 2, "1", 1)

        ).mapKeyValue((k, v) -> Arguments.of(k, v));
    }
}
