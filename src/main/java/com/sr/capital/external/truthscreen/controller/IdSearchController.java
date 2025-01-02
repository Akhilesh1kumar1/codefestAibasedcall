package com.sr.capital.external.truthscreen.controller;

import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.service.PanService;
import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.*;



@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/idsearch")
public class IdSearchController {

    private final PanService panService;

    @PostMapping
    public IdSearchResponseDto sendRequest(@RequestBody IdSearchRequestDto requestDTO) throws RequestTransformerNotFoundException {
        IdSearchResponseDto response = panService.sendPanRequest(requestDTO);
        return response;
    }

}
