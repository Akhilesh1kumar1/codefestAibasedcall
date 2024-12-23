package com.sr.capital.external.crif.util;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.entity.mongo.crif.CrifReport;
import com.sr.capital.repository.mongo.CrifReportRepo;
import com.sr.capital.repository.mongo.CrifUserModelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrifReportModelHelper {
    private final CrifReportRepo crifReportRepo;
    private final AES256 aes256;

    public Optional<CrifReport> findByMobile(String mobile) {
        String encrypt = aes256.encrypt(mobile);
        return crifReportRepo.findByMobile(encrypt);
    }
}
