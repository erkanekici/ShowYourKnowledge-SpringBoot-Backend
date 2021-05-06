package com.util;

import java.util.concurrent.ThreadLocalRandom;
public class RandomGenerator {

    private static RandomGenerator _this = null;

    public static RandomGenerator getInstance() {
        if (_this == null)
            _this = new RandomGenerator();
        return _this;
    }

    public String generateRandom() {
        return String.valueOf(System.currentTimeMillis() + ThreadLocalRandom.current().nextInt());
    }}

