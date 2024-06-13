package com.sr.capital.kyc.service.verifier;


import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BankDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.VerifierResponse;
import com.sr.capital.kyc.external.adaptor.IdfyVerificationAdapter;
import com.sr.capital.kyc.external.exception.IDfyNameComparisonException;
import com.sr.capital.kyc.external.response.verification.NameComparisonResponse;
import com.sr.capital.kyc.manager.KycDocDetailsManager;
import com.sr.capital.kyc.service.interfaces.DetailsVerifier;
import com.sr.capital.kyc.service.transformer.external.NameComparisonRequestTransformer;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Service
public class PanBankVerifier implements DetailsVerifier {

    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private KycDocDetailsManager kycDocDetailsManager;

    @Autowired
    private NameComparisonRequestTransformer nameComparisonRequestTransformer;

    @Autowired
    private IdfyVerificationAdapter idfyVerificationAdapter;

    @Override
    @SuppressWarnings("unchecked")
    public VerifierResponse verify(DocOrchestratorRequest orchestratorRequest)
        throws IDfyNameComparisonException, ServiceEndpointNotFoundException {

        Map<DocType, KycDocDetails<?>> docTypeKycDocDetailsMap = orchestratorRequest.getDocTypeKycDocDetailsMap();
        KycDocDetails<PanDocDetails> panDetails;
        KycDocDetails<BankDocDetails> bankDetails;

        if(CollectionUtils.isEmpty(docTypeKycDocDetailsMap)) {
            panDetails = (KycDocDetails<PanDocDetails>) kycDocDetailsManager
                    .findKycDocDetailsByTenantIdAndDocType(RequestData.getTenantId(), DocType.PAN);

            bankDetails = (KycDocDetails<BankDocDetails>) kycDocDetailsManager
                    .findKycDocDetailsByTenantIdAndDocType(RequestData.getTenantId(), DocType.BANK_CHEQUE);
        } else {
            panDetails = (KycDocDetails<PanDocDetails>) docTypeKycDocDetailsMap.get(DocType.PAN);

            bankDetails = (KycDocDetails<BankDocDetails>) docTypeKycDocDetailsMap.get(DocType.BANK_CHEQUE);
        }

        Boolean matched = Boolean.FALSE;
        if (ObjectUtils.isNotEmpty(panDetails) && ObjectUtils.isNotEmpty(bankDetails)) {

            NameComparisonResponse response = idfyVerificationAdapter.getNameComparisonScore(
                    nameComparisonRequestTransformer.transformRequest(panDetails.getDetails().getNameOnCard(), bankDetails.getDetails().getAccountName())
            );
            if (kycAppProperties.getAllowedNameScore() <= response.getResult().getMatchOutput().getNameMatch()) {
                matched = Boolean.TRUE;
            }
        }

        return VerifierResponse.builder().isVerified(matched).build();
    }

}
