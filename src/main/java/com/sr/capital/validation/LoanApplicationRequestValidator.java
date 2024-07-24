package com.sr.capital.validation;

import com.omunify.core.util.ExceptionUtils;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.dto.response.BaseCreditPartnerResponseDto;
import com.sr.capital.entity.primary.BaseCreditPartner;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.LoanOffer;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.LoanOfferService;
import com.sr.capital.service.RequestValidator;
import com.sr.capital.service.entityimpl.BaseCreditPartnerEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanApplicationRequestValidator  implements RequestValidator {

    final LoanApplicationRepository loanApplicationRepository;
    final LoanOfferService loanOfferService;
    final BaseCreditPartnerEntityServiceImpl baseCreditPartnerEntityService;
    @Override
    public <T, U> T validateRequest(U request) throws Exception {

        LoanApplicationRequestDto requestDto = (LoanApplicationRequestDto) request;
        requestDto.setSrCompanyId(Long.valueOf(RequestData.getTenantId()));
        requestDto.setUserId(RequestData.getUserId());
        if(requestDto.getLoanOfferId()!=null) {
            LoanOffer loanOffer = loanOfferService.getLoanOfferById(requestDto.getLoanOfferId());

            if(loanOffer==null){
                throw new CustomException("Loan offer details not found for loan offerId "+requestDto.getLoanOfferId(), HttpStatus.BAD_REQUEST);
            }
        }
        if(requestDto.getLoanVendorId()!=null){
            if(!baseCreditPartnerEntityService.isVendorExist(requestDto.getLoanVendorId())){
                ExceptionUtils.throwCustomException("C0001", "Given vendor doesn't exists", HttpStatus.BAD_REQUEST);
            }
        }
        if(requestDto.getLoanVendorName()!=null) {
            BaseCreditPartner baseCreditPartner = baseCreditPartnerEntityService.getCreditPartnerByName(requestDto.getLoanVendorName());
            if (baseCreditPartner != null) {
                if (requestDto.getLoanVendorId() == null) {
                    requestDto.setLoanVendorId(baseCreditPartner.getId());
                }
            }
        }



        return (T) requestDto;
    }
}
