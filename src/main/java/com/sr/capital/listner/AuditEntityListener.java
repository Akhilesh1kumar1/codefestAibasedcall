package com.sr.capital.listner;




import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.primary.AuditData;
import com.sr.capital.entity.primary.interfaces.Auditable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.slf4j.MDC;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

public class AuditEntityListener {

    @PrePersist
    public void beforeSave(Auditable auditable){
        AuditData auditData = auditable.getAuditData();
        if(auditData == null){
            auditData = new AuditData();
            auditable.setAuditData(auditData);
        }
        String userId = RequestData.getUserId()==null? "SYSTEM": String.valueOf(RequestData.getUserId());
        auditData.setCreatedAt(LocalDateTime.now());
        auditData.setCreatedBy(userId);
        auditData.setUpdatedAt(auditData.getCreatedAt());
        auditData.setUpdatedBy(auditData.getCreatedBy());
    }

    @PreUpdate
    public void beforeUpdate(Auditable auditable){
        AuditData auditData = auditable.getAuditData();
        String userId = RequestData.getUserId()==null? "SYSTEM": String.valueOf(RequestData.getUserId());
        auditData.setUpdatedAt(LocalDateTime.now());
        auditData.setUpdatedBy(ObjectUtils.isEmpty(userId) ? "SYSTEM" : userId.toString());
    }
}
