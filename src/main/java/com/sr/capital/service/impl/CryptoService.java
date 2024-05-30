package com.sr.capital.service.impl;


import com.sr.capital.exception.custom.DecryptionException;
import com.sr.capital.exception.custom.RSAKeysNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

@Component
public class CryptoService {


    @Autowired
    private RSAUtilityService utilityService;

    public String decryptDataUsingRsaPrivateKey(String encryptedData) throws RSAKeysNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, DecryptionException {
        if(!StringUtils.hasText(encryptedData)){
            return null;
        }
        PrivateKey privateKey = utilityService.getPrivateKey();
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        OAEPParameterSpec parameterSpec = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey, parameterSpec);

        try {
            byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedDataBytes = rsaCipher.doFinal(encryptedDataBytes);
            return new String(decryptedDataBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptionException();
        }
    }


//    public String generateInvitationToken(UserSignUpPreFilledData request) throws JWTCreationException {
//        Algorithm algorithm = Algorithm.HMAC256(identityAppProperties.getJwtSecret());
//        Date iat = Date.from(Instant.now());
//        Date exp = Date.from(Instant.now().plus(identityAppProperties.getInvitationTokenExpiry(), ChronoUnit.HOURS));
//        String parentId = request.getParentId().toString();
//        return JWT.create()
//                .withClaim(UserSignUpPrefilledFields.EMAIL, request.getEmail())
//                .withClaim(UserSignUpPrefilledFields.PARENT_ID, parentId)
//                .withClaim(UserSignUpPrefilledFields.TENANT_ID, request.getTenantId())
//                .withClaim(UserSignUpPrefilledFields.USER_GROUP_NAME, request.getUserGroupName())
//                .withClaim(UserSignUpPrefilledFields.USER_GROUP_ID, request.getUserGroupId().toString())
//                .withIssuedAt(iat)
//                .withExpiresAt(exp)
//                .withIssuer(identityAppProperties.getJwtIssuer())
//                .sign(algorithm);
//    }


//    public UserSignUpPreFilledData decodeInvitationToken(String token) throws CustomException {
//        try{
//            DecodedJWT jwt = JWT.decode(token);
//            UserSignUpPreFilledData preFilledData = UserSignUpPreFilledData.builder().build();
//            if(Date.from(Instant.now()).after(jwt.getExpiresAt())) {
//                throw new ExpiredResourceException();
//            }
//            //TODO: Find a better way to do it
//            if(!isClaimNull(jwt.getClaim(UserSignUpPrefilledFields.EMAIL)))
//                preFilledData.setEmail(jwt.getClaim(UserSignUpPrefilledFields.EMAIL).asString());
//            if(!isClaimNull(jwt.getClaim(UserSignUpPrefilledFields.PARENT_ID)))
//                preFilledData.setParentId(UUID.fromString(jwt.getClaim(UserSignUpPrefilledFields.PARENT_ID).asString()));
//            if(!isClaimNull(jwt.getClaim(UserSignUpPrefilledFields.TENANT_ID)))
//                preFilledData.setTenantId(jwt.getClaim(UserSignUpPrefilledFields.TENANT_ID).asString());
//            if(!isClaimNull(jwt.getClaim(UserSignUpPrefilledFields.USER_GROUP_ID)))
//                preFilledData.setUserGroupId(UUID.fromString(jwt.getClaim(UserSignUpPrefilledFields.USER_GROUP_ID).asString()));
//            if(!isClaimNull(jwt.getClaim(UserSignUpPrefilledFields.USER_GROUP_NAME)))
//                preFilledData.setUserGroupName(jwt.getClaim(UserSignUpPrefilledFields.USER_GROUP_NAME).asString());
//
//            return preFilledData;
//
//        } catch (JWTDecodeException exception) {
//            throw new InvalidResourceException();
//        }
//    }

//    private boolean isClaimNull(Claim claim){
//        return claim.getClass() == NullClaim.class;
//    }

}
