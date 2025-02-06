package com.sr.capital.external.truthscreen.service.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.dto.data.NddExtractionResponseData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.NddDetails;
import com.sr.capital.external.truthscreen.entity.NddDetailsHistory;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.manager.NddAggregateDataRepositoryManager;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TruthScreenNddEntityConstructor implements TruthScreenEntityConstructor {

    private final NddAggregateDataRepositoryManager nddAggregateDataRepositoryManager;

    private final AES256 aes256;

    @Override
    public <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity) throws IOException {
        NddExtractionResponseData nddExtractionResponseData = MapperUtils.convertValue(request.getTruthScreenBaseResponse(),NddExtractionResponseData.class);
        NddDetails nddDetails = getNddDetails(nddExtractionResponseData, request);
        boolean final_verfication = nddDetailsCheck(nddDetails);
        return (T) TruthScreenDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .transId(request.getTransId())
                .initialStatus(String.valueOf(request.getTruthScreenBaseResponse().getStatus()))
                .details(final_verfication)
                .truthScreenDocType(request.getDocType())
                .build();

    }

    private NddDetails getNddDetails(NddExtractionResponseData nddExtractionResponseData, TruthScreenDocOrchestratorRequest request){
        NddDetails nddDetails = NddDetails.builder().creditDefault(nddExtractionResponseData.getCreditDefault())
                .regulatory(nddExtractionResponseData.getRegulatory())
                .sanction(nddExtractionResponseData.getSanction())
                .criminal(nddExtractionResponseData.getCriminal())
                .defaultDirectorsAndCompanies(nddExtractionResponseData.getDefaultDirectorsAndCompanies())
                .message(nddExtractionResponseData.getMessage())
                .status_code(nddExtractionResponseData.getStatus_code())
                .message(nddExtractionResponseData.getMessage())
                .tsTransId(nddExtractionResponseData.getTsTransId())
                .build();
        return saveAggregateData(nddDetails, aes256, request);
    }

    private NddDetails saveAggregateData(NddDetails nddDetails,AES256 aes256, TruthScreenDocOrchestratorRequest request){
        nddDetails.setTransId(request.getTransId());
        NddDetails.encrypt(nddDetails,aes256);
        nddAggregateDataRepositoryManager.saveDoc(nddDetails);
        return nddDetails;
    }

    private boolean nddDetailsCheck(NddDetails nddDetails){
        if (nddDetails == null) {
            return true; // Or false, depending on how you want to handle null input
        }

        if (nddDetails.getCreditDefault() != null &&
                nddDetails.getCreditDefault().getData() != null &&
                nddDetails.getCreditDefault().getData().getData() != null &&
                !nddDetails.getCreditDefault().getData().getData().isEmpty()) {
            return false;
        }

        if (nddDetails.getRegulatory() != null &&
                nddDetails.getRegulatory().getData() != null &&
                nddDetails.getRegulatory().getData().getData() != null &&
                !nddDetails.getRegulatory().getData().getData().isEmpty()) {
            return false;
        }

        if (nddDetails.getSanction() != null &&
                nddDetails.getSanction().getData() != null &&
                nddDetails.getSanction().getData().getData() != null &&
                !nddDetails.getSanction().getData().getData().isEmpty()) {
            return false;
        }

        if (nddDetails.getCriminal() != null &&
                nddDetails.getCriminal().getData() != null &&
                nddDetails.getCriminal().getData().getData() != null &&
                !nddDetails.getCriminal().getData().getData().isEmpty()) {
            return false;
        }

        if (nddDetails.getDefaultDirectorsAndCompanies() != null &&
                nddDetails.getDefaultDirectorsAndCompanies().getData() != null &&
                nddDetails.getDefaultDirectorsAndCompanies().getData().getData() != null &&
                !nddDetails.getDefaultDirectorsAndCompanies().getData().getData().isEmpty()) {
            return false;
        }

        return true;
    }
}
