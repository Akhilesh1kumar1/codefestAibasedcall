package com.sr.capital.helpers.enums;

public enum CommunicationTemplateNames {

    LEAD_REJECTED("rejection-email-template.html"),
    REMINDER_EMAIL("reminder-email-template"),
    EXCEPTION_ALERT_EMAIL("exception-alert-email-template"),
    PROCESSING_STAGE("processing-stage-email-template"),
    OFFER_GENERATION("offer-generation-email-template"),
    LOAN_DISBURSED("loan-disbursed"),
    DOCUMENT_PENDING("document-pending-template");

    private String templateName;
    CommunicationTemplateNames(String s) {
        this.templateName =s;
    }

    public String getTemplateName() {
        return templateName;
    }
}
