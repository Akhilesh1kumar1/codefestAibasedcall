package com.sr.capital.entity;


import com.sr.capital.entity.interfaces.Auditable;
import com.sr.capital.listner.AuditEntityListener;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@MappedSuperclass
@EntityListeners(AuditEntityListener.class)
public class LongBaseEntity implements Serializable, Auditable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="id", insertable = false, updatable = false, nullable = false)
    private Long id;

    @Embedded
    private AuditData auditData;

    @Column(name = "is_enabled")
    private Boolean isEnabled = false;
}
