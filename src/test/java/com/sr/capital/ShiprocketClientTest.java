package com.sr.capital;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.omunify.restutil.RestUtil;
import com.omunify.restutil.RestUtilProvider;
import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.client.ShiprocketClient;
import com.sr.capital.util.LoggerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.inject.Provider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.omunify.restutil.RestUtilProvider.getInstance;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;


/*@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yaml")*/
public class ShiprocketClientTest {

    LoggerUtil loggerUtil = LoggerUtil.getLogger(ShiprocketClientTest.class);
    @InjectMocks
    private ShiprocketClient shiprocketClient;

    @Mock
    private AppProperties appProperties;

    @Mock
    private RestUtil restUtil;

    @Mock
    Provider<RestUtil> restUtilProvider;

    @InjectMocks
    RestUtilProvider restUtilProviderWrapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(getInstance()).thenReturn((RestUtil) restUtilProvider);
        when(restUtilProvider.get()).thenReturn(restUtil);
        when(restUtil.withHeaders(anyMap())).thenReturn(restUtil);
        when(appProperties.getShiprocketAuthBaseUrl()).thenReturn("https://sr-auth-qa-1.kartrocket.com");
        when(appProperties.getShiprocketValidateTokenEndPoint()).thenReturn("/authorize/user");


    }

    @Test
    public void testCallThirdPartyApi() throws UnirestException, CustomException {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3NyLXNlcnZpY2VhYmlsaXR5LW11bHRpY2hhbm5lbC1hcGktcWEtMS5rYXJ0cm9ja2V0LmNvbS92MS9hdXRoL2xvZ2luIiwiaWF0IjoxNzE2NTQzMjUwLCJleHAiOjE3MTc0MDcyNTAsIm5iZiI6MTcxNjU0MzI1MCwianRpIjoiNU1UNDJJU3Z1QVZ3RHZRbiIsInN1YiI6NzQ2LCJwcnYiOiIwNWJiNjYwZjY3Y2FjNzQ1ZjdiM2RhMWVlZjE5NzE5NWEyMTFlNmQ5IiwiY2lkIjo3MDQsImVtYWlsIjoibmVoYS5yYWpwdXQrc3RhZ2VAc2hpcHJvY2tldC5pbiJ9.hTqQ0LgSSULiXMaVmeauIF7EmKaPH8v6moqr3vV6UEA";

        shiprocketClient.validateToken(token);
    }
}
