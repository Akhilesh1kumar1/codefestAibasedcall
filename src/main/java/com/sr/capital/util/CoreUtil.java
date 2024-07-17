package com.sr.capital.util;

import com.sr.capital.exception.custom.InvalidMobileException;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sr.capital.helpers.constants.Constants.MOBILE_REGEX;

public class CoreUtil {

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    public static String encodeHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte b : byteArray) {
            hexStringBuffer.append(byteToHex(b));
        }
        return hexStringBuffer.toString();
    }

    public static String getFullName(String firstName, String lastName, String defaultName) {
        String name = StringUtils.hasText(firstName) ?  firstName : "";
        name += StringUtils.hasText(name) ? " " + (StringUtils.hasText(lastName) ? lastName : "") : defaultName;
        return name;
    }

    public static String getFirstName(String fullName){
        if(!StringUtils.hasText(fullName.trim())) {
            return null;
        }
        String[] splitName = fullName.trim().split(" ");
        if(splitName.length == 1) {
            return fullName.trim();
        }
        StringBuilder firstName = new StringBuilder();
        for(int i = 0; i < splitName.length - 1; i++) {
            if(StringUtils.hasText(splitName[i].trim())) {
                firstName.append(splitName[i].trim()).append(" ");
            }
        }
        return firstName.toString().trim();
    }

    public static String getLastName(String fullName){
        if(!StringUtils.hasText(fullName.trim())) {
            return null;
        }
        String[] splitName = fullName.trim().split(" ");
        if(splitName.length == 1) {
            return null;
        }
        return splitName[splitName.length - 1].trim();
    }



    public static byte[] decodeHexString(String hex) {
        byte[] bArray = new BigInteger("10" + hex,16).toByteArray();
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = bArray[i+1];
        return ret;
    }

    public static String getRandomAlphaNumericString(int length)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public static void validateMobile(String mobile) throws InvalidMobileException {
        Matcher mobileMatcher = Pattern.compile(MOBILE_REGEX).matcher(mobile);
        if(!mobileMatcher.matches()){
            throw new InvalidMobileException();
        }
    }

    public static UUID getUUIDID(Object id) {

        UUID loanId = null;
        if (id instanceof byte[]) {
            byte[] loanIdBytes = (byte[]) id;
            loanId = uuidFromBytes(loanIdBytes);
        } else if (id instanceof UUID) {
            loanId = (UUID) id;
        } else if (id instanceof String) {
            loanId = UUID.fromString((String) id);
        } else {
            // Handle other cases or throw an error if necessary
            throw new IllegalArgumentException("Unsupported loanId type: " + id.getClass());
        }
        return loanId;
    }

    public static UUID uuidFromBytes(byte[] bytes) {
        long msb = 0;
        long lsb = 0;
        assert bytes.length == 16;
        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (bytes[i] & 0xff);
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (bytes[i] & 0xff);
        return new UUID(msb, lsb);
    }

}
