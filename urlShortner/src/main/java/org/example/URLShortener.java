package org.example;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class URLShortener {

    private Map<String, String> urlMappings = new HashMap<>();

    public String shortenURL(String longURL) {
        String hashedURL = DigestUtils.sha256Hex(longURL);
        String shortURL = base62Encode(hashedURL);
        urlMappings.put(shortURL, longURL);
        return shortURL;
    }

    public String expandURL(String shortURL) {
        return urlMappings.get(shortURL);
    }
    private String base62Encode(String input) {
        String base62Chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String truncatedHash = input.substring(0, 12);
        long value = Long.parseLong(truncatedHash, 16);
        StringBuilder encoded = new StringBuilder();

        while (value > 0) {
            int remainder = (int) (value % 62);
            encoded.append(base62Chars.charAt(remainder));
            value /= 62;
        }

        return encoded.reverse().toString();
    }
}