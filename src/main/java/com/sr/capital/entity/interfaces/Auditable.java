package com.sr.capital.entity.interfaces;


import com.sr.capital.entity.AuditData;

public interface Auditable {

    AuditData getAuditData();

    void setAuditData(AuditData data);

}
