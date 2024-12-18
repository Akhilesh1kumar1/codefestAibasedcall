package com.sr.capital.external.truthscreen.test;


import com.sr.capital.config.AppProperties;
import com.sr.capital.external.truthscreen.util.TruthScreenUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
class TestAuthController {

    final AppProperties appProperties;
    private final TestEncryptionService encryptionService;


    @PostMapping("/encrypt")
    public String encryptRequest(@RequestBody TestEncryptRequestDto input) throws Exception {
        String iv = TruthScreenUtility.getIV();
        String encryptedData = TruthScreenUtility.encrypt(appProperties.getAuthBridgePassword(), iv, input);
        return encryptedData;
    }

    @PostMapping("/decrypt")
    public TestEncryptRequestDto decryptResponse(@RequestBody TestDecryptRequestDto encryptedResponse) throws Exception {
        //String iv = TruthScreenUtility.getIV();
        TestEncryptRequestDto encryptedData = TruthScreenUtility.decrypt(appProperties.getAuthBridgePassword(),encryptedResponse.getResponseData(), TestEncryptRequestDto.class);
        return encryptedData;
    }

    @GetMapping("/HI")
    public String SAY(){
        return "hoooooooo";
    }
}
