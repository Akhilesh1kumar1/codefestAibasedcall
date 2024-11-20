package com.sr.capital.helpers.enums;

public enum CommunicationTemplateNames {

    LEAD_REJECTED("rejection-email-template.html"),
    REMINDER_EMAIL("reminder-email-template"),
    PROCESSING_STAGE("processing-stage-email-template"),
    OFFER_GENERATION("offer-generation-email-template"),
    LOAN_DISBURSED("loan-disbursed");

    private String templateName;
    CommunicationTemplateNames(String s) {
        this.templateName =s;
    }

    public String getTemplateName() {
        return templateName;
    }
}
