package com.sr.capital.service;

import com.sr.capital.dto.request.UserDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.shiprocket.dto.response.ApiTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.InternalTokenUserDetailsResponse;

public interface UserService {

    ApiTokenUserDetailsResponse getUserDetails(String token);

    UserDetails saveUserDetails(UserDetails userDetails) throws CustomException;

    InternalTokenUserDetailsResponse getUserDetailsUsingInternalToken(String token);
}
