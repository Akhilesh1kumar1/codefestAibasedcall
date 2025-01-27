package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.mongo.ErrorLogs;
import com.sr.capital.repository.mongo.ErrorLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorLogsEntityServiceImpl {

    final ErrorLogsRepository errorLogsRepository;


    public ErrorLogs saveErrorLogs(ErrorLogs errorLogs){
        return errorLogsRepository.save(errorLogs);
    }
}
