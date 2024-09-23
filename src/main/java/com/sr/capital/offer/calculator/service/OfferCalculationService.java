package com.sr.capital.offer.calculator.service;

import com.sr.capital.entity.primary.UnderWritingConfig;
import com.sr.capital.offer.calculator.helpers.ParameterName;
import com.sr.capital.offer.calculator.helpers.UnderwritingGroupNames;
import com.sr.capital.offer.calculator.model.OfferCalculatorRequestDto;
import com.sr.capital.offer.calculator.model.Parameter;
import com.sr.capital.offer.calculator.strategy.*;
import com.sr.capital.service.entityimpl.UnderWritingEntityServiceImpl;
import com.sr.capital.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferCalculationService {

    final UnderWritingEntityServiceImpl underWritingEntityService;

    public void calculateLoanOffer(Long tenantId){

        //build undercapitalization
        OfferCalculatorRequestDto offerCalculatorRequestDto = buildOfferCalculationDetails(tenantId);

        //get config
        List<UnderWritingConfig> underWritingConfigs = underWritingEntityService.getAllParameters();

        //build config parameters
        Map<String,List<Parameter>> underwritingParameters = buildUnderWritingParamters(underWritingConfigs);

        //calculate score
        double score = getScore(offerCalculatorRequestDto,underwritingParameters);

        log.info("final score {} for tenantId {} ",score,tenantId);

        //generate loan offer
        generateLoanOffer(tenantId,score);

    }

    private void generateLoanOffer(Long tenantId, double score) {
        BigDecimal loanAmount =BigDecimal.valueOf(1000000);
        if(score>10){
            loanAmount =BigDecimal.valueOf(500000);
        }

    }

    private double getScore(OfferCalculatorRequestDto offerCalculatorRequestDto, Map<String, List<Parameter>> underwritingParameters) {

        OfferCalculationStrategy calculationStrategy = new SrParametersStrategy();
        Double score= calculationStrategy.calculateScore(offerCalculatorRequestDto,underwritingParameters.get(UnderwritingGroupNames.SR.name()));

        calculationStrategy = new ItrParamtersStrategy();
        score+=calculationStrategy.calculateScore(offerCalculatorRequestDto,underwritingParameters.get(UnderwritingGroupNames.ITR.name()));

        calculationStrategy = new GstStrategy();
        score+=calculationStrategy.calculateScore(offerCalculatorRequestDto,underwritingParameters.get(UnderwritingGroupNames.GST.name()));


        calculationStrategy = new BalanceSheetStrategy();
        score+=calculationStrategy.calculateScore(offerCalculatorRequestDto,underwritingParameters.get(UnderwritingGroupNames.PL.name()));


        calculationStrategy = new BankStatementAnalyzerStrategy();
        score+=calculationStrategy.calculateScore(offerCalculatorRequestDto,underwritingParameters.get(UnderwritingGroupNames.BANK.name()));

        calculationStrategy = new CibilStrategy();
        score+=calculationStrategy.calculateScore(offerCalculatorRequestDto,underwritingParameters.get(UnderwritingGroupNames.CIBIL.name()));

        calculationStrategy = new InventoryDataStrategy();
        score+=calculationStrategy.calculateScore(offerCalculatorRequestDto,underwritingParameters.get(UnderwritingGroupNames.INVENTORY.name()));

        calculationStrategy = new SalesDataStrategy();
        score+=calculationStrategy.calculateScore(offerCalculatorRequestDto,underwritingParameters.get(UnderwritingGroupNames.SALES.name()));

        return score;
    }

    private Map<String, List<Parameter>> buildUnderWritingParamters(List<UnderWritingConfig> underWritingConfigs) {

        Map<String,List<Parameter>> underwritingParameters = new LinkedHashMap<>();

        underWritingConfigs.stream().forEach(underWritingConfig -> {
            List<Parameter> parameterList =underwritingParameters.getOrDefault(underWritingConfig.getGroupName(),new ArrayList<>());
            parameterList.add(buildParmeters(underWritingConfig));
            underwritingParameters.put(underWritingConfig.getGroupName(),parameterList);


        });
        return underwritingParameters;
    }

    private Parameter buildParmeters(UnderWritingConfig underWritingConfig){
        return Parameter.builder().score(underWritingConfig.getScore()).name(underWritingConfig.getName()).value(underWritingConfig.getValue()).condition(underWritingConfig.getCondition()).weightage(underWritingConfig.getWeightage()).build();
    }

    private OfferCalculatorRequestDto buildOfferCalculationDetails(Long tenantId) {
        OfferCalculatorRequestDto offerCalculatorRequestDto =OfferCalculatorRequestDto.builder().build();
        return offerCalculatorRequestDto;
    }
}
