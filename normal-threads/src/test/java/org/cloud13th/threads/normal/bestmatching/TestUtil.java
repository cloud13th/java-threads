package org.cloud13th.threads.normal.bestmatching;

import org.cloud13th.threads.normal.CommonTestUtil;
import org.cloud13th.threads.normal.bestmatching.common.WordsLoader;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

class TestUtil extends CommonTestUtil {

    protected static final String TARGET_WORD = "sds";

    protected static List<String> DICTIONARY;

    @BeforeAll
    static void init() {
        DICTIONARY = WordsLoader.load("data/UK-Advanced-Cryptics-Dictionary.txt");
        System.out.println("Dictionary Size: " + DICTIONARY.size());
    }
}
