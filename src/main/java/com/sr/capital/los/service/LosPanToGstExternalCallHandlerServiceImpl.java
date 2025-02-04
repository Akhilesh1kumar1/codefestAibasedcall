package com.sr.capital.los.service;

import com.sr.capital.config.AppProperties;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.service.IdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class LosPanToGstExternalCallHandlerServiceImpl {

    final AppProperties appProperties;
    final IdService idService;


    public void handleLOSExternalCall(String losUserId) {

//        IdSearchRequestDto requestDTO = ;
//        idService.sendRequest(requestDTO);
    }
}
