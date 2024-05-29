package com.sr.capital.service.impl;


import com.sr.capital.entity.RSAKeys;
import com.sr.capital.exception.custom.RSAKeysNotFoundException;
import com.sr.capital.repository.RSAKeysRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@AllArgsConstructor
public class RSAUtilityService {


    final RSAKeysRepo rsaKeysRepo;

    public void generateRsaKeyPairs() throws NoSuchAlgorithmException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        RSAKeys rsaKeysEntity = rsaKeysRepo.findKeyPair();

        if(rsaKeysEntity == null){
            rsaKeysEntity = RSAKeys.builder()
                    .publicKey(publicKeyString)
                    .privateKey(privateKeyString)
                    .build();
        } else {
            rsaKeysEntity.setPublicKey(publicKeyString);
            rsaKeysEntity.setPrivateKey(privateKeyString);
        }

        rsaKeysRepo.save(rsaKeysEntity);
    }

    public PublicKey getPublicKey() throws RSAKeysNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAKeys rsaKeysEntity = rsaKeysRepo.findKeyPair();
        if(rsaKeysEntity == null){
            throw new RSAKeysNotFoundException();
        }
        byte[] keyBytes = decodeKeyString(rsaKeysEntity.getPublicKey());
        return getPublicKeyFromByteStream(keyBytes);
    }

    public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, RSAKeysNotFoundException {
        RSAKeys rsaKeysEntity = rsaKeysRepo.findKeyPair();
        if(rsaKeysEntity == null){
            throw new RSAKeysNotFoundException();
        }
        byte[] keyBytes = decodeKeyString(rsaKeysEntity.getPrivateKey());
        return getPrivateKeyFromByteStream(keyBytes);
    }

    public String getPublicKeyString() throws RSAKeysNotFoundException {
        RSAKeys rsaKeysEntity = rsaKeysRepo.findKeyPair();
        if(rsaKeysEntity == null){
            throw new RSAKeysNotFoundException();
        }
        return rsaKeysEntity.getPublicKey();
    }

    private byte[] decodeKeyString(String key) {
        return Base64.getDecoder().decode(key);
    }

    private PublicKey getPublicKeyFromByteStream(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return kf.generatePublic(keySpec);
    }

    private PrivateKey getPrivateKeyFromByteStream(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return kf.generatePrivate(keySpec);
    }
}