package com.sr.capital.service;

import com.sr.capital.dto.request.CibilScoreCheckRequestDto;
import com.sr.capital.dto.request.PANUpdateRequestDto;
import com.sr.capital.dto.response.CibilScoreCheckResponseDto;
import com.sr.capital.dto.response.PANUpdateResponse;
import com.sr.capital.entity.primary.PANDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PANService {

    public PANUpdateResponse createPanDetails(PANUpdateRequestDto adharUpdateRequestDto, List<MultipartFile> multipartFileList) throws Exception;

    public PANDetails readPanDetails(Long userId);

    public PANUpdateResponse updatePanDetails(PANUpdateRequestDto adharUpdateRequestDto, List<MultipartFile> multipartFileList) throws Exception;

    public CibilScoreCheckResponseDto getCibilDetailsUsingPan(CibilScoreCheckRequestDto cibilScoreCheckRequestDto) throws Exception;

    public Boolean validatePanNumber(String panNumber) throws Exception;

}
