package com.sr.capital.service;

import com.sr.capital.dto.request.PanUpdateRequestDto;
import com.sr.capital.dto.response.PanUpdateResponse;
import com.sr.capital.entity.PANDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PANService {

    public PanUpdateResponse createPanDetails(PanUpdateRequestDto adharUpdateRequestDto, List<MultipartFile> multipartFileList) throws Exception;

    public PANDetails readPanDetails(Long userId);

    public PanUpdateResponse updatePanDetails(PanUpdateRequestDto adharUpdateRequestDto, List<MultipartFile> multipartFileList) throws Exception;

}
