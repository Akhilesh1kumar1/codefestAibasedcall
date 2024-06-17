package com.sr.capital.entity.primary.interfaces;


import com.sr.capital.entity.primary.AuditData;

public interface Auditable {

    AuditData getAuditData();

    void setAuditData(AuditData data);

}
