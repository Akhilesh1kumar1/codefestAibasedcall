package com.sr.capital.util;

//import com.omunify.kafka.MsgMessage;
import com.omunify.kafka.MsgMessage;
import com.sr.capital.exception.custom.CustomException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.sr.capital.helpers.constants.Constants.Headers.TENANT_HEADER;

/**
 * @author vikas.sinwar created on 15/09/24
 */
public class TenantUtils {

    private TenantUtils() {
    }

    public static String fetchTenantId(MsgMessage message) throws CustomException {
        Map<String, String> messageHeaders = message.getHeaders();
        if (MapUtils.isNotEmpty(messageHeaders) && StringUtils.isNotEmpty(messageHeaders.get(TENANT_HEADER))) {
            return messageHeaders.get(TENANT_HEADER);
        }
        throw new CustomException("Tenant not found");
    }
}
