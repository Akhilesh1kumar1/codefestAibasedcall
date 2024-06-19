package com.sr.capital.entity.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;

import static com.sr.capital.helpers.constants.Constants.EntityNames.CAPITAL_REPORT;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = CAPITAL_REPORT)
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class CapitalDataReport {


    @Id
    @Column(name = "COMPANY_ID")
    private Integer companyId;

    @Column(name = "PLATFORM_AGE_SIGNUP", nullable = false, columnDefinition = "int default 0")
    private Integer platformAgeSignup = 0;

    @Column(name = "PLATFORM_AGE_FTS")
    private Integer platformAgeFts;

    @Column(name = "ORG_TYPE", nullable = false, columnDefinition = "varchar(100) default ''")
    private String orgType = "";

    //@Type(type = "jsonb")
    @Column(name = "DETAILS_INFO", columnDefinition = "jsonb")
    private String detailsInfo;

    @Column(name = "UPDATED_ON")
    private Timestamp updatedOn;

    @Column(name = "PLATFORM_AGE_SIGNUP_MONTH", nullable = false, columnDefinition = "int default 0")
    private Integer platformAgeSignupMonth = 0;

    @Column(name = "PLATFORM_AGE_FTS_MONTH", nullable = false, columnDefinition = "int default 0")
    private Integer platformAgeFtsMonth = 0;

}
