package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.primary.UnderWritingConfig;
import com.sr.capital.offer.calculator.model.Parameter;
import com.sr.capital.repository.primary.UnderWritingConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnderWritingEntityServiceImpl {

    final UnderWritingConfigRepository underWritingConfigRepository;

    public List<UnderWritingConfig> getAllParameters(){
        return underWritingConfigRepository.findAll();
    }
}
