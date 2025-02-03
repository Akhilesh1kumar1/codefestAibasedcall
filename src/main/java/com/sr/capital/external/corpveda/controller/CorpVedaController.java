package com.sr.capital.external.corpveda.controller;

import com.sr.capital.external.corpveda.dto.request.CorpVedaRequestDto;
import com.sr.capital.external.corpveda.dto.response.CorpVedaResponseDto;
import com.sr.capital.external.corpveda.service.CorpVedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/corpveda")
public class CorpVedaController {

    private final CorpVedaService corpVedaService;

    @PostMapping
    public CorpVedaResponseDto getDetails(@RequestBody CorpVedaRequestDto requestDTO) throws Exception {
        CorpVedaResponseDto response = corpVedaService.getCinDetails(requestDTO);
        return response;
    }
}
