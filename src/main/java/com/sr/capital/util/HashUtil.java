package com.sr.capital.util;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;


public class HashUtil {

    private static final String RNG_ALGORITHM = "SHA1PRNG";
    private static final String RNG_ALGORITHM_PROVIDER = "SUN";

    public static String hmacSha256Encryption(String message, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes("UTF-8")));
    }

    public static String generateResourceHash(String resource) {
        return DigestUtils.sha256Hex(resource);
    }


    public static String generateEmailVerificationData() throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom sr = SecureRandom.getInstance(RNG_ALGORITHM, RNG_ALGORITHM_PROVIDER);
        sr.setSeed(sr.generateSeed(12));
        byte[] verificationToken = new byte[12];
        sr.nextBytes(verificationToken);
        return CoreUtil.encodeHexString(verificationToken);
    }

    public static Long generateRandomUserId() throws NoSuchAlgorithmException, NoSuchProviderException {
        return ThreadLocalRandom.current().nextLong(50000000L, Long.MAX_VALUE);
    }







}

