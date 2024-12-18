package com.sr.capital.external.truthscreen.controller;

import com.sr.capital.external.truthscreen.dto.request.AsyncRequestDto;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.AsyncReponseDto;
import com.sr.capital.external.truthscreen.dto.response.PanResponseDto;
import com.sr.capital.external.truthscreen.service.PanService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/idsearch")
public class IdSearchController {

    private final PanService panService;

    @PostMapping("/pan")
    public ResponseEntity<PanResponseDto> sendPanRequest(@RequestBody IdSearchRequestDto requestDTO) {
        //PanResponseDto response = panService.sendPanRequest(requestDTO);
        return null;

    }

    @PostMapping("/pan/async-status")
    public ResponseEntity<AsyncReponseDto> getAsyncStatus(@RequestBody AsyncRequestDto asyncRequestDto) {
        AsyncReponseDto response = panService.getAsyncStatus(asyncRequestDto);
        return ResponseEntity.ok(response);
    }
}
