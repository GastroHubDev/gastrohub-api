package br.com.fiap.gastrohubapi.infrastructure.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public final class TextNormalizer {

    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}");

    private TextNormalizer() {}

    public static boolean containsIgnoreCaseAndAccents(String text, String term) {
        return normalize(text).contains(normalize(term));
    }

    private static String normalize(String value) {
        String decomposed = Normalizer.normalize(value, Normalizer.Form.NFD);
        return DIACRITICS.matcher(decomposed).replaceAll("").toLowerCase();
    }
}
