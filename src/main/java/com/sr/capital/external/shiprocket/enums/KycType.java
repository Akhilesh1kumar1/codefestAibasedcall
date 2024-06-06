package com.sr.capital.external.shiprocket.enums;

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
    }),
    INDIVIDUAL(2, new KycDocumentType[] {
            KycDocumentType.PAN_UPLOAD,
            KycDocumentType.LICENSE_UPLOAD,
            KycDocumentType.PASSPORT_UPLOAD
    }),
    PROPRIETORSHIP(3, new KycDocumentType[] {
            KycDocumentType.PAN_UPLOAD,
            KycDocumentType.LICENSE_UPLOAD,
            KycDocumentType.PASSPORT_UPLOAD,
            KycDocumentType.CIN_UPLOAD,
            KycDocumentType.GST_UPLOAD,
            KycDocumentType.UDYOG_AADHAR
    });

    final int value;
    final KycDocumentType[] acceptedDocumentTypes;

    public static KycType fromValue(int value) {
        for (KycType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }

}
