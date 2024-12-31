package com.sr.capital.external.crif.util;

import com.sr.capital.entity.mongo.crif.CrifConsentDetails;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class StringUtils {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Generic method to create a pipe-separated string for any object
    public static <T> String toPipeSeparatedString(T obj) {
        if (obj == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        try {

            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value != null) {
                    result.append(value.toString()).append("|");
                } else {
                    result.append("|");
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing fields", e);
        }

        if (result.charAt(result.length() - 1) == '|') {
            return result.substring(0, result.length() - 1);
        }
        return result.toString();
    }

    /**
     * Method to get the time after given months from the current time.
     *
     * @return A string representing the date-time given months from now in the format "yyyy-MM-dd HH:mm:ss"
     */
    public static String getTimeAfterMonths(int month) {
        // Get the current date-time
        LocalDateTime currentTime = LocalDateTime.now();

        // Calculate the date-time after given months
        LocalDateTime sixMonthsLater = currentTime.plusMonths(month);

        // Format and return the result as a string
        return sixMonthsLater.format(FORMATTER);
    }

}