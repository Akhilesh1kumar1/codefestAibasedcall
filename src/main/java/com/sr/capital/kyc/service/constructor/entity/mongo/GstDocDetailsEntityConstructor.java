package com.sr.capital.kyc.service.constructor.entity.mongo;


import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.entity.primary.Task;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.TaskStatus;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.extraction.data.GstExtractionData;
import com.sr.capital.kyc.external.response.extraction.GstExtractionResponse;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class GstDocDetailsEntityConstructor implements EntityConstructor {

    final AES256 aes256;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) {

        GstDocDetails docDetails = null;
        try {
            docDetails = MapperUtils.convertValue(request.getDocDetails(), GstDocDetails.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GstExtractionResponse gstExtractionResponse = (GstExtractionResponse) request.getKarzaBaseResponse();
        docDetails.setGstin(aes256.encrypt(docDetails.getGstin()));
        docDetails.setUsername(aes256.encrypt(docDetails.getUsername()));
        KycDocDetails<GstDocDetails> gstDocList = (KycDocDetails<GstDocDetails>) request.getKycDocDetails();
        AtomicBoolean isRecordFound = new AtomicBoolean(false);
        if(gstDocList!=null && gstDocList.getDetails()!=null && CollectionUtils.isNotEmpty(gstDocList.getDetails().getGstDetails())){

            GstDocDetails finalDocDetails = docDetails;
            gstDocList.getDetails().getGstDetails().forEach(gstUserDetails -> {
                if(gstUserDetails.getGstin().equalsIgnoreCase(finalDocDetails.getGstin())){
                    isRecordFound.set(true);
                    gstUserDetails.setUsername(finalDocDetails.getUsername());
                }
            });
        }else{
            docDetails.setGstDetails(new ArrayList<>());
        }

        if(!isRecordFound.get()){
            List<GstDocDetails.GstUserDetails> gstUserDetailList =new ArrayList<>();
            GstDocDetails.GstUserDetails gstUserDetails= GstDocDetails.GstUserDetails.builder().gstin(docDetails.getGstin()).username(docDetails.getUsername()).refId(docDetails.getRefId()).consent(docDetails.getConsent())
                    .consolidate(docDetails.isConsolidate()).extendedPeriod(docDetails.isExtendedPeriod()).build();
            gstUserDetailList.add(gstUserDetails);
            docDetails.setGstDetails(gstUserDetailList);
            if(gstDocList!=null){
                gstDocList.getDetails().getGstDetails().add(gstUserDetails);
            }

        }

        if(gstDocList==null){
            gstDocList= KycDocDetails.<GstDocDetails>builder()
                    .srCompanyId(RequestData.getTenantId())
                    .docType(DocType.GST)
                    .details(docDetails)
                    .build();
        }

        Task task = Task.builder().type(TaskType.GST).groupId(Long.valueOf(RequestData.getTenantId())).requestId(gstExtractionResponse.getRequestId()).status(TaskStatus.PENDING_FOR_MANUAL_APPROVAL).remarks("gst fetch").build();
        request.setTask(task);
        return (T) gstDocList;
    }



}
