package com.sr.capital.entity.primary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "whatsapp_api_log")
public class WhatsappApiLog extends LongBaseEntity{

    @Column(name = "message_id")
    String messageId;


    String remarks;

    @Column(name = "internal_id")
    String internalId;

    @Column(name = "event_type")
    String eventType;

    @Column(name = "sr_company_id")
    Long srCompanyId;
}
