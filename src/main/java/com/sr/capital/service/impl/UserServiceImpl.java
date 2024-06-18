package com.sr.capital.service.impl;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.UserDetails;
import com.sr.capital.entity.primary.User;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.shiprocket.client.ShiprocketClient;
import com.sr.capital.external.shiprocket.dto.response.ApiTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.InternalTokenUserDetailsResponse;
import com.sr.capital.repository.primary.UserRepository;
import com.sr.capital.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

   final ShiprocketClient shiprocketClient;

   final UserRepository userRepository;

    final AES256 aes256;

    @Override
    public ApiTokenUserDetailsResponse getUserDetails(String token) {
        return shiprocketClient.getUserDetailsUsingApiToken(token);
    }

    @Override
    public UserDetails saveUserDetails(UserDetails userDetails) throws CustomException {

        validateUserDetails(userDetails);
        encryptData(userDetails);
        User user =userRepository.findBySrUserId(Long.valueOf(userDetails.getUserId()));
       if(user==null){
           user =User.mapUser(userDetails);
       }else {
           throw new CustomException("User already exist",HttpStatus.BAD_REQUEST);
       }

        userRepository.save(user);
        return UserDetails.mapUser(user);
    }

    private void encryptData(UserDetails userDetails) {
        userDetails.setEmail(aes256.encrypt(userDetails.getEmail()));
        userDetails.setMobileNumber(aes256.encrypt(userDetails.getMobileNumber()));
        userDetails.setFirstName(aes256.encrypt(userDetails.getFirstName()));
        userDetails.setLastName(aes256.encrypt(userDetails.getLastName()));
        userDetails.setMiddleName(aes256.encrypt(userDetails.getMiddleName()));
    }

    @Override
    public InternalTokenUserDetailsResponse getUserDetailsUsingInternalToken(String token) {
        InternalTokenUserDetailsResponse response = shiprocketClient.getUserDetailsUsingInternalToken(token);

        if(response.getUserId()!=null){
            User user = userRepository.findBySrUserId(Long.valueOf(response.getUserId()));
            if(user!=null){
                response.setIsAccepted(user.getIsAccepted());
            }
        }
        return response;
    }

    private void validateUserDetails(UserDetails userDetails) throws CustomException {

        if(RequestData.getUserId().longValue()!=Long.valueOf(userDetails.getUserId())){
            throw new CustomException("Invalid UserId ", HttpStatus.BAD_REQUEST);
        }

        if(!RequestData.getTenantId().equalsIgnoreCase(String.valueOf(userDetails.getCompanyId()))){
            throw new CustomException("Invalid CompanyId ", HttpStatus.BAD_REQUEST);
        }
    }
}
