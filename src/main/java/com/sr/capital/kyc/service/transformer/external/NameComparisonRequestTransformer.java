package com.sr.capital.kyc.service.transformer.external;

import com.sr.capital.kyc.external.request.verification.NameComparisonRequest;
import com.sr.capital.kyc.external.request.verification.data.NameComparisonData;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NameComparisonRequestTransformer {

    public NameComparisonRequest transformRequest(String name1, String name2) {
        NameComparisonData data = NameComparisonData.builder()
                .name1(name1)
                .name2(name2)
                .build();

        return NameComparisonRequest.builder()
                .taskId(UUID.randomUUID().toString())
                .groupId(UUID.randomUUID().toString())
                .data(data)
                .build();
    }
}
