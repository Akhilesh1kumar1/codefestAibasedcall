package com.sr.capital.kyc.service.constructor.entity.mongo;


import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BankDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.kyc.dto.request.BankDetailsRequest;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankDocDetailsEntityConstructor implements EntityConstructor {

    final AES256 aes256;
    @Override
    @SuppressWarnings("unchecked")
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) {
        List<BankDocDetails> bankDocDetailsList =new ArrayList<>();

        List<String> images = new ArrayList<>();
        if(request.getFile1()!=null)
           images.add(request.getFile1().getFileName());
        if(request.hasFile2()){
            images.add(request.getFile2().getFileName());
        }
        KycDocDetails<List<BankDocDetails>> bankKycDocDetails = (KycDocDetails<List<BankDocDetails>>) request.getKycDocDetails();
        BankDocDetails bankDocDetails =null;
        try {
             bankDocDetails = MapperUtils.convertValue(request.getDocDetails(),BankDocDetails.class);
             encryptData(bankDocDetails);
             bankDocDetailsList.add(bankDocDetails);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(bankKycDocDetails!=null){
             bankKycDocDetails.getDetails().add(bankDocDetails);
             bankKycDocDetails.setKycType(request.getKycType());
            return (T) bankKycDocDetails;
        }

        return (T) KycDocDetails.<List<BankDocDetails>>builder()
                .srCompanyId(request.getSrCompanyId())
                .images(images)
                .docType(request.getDocType())
                .details(bankDocDetailsList)
                .kycType(request.getKycType())
                .build();
    }

    private void encryptData(BankDocDetails bankDocDetails) {
        bankDocDetails.setBankAddress(aes256.encrypt(bankDocDetails.getBankAddress()));
        bankDocDetails.setAccountName(aes256.encrypt(bankDocDetails.getAccountName()));
        bankDocDetails.setAccountNo(aes256.encrypt(bankDocDetails.getAccountNo()));
        bankDocDetails.setBankAccountPassword(aes256.encrypt(bankDocDetails.getBankAccountPassword()));
        bankDocDetails.setBankAccountUserName(aes256.encrypt(bankDocDetails.getBankAccountUserName()));

    }


}
