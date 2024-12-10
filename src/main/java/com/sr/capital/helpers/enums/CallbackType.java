package com.sr.capital.helpers.enums;

public enum CallbackType {

    USER_SIGN_UP("user_signup"),
    USER_LOG_IN("user_login"),
    USER_LOG_IN_CUM_SIGN_UP("user_log_in_cum_sign_up"),
    USER_EMAIL_VERIFICATION("user_email_verification"),
    RESET_PASSWORD("reset_password"),
    CRIF_VERIFICATION("crif_verification"),

    UNKNOWN_REQUEST("unknown_request");

    final String type;

    CallbackType(String type) {
        this.type = type;
    }
}
