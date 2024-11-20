package com.sr.capital.entity.primary;


import com.sr.capital.entity.primary.interfaces.Auditable;
import com.sr.capital.listner.AuditEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@MappedSuperclass
@EntityListeners(AuditEntityListener.class)
@FieldNameConstants
public class LongBaseEntity implements Serializable, Auditable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="id", insertable = false, updatable = false, nullable = false)
    private Long id;

    @Embedded
    private AuditData auditData;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;
}
