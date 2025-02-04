package com.sr.capital.los.controller;

import com.sr.capital.los.dto.FormProgressDTO;
import com.sr.capital.los.service.FormProgressService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
@AllArgsConstructor
public class FormProgressController {

    private final FormProgressService formProgressService;

    // Update form progress
    @PutMapping("/{referenceId}")
    public ResponseEntity<FormProgressDTO> updateProgress(@PathVariable String referenceId, @RequestBody FormProgressDTO progressDTO) {
        return ResponseEntity.ok(formProgressService.updateFormProgress(referenceId, progressDTO));
    }

    // Get current form progress
    @GetMapping("/{referenceId}")
    public ResponseEntity<FormProgressDTO> getProgress(@PathVariable String referenceId) {
        return ResponseEntity.ok(formProgressService.getFormProgress(referenceId));
    }
}
