package org.matrix.izumbankapp.util;

import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class GenerateRandom {
    private final Random random = new Random();

    public static String generateAccountNumber() {
        return generateRandomNumber(7);
    }

    public static String generateCif() {
        return generateRandomNumber(5);
    }

    private static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
