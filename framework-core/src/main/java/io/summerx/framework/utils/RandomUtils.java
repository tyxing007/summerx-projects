package io.summerx.framework.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by xiayg on 7/11/2016.
 */
public class RandomUtils {

    public static final String ALPHA_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHA_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String ALPHA = ALPHA_UPPERCASE + ALPHA_LOWERCASE;
    public static final String NUMBER = "0123456789";
    public static final String ALPHA_NUMBER = ALPHA + NUMBER;

    public static String generateNumberSequence(int length) {
        return generateRandomSequence(NUMBER.toCharArray(), length);
    }
    public static String generateAlphaNumberSequence(int length) {
        return generateRandomSequence(ALPHA_NUMBER.toCharArray(), length);
    }
    public static String generateSequence(String strings, int length) {
        return generateRandomSequence(strings.toCharArray(), length);
    }
    public static String generateRandomSequence(char[] sequences, int length) {
        SecureRandom rnd = new SecureRandom(UUID.randomUUID().toString().getBytes());
        StringBuilder sequence = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sequence.append(sequences[rnd.nextInt(sequences.length)]);
        }
        return sequence.toString();
    }
}
