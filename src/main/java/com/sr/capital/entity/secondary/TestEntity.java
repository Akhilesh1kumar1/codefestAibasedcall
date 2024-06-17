package com.sr.capital.entity.secondary;

import com.sr.capital.entity.primary.LongBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "test")
public class TestEntity extends LongBaseEntity {

    String description;
}
