package com.sr.capital.external.truthscreen.service.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.dto.data.GSTinExtractionResponseData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.GSTinDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.repository.GstinAggregateDataRepository;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenGstinEntityConstructor implements TruthScreenEntityConstructor {

    @Autowired
    private GstinAggregateDataRepository gstinAggregateDataRepository;

    @Autowired
    private AES256 aes256;

    @Override
    public <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity) throws IOException {
        GSTinExtractionResponseData gStinExtractionResponseData = MapperUtils.convertValue(request.getTruthScreenBaseResponse().getMsg(), GSTinExtractionResponseData.class);
        GSTinDetails gstinDetails = getGSTdetails(gStinExtractionResponseData,request);
        return (T) TruthScreenDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .transId(request.getTransId())
                .initialStatus(request.getTruthScreenBaseResponse().getStatus())
                .details(gstinDetails)
                .truthScreenDocType(request.getDocType())
                .build();

    }

    private GSTinDetails getGSTdetails(GSTinExtractionResponseData extractionResponseData, TruthScreenDocOrchestratorRequest request){
        GSTinDetails gstinDetails = GSTinDetails.builder().filingData(extractionResponseData.getFilingData())
                .goodsAndService(extractionResponseData.getGoodsAndService())
                .placeOfBusinessData(extractionResponseData.getPlaceOfBusinessData())
                .proprietorName(extractionResponseData.getProprietorName())
                .administrativeOffice(extractionResponseData.getAdministrativeOffice())
                .annualAggregateTurnover(extractionResponseData.getAnnualAggregateTurnover())
                .constitutionOfBusiness(extractionResponseData.getConstitutionOfBusiness())
                .dateOfCancellation(extractionResponseData.getDateOfCancellation())
                .dateOfRegistration(extractionResponseData.getDateOfRegistration())
                .gstinUinStatus(extractionResponseData.getGstinUinStatus())
                .gstinUin(extractionResponseData.getGstinUin())
                .grossTotalIncome(extractionResponseData.getGrossTotalIncome())
                .legalNameOfBusiness(extractionResponseData.getLegalNameOfBusiness())
                .natureOfBusinessActivities(extractionResponseData.getNatureOfBusinessActivities())
                .natureOfCoreBusinessActivity(extractionResponseData.getNatureOfCoreBusinessActivity())
                .otherOffice(extractionResponseData.getOtherOffice())
                .percentageOfTaxPaymentInCash(extractionResponseData.getPercentageOfTaxPaymentInCash())
                .taxpayerType(extractionResponseData.getTaxpayerType())
                .tradeName(extractionResponseData.getTradeName())
                .whetherAadhaarAuthenticated(extractionResponseData.getWhetherAadhaarAuthenticated())
                .whetherEKYCVerified(extractionResponseData.getWhetherEKYCVerified())
                .fieldVisitConducted(extractionResponseData.getFieldVisitConducted())
                .build();
        GSTinDetails responseGstinDetails = saveAggregateData(gstinDetails,aes256,request);
        return responseGstinDetails;
    }

    private GSTinDetails saveAggregateData(GSTinDetails gstinDetails,AES256 aes256, TruthScreenDocOrchestratorRequest request){
        gstinDetails.setTransId(request.getTransId());
        gstinAggregateDataRepository.save(gstinDetails);
        GSTinDetails.encryptInfo(gstinDetails,aes256);
        gstinDetails.setFilingData(null);
        gstinDetails.setGoodsAndService(null);
        gstinDetails.setPlaceOfBusinessData(null);
        return gstinDetails;
    }


}
