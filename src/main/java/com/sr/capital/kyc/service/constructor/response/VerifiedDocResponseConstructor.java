package com.sr.capital.kyc.service.constructor.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omunify.core.model.GenericResponse;
import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.kyc.dto.response.VerifiedDocData;
import com.sr.capital.kyc.dto.response.verified.*;
import com.sr.capital.kyc.manager.KycVerifiedDetailsManager;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.List;

@Service
public class VerifiedDocResponseConstructor {

    @Autowired
    private KycVerifiedDetailsManager kycVerifiedDetailsManager;

    @Autowired
    private ObjectMapper mapper;

    public ResponseEntity<?> constructVerifiedResponse(final String tenantId) {

        List<KycVerifiedDetails<?>> verifiedDetailsList = kycVerifiedDetailsManager.findKycVerifiedDetailsByTenantId(
            tenantId);

        VerifiedDocData verifiedDocData = VerifiedDocData.builder()
            .tenantId(tenantId)
            .build();

        if (ObjectUtils.isNotEmpty(verifiedDetailsList)) {
            verifiedDetailsList.forEach(document -> {
                TaskType taskType = document.getTaskType();

                if (TaskType.PAN.equals(taskType)) {
                    PanVerifiedData panVerifiedData = mapper.convertValue(document.getDetails(), PanVerifiedData.class);
                    panVerifiedData.setActive(document.getIsActive());
                    panVerifiedData.setExpiresAt(document.getExpiresAt().toString());
                    verifiedDocData.setPanVerifiedData(panVerifiedData);
                } else if (TaskType.BANK_DETAILS.equals(taskType)) {
                    BankVerifiedData bankVerifiedData = mapper.convertValue(document.getDetails(), BankVerifiedData.class);
                    bankVerifiedData.setActive(document.getIsActive());
                    bankVerifiedData.setExpiresAt(document.getExpiresAt().toString());
                    verifiedDocData.setBankVerifiedData(bankVerifiedData);
                } else if (TaskType.GST.equals(taskType)) {
                    GstVerifiedData gstVerifiedData = mapper.convertValue(document.getDetails(), GstVerifiedData.class);
                    gstVerifiedData.setActive(document.getIsActive());
                    gstVerifiedData.setExpiresAt(document.getExpiresAt().toString());
                    verifiedDocData.setGstVerifiedData(gstVerifiedData);
                } else if (TaskType.PAN_AADHAAR.equals(taskType)) {
                    PanAadhaarVerifiedData panAadhaarVerifiedData = mapper.convertValue(document.getDetails(), PanAadhaarVerifiedData.class);
                    panAadhaarVerifiedData.setActive(document.getIsActive());
                    panAadhaarVerifiedData.setExpiresAt(document.getExpiresAt().toString());
                    verifiedDocData.setPanAadhaarVerifiedData(panAadhaarVerifiedData);
                } else if (TaskType.PAN_GST.equals(taskType)) {
                    PanGstVerifiedData panGstVerifiedData = mapper.convertValue(document.getDetails(), PanGstVerifiedData.class);
                    panGstVerifiedData.setActive(document.getIsActive());
                    panGstVerifiedData.setExpiresAt(document.getExpiresAt().toString());
                    verifiedDocData.setPanGstVerifiedData(panGstVerifiedData);
                }
            });
        }

        GenericResponse<VerifiedDocData> response = new GenericResponse<>();
        response.setStatusCode(HttpStatusCode.OK);
        response.setData(verifiedDocData);

        return new ResponseEntity<>(
            response,
            HttpStatus.OK
        );
    }

}
