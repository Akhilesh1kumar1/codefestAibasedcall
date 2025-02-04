package com.sr.capital.los.service;

import com.sr.capital.entity.mongo.los.FormProgress;
import com.sr.capital.los.dto.FormProgressDTO;
import com.sr.capital.los.utill.LosConstant;
import com.sr.capital.repository.mongo.los.FormProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FormProgressServiceImpl implements FormProgressService {

    @Autowired
    private FormProgressRepository formProgressRepository;

    @Override
    public FormProgressDTO updateFormProgress(String referenceId, FormProgressDTO progressDTO) {
        Optional<FormProgress> existingProgress = formProgressRepository.findByReferenceId(referenceId);
        FormProgress progress = existingProgress.orElseGet(() -> FormProgress.builder().referenceId(referenceId).build());

        // Update status
        progress.setStatus(progressDTO.getStatus());

        // Save to DB
        formProgressRepository.save(progress);

        // Convert to DTO
        FormProgressDTO responseDTO = new FormProgressDTO();
        responseDTO.setReferenceId(progress.getReferenceId());
        responseDTO.setStatus(progress.getStatus());

        return responseDTO;
    }

    @Override
    public FormProgressDTO getFormProgress(String referenceId) {
        return formProgressRepository.findByReferenceId(referenceId)
                .map(progress -> {
                    FormProgressDTO dto = new FormProgressDTO();
                    dto.setReferenceId(progress.getReferenceId());
                    dto.setStatus(progress.getStatus());
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException(LosConstant.PROGRESS_NOT_FOUND_FOR_REFERENCE_ID + referenceId));
    }
}
