package com.sr.capital.service;

import com.sr.capital.dto.request.UserDetails;
import com.sr.capital.dto.response.UserProgressResponseDto;
import com.sr.capital.entity.primary.User;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.shiprocket.dto.response.ApiTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.InternalTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.ValidateMobileResponse;

public interface UserService {

    ApiTokenUserDetailsResponse getUserDetails(String token);

    UserDetails saveUserDetails(UserDetails userDetails) throws CustomException;

    InternalTokenUserDetailsResponse getUserDetailsUsingInternalToken(String token);

    boolean updateVerifyFlag(Long userId);

    User getCompanyDetails(Long srCompanyId);

    ValidateMobileResponse validateMobileNumber(String mobileNumber);

    UserProgressResponseDto getCompanyCompanyProgressState();

    UserDetails getCompanyDetailsWithoutEncryption(Long srCompanyId) ;

}
