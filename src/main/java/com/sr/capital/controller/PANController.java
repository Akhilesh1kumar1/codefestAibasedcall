package com.sr.capital.controller;

import lombok.RequiredArgsConstructor;

import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.PanUpdateRequestDto;
import com.sr.capital.dto.response.PanUpdateResponse;
import com.sr.capital.entity.PANDetails;
import com.sr.capital.service.PANService;
import com.sr.capital.util.ResponseBuilderUtil;
import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.PAN_SAVE_SUCCESS;

import java.util.List;

@RestController
@RequestMapping("/api/pan")
@RequiredArgsConstructor
@Validated
public class PANController {

    final PANService panService;

    @GetMapping("")
    public GenericResponse<PANDetails> getPanDetails() throws Exception {
        return ResponseBuilderUtil.getResponse(panService.readPanDetails(RequestData.getUserId()), SUCCESS,
                "", HttpStatus.SC_OK);
    }

    @PostMapping("")
    public GenericResponse<PanUpdateResponse> addPanDetails(
            @RequestPart("data") PanUpdateRequestDto panUpdateRequestDto,
            @RequestParam("image") List<MultipartFile> image) throws Exception {

        return ResponseBuilderUtil.getResponse(panService.createPanDetails(panUpdateRequestDto, image), SUCCESS,
                PAN_SAVE_SUCCESS, HttpStatus.SC_OK);
    }

    @PutMapping("")
    public GenericResponse<PanUpdateResponse> updatePanDetails(
            @RequestPart("data") PanUpdateRequestDto panUpdateRequestDto,
            @RequestParam(name = "image", required = false) List<MultipartFile> image) throws Exception {

        return ResponseBuilderUtil.getResponse(panService.updatePanDetails(panUpdateRequestDto, image), SUCCESS,
                PAN_SAVE_SUCCESS, HttpStatus.SC_OK);
    }

}
