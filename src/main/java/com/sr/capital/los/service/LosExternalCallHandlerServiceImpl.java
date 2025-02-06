package com.sr.capital.los.service;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.los.LosUserEntity;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.entity.GSTinDetails;
import com.sr.capital.external.truthscreen.service.IdService;
import com.sr.capital.external.truthscreen.service.constructors.TruthScreenGstinEntityConstructor;
import com.sr.capital.external.truthscreen.service.transformers.TruthScreenExternalRequestTransformerStrategy;
import com.sr.capital.kyc.service.strategy.ExternalRequestTransformerStrategy;
import com.sr.capital.los.utill.ExternalCallStatusEnum;
import com.sr.capital.repository.mongo.los.LosUserRepository;
import com.sr.capital.util.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;

import static com.sr.capital.external.truthscreen.enums.TruthScreenDocType.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class LosExternalCallHandlerServiceImpl {

    final AppProperties appProperties;
    final LosUserRepository losUserRepository;
    final IdService idService;
    final TruthScreenGstinEntityConstructor truthScreenGstinEntityConstructor;
    final TruthScreenExternalRequestTransformerStrategy transformExtractionRequest;
    final LosUserService losUserService;


    public void handleExternalCall(String losUserId) throws Exception {
        Optional<LosUserEntity> optional = losUserRepository.findById(losUserId);
        if (optional.isPresent()) {
            LosUserEntity losUserEntity = optional.get();

            // call ndd // no input
            IdSearchResponseDto<?> idSearchResponseDto = idService.sendRequest(new IdSearchRequestDto(RequestData.getTenantId() + HashUtil.generateRandomId(),
                    NDD.getValue(), null, null, null, null));
            losUserService.updateDetailsIntoEventStatusHandlerTable(losUserId, ExternalCallStatusEnum.NDD_FETCHED.name());
            //call crif score
            //call pan to gst // pan input
            IdSearchResponseDto<?> idSearchResponseDto1 = idService.sendRequest(new IdSearchRequestDto(RequestData.getTenantId() + HashUtil.generateRandomId(),
                    PAN_TO_GST.getValue(), losUserEntity.getPan(), null, null, null));
            losUserService.updateDetailsIntoEventStatusHandlerTable(losUserId, ExternalCallStatusEnum.PAN_TO_GST_FETCHED.name());

//            TruthScreenBaseRequest<?> truthScreenBaseRequest = transformExtractionRequest.transformExtractionRequest(idSearchResponseDto1);

            if (idSearchResponseDto1 != null) {
                // we are not going to call this api because we are using gst analytics
                // call GST_IN -> call gstin in loop for list of gst details provided in panToGST response input will gstIn
//                truthScreenGstinEntityConstructor.constructEntity(idSearchResponseDto1 ,GSTinDetails.class)
//                IdSearchResponseDto<?> idSearchResponseDto2 = idService.sendRequest(new IdSearchRequestDto(RequestData.getTenantId() + HashUtil.generateRandomId(),
//                        GSTIN.getValue(), "gstinUin", null, null, null));

//                losUserService.updateDetailsIntoEventStatusHandlerTable(losUserId, ExternalCallStatusEnum.GST_IN_FETCHED.name());

                // call CIN // company full name from pan compehensive
                IdSearchResponseDto<?> idSearchResponseDto3 = idService.sendRequest(new IdSearchRequestDto(RequestData.getTenantId() + HashUtil.generateRandomId(),
                        CIN.getValue(), "company full name from pan compehensive", null, null, null));
                losUserService.updateDetailsIntoEventStatusHandlerTable(losUserId, ExternalCallStatusEnum.CIN_FETCHED.name());

                // call CORPVEDA_DETAILS
//        IdSearchResponseDto<?> idSearchResponseDto = idService.sendRequest(new IdSearchRequestDto(RequestData.getTenantId() + HashUtil.generateRandomId(),
//                PAN_TO_GST.getValue(), updatedLosUserDTO.getPan()));

            }
        }
    }
}
