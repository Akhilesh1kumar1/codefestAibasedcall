package com.sr.capital.los.service;

import com.sr.capital.los.dto.FormProgressDTO;

public interface FormProgressService {
    FormProgressDTO updateFormProgress(String referenceId, FormProgressDTO progressDTO);
    FormProgressDTO getFormProgress(String referenceId);
}
