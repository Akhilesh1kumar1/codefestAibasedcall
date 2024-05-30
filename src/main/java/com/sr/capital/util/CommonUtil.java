package com.sr.capital.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import static com.sr.capital.helpers.constants.Constants.Separators.SLASH_SEPARATOR;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.UTILITY_INSTANTIATION_ERROR;

public class CommonUtil {

    private CommonUtil() {
        throw new AssertionError(UTILITY_INSTANTIATION_ERROR);
    }

    public static String getFolderString(String... uploadLocationIdentifiers) {
        return Arrays.stream(uploadLocationIdentifiers).reduce((x, y) -> x + SLASH_SEPARATOR + y).orElse(null);
    }

    public static double getValueWithPrecision(double value, int precision) {
        return BigDecimal.valueOf(value)
                .setScale(precision, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
