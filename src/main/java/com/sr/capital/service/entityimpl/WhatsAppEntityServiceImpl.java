package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.primary.WhatsappApiLog;
import com.sr.capital.repository.primary.WhatsAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WhatsAppEntityServiceImpl {

    final WhatsAppRepository whatsAppRepository;


    public void saveWhatsAppApiLog(WhatsappApiLog whatsappApiLog){
        whatsAppRepository.save(whatsappApiLog);
    }

    public void saveAllWhatsAppApiLog(List<WhatsappApiLog> whatsappApiLog){
        whatsAppRepository.saveAll(whatsappApiLog);
    }

    public WhatsappApiLog getApiLog(String messageId){
        return whatsAppRepository.findByMessageId(messageId);
    }

}
