package com.sr.capital.service.entityimpl;

import com.sr.capital.dto.request.CreateBaseBankDto;
import com.sr.capital.dto.response.BaseBankResponseDto;
import com.sr.capital.entity.primary.Bank;
import com.sr.capital.entity.primary.BaseCreditPartner;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.repository.primary.BankRepository;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BankEntityServiceImpl {

    final BankRepository bankRepository;
    final RedissonClient redissonClient;

    public Boolean addBank(CreateBaseBankDto createBaseBankDto) throws CustomException {
        validateBankDetails(createBaseBankDto);
        Bank bank =bankRepository.save(Bank.mapBankDetailsFromDto(createBaseBankDto));
        saveInCache(bank);
        return true;
    }

    private void validateBankDetails(CreateBaseBankDto createBaseBankDto) throws CustomException {
        Bank bank =bankRepository.findByBankName(createBaseBankDto.getBankName());
        if(bank!=null){
            throw new CustomException("Bank details already exist", HttpStatus.BAD_REQUEST);
        }
    }

    public List<BaseBankResponseDto> getAllBanks() {
        RMapCache<String, Bank>  bankRMapCache = redissonClient.getMapCache(Constants.RedisKeys.BANK_DETAILS);
        return bankDtoFromBaseBankList(new ArrayList<>(bankRMapCache.values()));
    }

    public boolean syncAllBaseBankToCache() {
        List<Bank> bankList = (List<Bank>) bankRepository.findAll();
        RMapCache<String, Bank> bankRMapCache = redissonClient.getMapCache(Constants.RedisKeys.BANK_DETAILS);
        bankRMapCache.clear();
        bankRMapCache.putAll(bankList.stream().collect(Collectors.toMap(Bank::getBankName, Function.identity())));
        return true;
    }

    private void saveInCache(Bank bank) {
        RMapCache<String, Bank> bankRMapCache = redissonClient.getMapCache(Constants.RedisKeys.BANK_DETAILS);
        bankRMapCache.put(bank.getBankName(), bank);
    }

    private List<BaseBankResponseDto> bankDtoFromBaseBankList(List<Bank> baseBankList) {
        return MapperUtils.mapList(baseBankList, BaseBankResponseDto.class);
    }
}
