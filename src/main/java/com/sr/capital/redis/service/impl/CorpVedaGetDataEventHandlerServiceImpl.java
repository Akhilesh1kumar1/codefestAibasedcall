package com.sr.capital.redis.service.impl;

import com.sr.capital.external.corpveda.dto.request.CorpVedaRequestDto;
import com.sr.capital.external.corpveda.enums.CorpVedaDocType;
import com.sr.capital.external.corpveda.service.impl.CorpVedaServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CorpVedaGetDataEventHandlerServiceImpl {

    @Autowired
    private CorpVedaServiceImpl corpVedaService;


    public void handleCorpVedaGetData(String payload) throws Exception {
        log.info("Handling CorpVedaGetDataEventHandlerServiceImpl");
        String[] parts = payload.split("%%");  //
        String referenceID = parts[2];
        CorpVedaRequestDto request = CorpVedaRequestDto.builder()
                .docType(CorpVedaDocType.CIN_SEARCH_GET_DATA.getValue())
                .docValue(referenceID).build();

        corpVedaService.processNewRequest(request);
        log.info("Successfully saved data in the database for referenceID :: " + referenceID);
    }

}
