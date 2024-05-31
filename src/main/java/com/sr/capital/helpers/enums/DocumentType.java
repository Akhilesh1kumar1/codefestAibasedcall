package com.sr.capital.helpers.enums;

public enum DocumentType {

    ADDRESS_PROOF("address_proof"),
    CURRENT_ADDRESS_PROOF("current_address_proof"),
    BUSINESS_PROOF("business_proof"),
    CERT_OF_INCORPORATION("certificate_of_incorporation"),
    ARTICLE_OF_ASSOCIATION("article_of_association"),
    MEMORANDUM_OF_ASSOCIATION("memorandum_of_association"),
    POA_GRADE_TO_TRANSACT("poa_grade_to_transact"),

    BUSINESS_ENTITY_PAN("pan_of_business_entity"),
    PARTNERSHIP_DEED("partnership_deed"),
    REGISTRATION_CERTIFICATE("registration_certificate"),

    PROOF_OF_BUSINESS("proof_of_business"),

    IDENTITY_PROOF("identity_proof");

    String type;

    DocumentType(String type){
        this.type =type;
    }
}
