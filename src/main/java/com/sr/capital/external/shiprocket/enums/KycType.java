package com.sr.capital.external.shiprocket.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum KycType {

    COMPANY(1, new KycDocumentType[] {
            KycDocumentType.PAN_UPLOAD,
            KycDocumentType.GST_UPLOAD,
            KycDocumentType.CIN_UPLOAD
    },"Private Limited"),
    INDIVIDUAL(2, new KycDocumentType[] {
            KycDocumentType.PAN_UPLOAD,
            KycDocumentType.LICENSE_UPLOAD,
            KycDocumentType.PASSPORT_UPLOAD
    },"One Person Company (OPC)"),
    PROPRIETORSHIP(3, new KycDocumentType[] {
            KycDocumentType.PAN_UPLOAD,
            KycDocumentType.LICENSE_UPLOAD,
            KycDocumentType.PASSPORT_UPLOAD,
            KycDocumentType.CIN_UPLOAD,
            KycDocumentType.GST_UPLOAD,
            KycDocumentType.UDYOG_AADHAR
    }, "Proprietorship"),
    PARTNERSHIP(4,new KycDocumentType[]{

    }, "Partnership"),

    PRIVATE_LIMITED(5,new KycDocumentType[]{

    }, "Private Limited"),
    PUBLIC_LIMITED(6,new KycDocumentType[]{

    }, "Public Limited"),
    OTHERS(7,new KycDocumentType[]{

    }, "Others"),
    LIMITED_LIABILITY_PARTNERSHIP(8,new KycDocumentType[]{}
            ,"Limited Liability Partnership");



    final int value;
    final KycDocumentType[] acceptedDocumentTypes;
    private final String clientType;

    static final Map<Integer, KycType> VALUE_MAP = new HashMap<Integer, KycType>();
    static {
        for (KycType type : values()) {
            VALUE_MAP.put(type.getValue(), type);
        }
    }

    public static KycType fromValue(int value) {
        return VALUE_MAP.get(value);
    }

    public String getClientType() {
        return clientType;
    }
}
