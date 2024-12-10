package com.sr.capital.external.crif.util;

import com.sr.capital.config.AppProperties;
import com.sr.capital.external.crif.dto.request.AccessCodeDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringUtils {

    @Autowired
    AppProperties appProperties;

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

        // Remove the last trailing pipe if any
//        if (result.length() > 0 && result.charAt(result.length() - 1) == '|') {
//            result.deleteCharAt(result.length() - 1);
//        }

        return result.toString();
    }


}