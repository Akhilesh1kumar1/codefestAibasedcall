package com.sr.capital.external.truthscreen.controller;

import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.service.PanService;
import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.*;



@RestController()
@RequiredArgsConstructor
//:TODO :- MIGHT NEED TO CHANGE THE NAME
@RequestMapping("/api/idsearch")
public class IdSearchController {

    private final PanService panService;

    //:TODO :- NEED TO REMOVE THE "PAN" ADD PATH Variables when implementing the Business APIs
    @PostMapping("/pan")
    public IdSearchResponseDto sendPanRequest(@RequestBody IdSearchRequestDto requestDTO) throws RequestTransformerNotFoundException {
        IdSearchResponseDto response = panService.sendPanRequest(requestDTO);
        return response;
    }

}
