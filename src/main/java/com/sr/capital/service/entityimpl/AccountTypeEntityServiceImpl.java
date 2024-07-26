package com.sr.capital.service.entityimpl;

import com.sr.capital.dto.request.CreateBaseAccountTypeDto;
import com.sr.capital.dto.response.BaseAccountTypeResponseDto;
import com.sr.capital.dto.response.BaseBankResponseDto;
import com.sr.capital.entity.primary.AccountType;
import com.sr.capital.entity.primary.Bank;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.exception.custom.CustomRuntimeException;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.repository.primary.AccountTypeRepository;
import com.sr.capital.util.MapperUtils;
import lombok.AllArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountTypeEntityServiceImpl {

    final AccountTypeRepository accountTypeRepository;
    final RedissonClient redissonClient;


    public boolean createBaseAccountType(CreateBaseAccountTypeDto createBaseAccountTypeDto) throws IOException, CustomException {
       validateAccountTypeDetails(createBaseAccountTypeDto);
       AccountType accountType= MapperUtils.mapClass(createBaseAccountTypeDto, AccountType.class);
       accountTypeRepository.save(accountType);
       saveInCache(accountType);
       return true;
    }


    public List<BaseAccountTypeResponseDto> getAllAccountTypes() {
        RMapCache<String, AccountType>  bankRMapCache = redissonClient.getMapCache(Constants.RedisKeys.ACCOUNT_TYPE);
        return accountTypeResponseDtos(new ArrayList<>(bankRMapCache.values()));
    }

    private void validateAccountTypeDetails(CreateBaseAccountTypeDto createBaseAccountTypeDto) throws CustomException {

        AccountType accountType =accountTypeRepository.findByAccountTypeName(createBaseAccountTypeDto.getAccountTypeName());
        if(accountType!=null){
            throw new CustomException("Account type details already exist", HttpStatus.BAD_REQUEST);

        }
    }

    public boolean syncAllAccountTypesToCache() {
        List<AccountType> accountTypelist = (List<AccountType>) accountTypeRepository.findAll();
        RMapCache<String, AccountType> accountTypeRMapCache = redissonClient.getMapCache(Constants.RedisKeys.ACCOUNT_TYPE);
        accountTypeRMapCache.clear();
        accountTypeRMapCache.putAll(accountTypelist.stream().collect(Collectors.toMap(AccountType::getAccountTypeName, Function.identity())));
        return true;
    }

    private void saveInCache(AccountType accountType) {
        RMapCache<String, AccountType> accountTypeRMapCache = redissonClient.getMapCache(Constants.RedisKeys.ACCOUNT_TYPE);
        accountTypeRMapCache.put(accountType.getAccountTypeName(), accountType);
    }

    private List<BaseAccountTypeResponseDto> accountTypeResponseDtos(List<AccountType> accountTypes) {
        return MapperUtils.mapList(accountTypes, BaseAccountTypeResponseDto.class);
    }

}
