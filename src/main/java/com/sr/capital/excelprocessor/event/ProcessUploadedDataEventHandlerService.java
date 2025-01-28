package com.sr.capital.excelprocessor.event;

import com.omunify.kafka.MsgMessage;
import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.excelprocessor.model.ProcessUploadDataMessage;
import com.sr.capital.service.KafkaEventService;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessUploadedDataEventHandlerService implements KafkaEventService {
    private final ProcessUploadedDataEventHandlerServiceImpl processUploadedDataEventHandlerService;
    @Override
    public <T, U> T handleEvents(U request) throws Exception {

        MsgMessage message = (MsgMessage) request;
        FileUploadRequestDTO processUploadDataMessage = MapperUtils.readValue(message.getPayload(), FileUploadRequestDTO.class);
        processUploadedDataEventHandlerService.processData(processUploadDataMessage);

        return (T) Boolean.TRUE;
    }
}
