package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.WhatsappApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface WhatsAppRepository extends JpaRepository<WhatsappApiLog,Long> {

    WhatsappApiLog findByMessageId(String messageId);
}
