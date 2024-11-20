package com.sr.capital.entity.primary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.sr.capital.helpers.constants.Constants.EntityNames.PINCODE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = PINCODE)
public class Pincode extends LongBaseEntity{

    Long pincode;

    @Column(name = "loan_vendor_id")
    Long loanVendorId;

    String state;

    String district;

    String city;
}
