package com.sr.capital.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.GeneralSecurityException;
import java.util.Random;

public class OtpGenerator {

    private static final int OTP_LENGTH = 6;

    private static final String SEED = "3132333435363738393031323334353637383930313233343536373839303132";

    private static final int[] DIGITS_POWER = {1,10,100,1000,10000,100000,1000000,10000000,100000000};

    private static final String ALGORITHM = "HmacSHA256";

    private static byte[] hmac_sha(byte[] keyBytes, byte[] text){
        try {
            Mac hmac;
            hmac = Mac.getInstance(OtpGenerator.ALGORITHM);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    public static String generateOTP(){
        Random random = new Random();
        String result = null;
        long a = System.currentTimeMillis() + random.nextLong();
        String time = Long.toHexString(a).toUpperCase();

        byte[] msg = CoreUtil.decodeHexString(time);
        byte[] k = CoreUtil.decodeHexString(SEED);

        byte[] hash = hmac_sha(k, msg);

        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) |
                ((hash[offset + 1] & 0xff) << 16) |
                ((hash[offset + 2] & 0xff) << 8) |
                (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[OTP_LENGTH];

        result = Integer.toString(otp);
        while (result.length() < OTP_LENGTH) {
            result = "0" + result;
        }
        return result;
    }
}
