package com.sr.capital.listner;




import com.sr.capital.entity.AuditData;
import com.sr.capital.entity.interfaces.Auditable;
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
        String userId = MDC.get("USER_ID");
        auditData.setCreatedAt(LocalDateTime.now());
        auditData.setCreatedBy(ObjectUtils.isEmpty(userId) ? "SYSTEM" : userId.toString());
        auditData.setUpdatedAt(auditData.getCreatedAt());
        auditData.setUpdatedBy(auditData.getCreatedBy());
    }

    @PreUpdate
    public void beforeUpdate(Auditable auditable){
        AuditData auditData = auditable.getAuditData();
        String userId = MDC.get("USER_ID");
        auditData.setUpdatedAt(LocalDateTime.now());
        auditData.setUpdatedBy(ObjectUtils.isEmpty(userId) ? "SYSTEM" : userId.toString());
    }
}
