package com.sr.capital.kyc.dto.request;


import com.sr.capital.entity.primary.Task;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.external.shiprocket.enums.KycType;
import com.sr.capital.helpers.enums.DocActionType;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.external.request.IdfyBaseRequest;
import com.sr.capital.kyc.external.response.IdfyBaseResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
@Builder
public class DocOrchestratorRequest {

    private DocType docType;

    private KycType kycType;

    private String srCompanyId;

    private List<DocActionType> actions;

    private FileDetails file1;

    private FileDetails file2;

    private IdfyBaseRequest<?> idfyBaseRequest;

    private IdfyBaseResponse<?> idfyBaseResponse;

    public Boolean hasFile2() {
        return (this.getFile2() != null && this.getFile2().getFile() != null && this.getFile2().getFile().getSize() != 0);
    }

    private VerificationEntity verification;

    private Task task;

    private KycDocDetails<?> kycDocDetails;

    private KycVerifiedDetails<?> kycVerifiedDetails;

    private Map<DocType, KycDocDetails<?>> docTypeKycDocDetailsMap;

    private Map<String,Object> docDetails;


}
