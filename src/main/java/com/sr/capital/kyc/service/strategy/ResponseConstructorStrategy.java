package com.sr.capital.kyc.service.strategy;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.exception.custom.ResponseConstructorNotFoundException;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.constructor.response.*;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseConstructorStrategy {

    @Autowired
    private ExtractedAadhaarResponseConstructor extractedAadhaarResponseConstructor;

    @Autowired
    private ExtractedBankResponseConstructor extractedBankResponseConstructor;

    @Autowired
    private ExtractedGstResponseConstructor extractedGstResponseConstructor;

    @Autowired
    private ExtractedPanCardResponseConstructor extractedPanCardResponseConstructor;

    @Autowired
    private FetchDocDetailsResponseConstructor fetchDocDetailsResponseConstructor;

    @Autowired
    private UploadExtractAndSaveResponseConstructor uploadExtractAndSaveResponseConstructor;

    @Autowired
    private DefaultResponseConstructor defaultResponseConstructor;

    @Autowired
    private ExtractedGstByPanResponseConstructor extractedGstByPanResponseConstructor;

    @Autowired
    private ItrDocResponseConstructor itrDocResponseConstructor;


    public <T,U> ResponseEntity<GenericResponse<T>> constructResponse(U input) throws ResponseConstructorNotFoundException {

        ResponseConstructor responseConstructor;
        RequestType requestType = RequestData.getRequestType();
        switch (requestType) {
            case UPLOAD_AND_EXTRACT:
                DocOrchestratorRequest request = (DocOrchestratorRequest) input;
                switch (request.getDocType()){
                   /* case AADHAAR:
                        responseConstructor = extractedAadhaarResponseConstructor;
                        break;*/
                    case BANK_CHEQUE:
                        responseConstructor = extractedBankResponseConstructor;
                        break;
                 /*   case GST:
                        responseConstructor = extractedGstResponseConstructor;
                        break;
                    case PAN:
                        responseConstructor = extractedPanCardResponseConstructor;
                        break;*/
                    case GST_BY_PAN:
                        responseConstructor = extractedGstByPanResponseConstructor;
                        break;
                    case MSME:
                    case PROVISIONAL:
                    case LOAN_TRACKER:
                    case BUSINESS_ADDRESS:
                    case PERSONAL_ADDRESS:
                    case DRIVING_LICENCE:
                    case PROPRIETORSHIP:
                    case VOTING_CARD:
                    case CIN:
                    case AGREEMENT:
                    case DIRECTORS:
                    case PAN_GUARANTOR:
                    case PAN_PERSONAL:
                    case PASSPORT:
                    case ADHAR_GUARANTOR_COAPPLICANT:
                    case GST_REGISTRATION:
                    case SHOP_EST_REGISTRATION:
                    case TRADE_LICENSE:
                    case FOOD_LICENSE:
                    case DRUG_LICENSE_CERTIFICATE:
                    case UDYAM_REGISTRATION:
                    case UDYOG_AADHAAR:
                    case BANK_STATEMENT_CURRENT_6:
                    case ELECTRICITY_COMPANY:
                    case SALE_DEED_COMPANY:
                    case LANDLINE_BILL_3MONTH:
                    case PROPERTY_TAX_RECEIPT:
                    case RENT_AGREEMENT_COMPANY:
                    case FINANCIAL_AUDIT:
                    case ITR_RETURNS:
                    case GST_RETURNS_6:
                    case VALID_PARTNERSHIP_DEED:
                    case COMPANY_PAN:
                    case COMPANY_COI:
                    case MOA_AOA_COMPANY:
                    case LATEST_CA_SHAREHOLDINGS:
                    case ELECTRICITY:
                    case PIPED_GAS_BILL:
                    case WATER_BILL:
                    case SALE_DEED:
                    case LANDLINE_BILL:
                    case PAN:
                    case AADHAR:
                    case GST:
                    case EPAN:
                        responseConstructor = defaultResponseConstructor;
                         break;
                    case ITR:
                        responseConstructor =itrDocResponseConstructor;
                        break;
                    default:
                        throw new ResponseConstructorNotFoundException();
                }
                break;
            case DOC_DETAILS:
                responseConstructor = fetchDocDetailsResponseConstructor;
                break;
            case UPLOAD_EXTRACT_SAVE:
                responseConstructor = uploadExtractAndSaveResponseConstructor;
                break;
            case BANK_DETAILS:
            case GST:
                responseConstructor = defaultResponseConstructor;
                break;
            default:
                throw new ResponseConstructorNotFoundException();
        }

        return responseConstructor.constructResponse(input);
    }
}
