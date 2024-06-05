package com.sr.capital.helpers.strategy;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class CustomSnakeCaseStrategy extends PropertyNamingStrategies.SnakeCaseStrategy {

    @Override
    public String translate(String input) {
        String snakeCase = super.translate(input);
        // Add underscore before numbers
        return snakeCase.replaceAll("(?<=\\p{L})(?=\\p{N})", "_");
    }
}
