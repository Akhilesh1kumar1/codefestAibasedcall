package com.sr.capital.entity.primary;

import com.sr.capital.offer.calculator.helpers.ParameterName;
import jakarta.persistence.*;
import lombok.*;

import static com.sr.capital.helpers.constants.Constants.EntityNames.UNDERWRITING_CONFIG;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = UNDERWRITING_CONFIG)
public class UnderWritingConfig extends LongBaseEntity{

    @Enumerated(EnumType.STRING)
    private ParameterName name;

    private double value;

    private double score;

    private String condition;

    private Double Weightage;
}
