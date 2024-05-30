package com.sr.capital.helpers.enums;

public enum CommunicationChannels {
    EMAIL("email"),
    SMS("sms"),
    WHATSAPP("whatsapp"),
    SMS_WHATSAPP("sms_whatsapp");

    final String type;

    CommunicationChannels(String type) { this.type = type; }

    public String getType() {
        return type;
    }
}
