package com.sr.capital.external.truthscreen.controller;

import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.dto.request.AsyncRequestDto;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.AsyncReponseDto;
import com.sr.capital.external.truthscreen.service.PanService;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/idsearch")
public class IdSearchController {

    private final PanService panService;

    @PostMapping("/pan")
    public T sendPanRequest(@RequestBody IdSearchRequestDto requestDTO) throws RequestTransformerNotFoundException {
        T response = panService.sendPanRequest(requestDTO);
        return response;
    }

    @PostMapping("/pan/async-status")
    public ResponseEntity<AsyncReponseDto> getAsyncStatus(@RequestBody AsyncRequestDto asyncRequestDto) {
        AsyncReponseDto response = panService.getAsyncStatus(asyncRequestDto);
        return ResponseEntity.ok(response);
    }
}
