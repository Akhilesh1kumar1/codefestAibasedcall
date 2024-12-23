package com.sr.capital.external.crif.util;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.entity.mongo.crif.CrifUserModel;
import com.sr.capital.repository.mongo.CrifUserModelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrifUserModelHelper {
    private final CrifUserModelRepo crifUserModelRepo;
    private final AES256 aes256;

    public Optional<CrifUserModel> findByMobile(String mobile) {
        String encrypt = aes256.encrypt(mobile);
        return crifUserModelRepo.findByMobile(encrypt);
    }



}
