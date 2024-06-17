package com.sr.capital.service;

import com.sr.capital.dto.request.AdharUpdateRequestDto;
import com.sr.capital.dto.response.AdharUpdateResponse;
import com.sr.capital.entity.primary.AdharDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdharService {

    public AdharUpdateResponse addAdharDetails(AdharUpdateRequestDto adharUpdateRequestDto, List<MultipartFile> multipartFileList) throws Exception;

    public AdharDetails getAdharDetailsById(Long adharId);
}
