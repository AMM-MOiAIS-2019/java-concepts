package com.apakhomov


import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class WordCountSpec extends Specification {
    @Subject
    WordCount wc

    TextProvider provider = Mock()

    void setup() {
        wc = new WordCount(provider)
    }

    @Unroll
    def 'ddt test, input: `#input` expected result: `#expected`'(String input, Map<String, Integer> expected) {
        when:
        def actual = wc.countWords()

        then:
        1 * provider.text >> input
        1 * provider.close(_)

        expected == actual

        where:
        input       | expected
        null        | null
        ''          | [:]
        '1'         | ['1': 1]
        'my word'   | ['my': 1, 'word': 1]
        'my my w w' | ['my': 2, 'w': 2]
    }


    def "verify all example"() {
        given:
        def expected = ['lol': 1]

        when:
        def actual = wc.countWords('lol')

        then:
        verifyAll {
            expected == actual
            actual.size() == expected.size()
        }
    }

    def "NPE won't thrown"() {
        when:
        wc.countWords(null as String)

        then:
        notThrown(NullPointerException)
    }
}
