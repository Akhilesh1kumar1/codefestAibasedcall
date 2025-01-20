package com.sr.capital.external.truthscreen.controller;

import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.service.IdService;
import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.*;



@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/idsearch")
public class IdSearchController {

    private final IdService idService;

    @PostMapping
    public IdSearchResponseDto sendRequest(@RequestBody IdSearchRequestDto requestDTO) throws Exception {
        IdSearchResponseDto response = idService.sendRequest(requestDTO);
        return response;
    }

}
