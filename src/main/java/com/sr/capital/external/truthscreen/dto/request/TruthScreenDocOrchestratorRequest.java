package com.sr.capital.external.truthscreen.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenBaseResponse;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class TruthScreenDocOrchestratorRequest {

    private TruthScreenDocType docType;

    private String docNumber;

    @JsonProperty("sr_company_id")
    private String srCompanyId;

    private String transId;

    private TruthScreenBaseRequest<?> truthScreenBaseRequest;

    private TruthScreenBaseResponse<?> truthScreenBaseResponse;

    private TruthScreenDocDetails<T> truthScreenDocDetails;

    private TruthScreenDocDetails<T> previousDocDetails;

    private String fromDate;

    private String toDate;

    private String gstPortalUsername;

}
