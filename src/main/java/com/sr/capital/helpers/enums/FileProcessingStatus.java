package com.sr.capital.helpers.enums;

public enum FileProcessingStatus {

    ACKNOWLEDGEMENT_PENDING("ACKNOWLEDGEMENT PENDING"),
    ACKNOWLEDGEMENT_DONE("ACKNOWLEDGEMENT DONE"),
    PUSHED_TO_PROCESSING_QUEUE("PUSHED TO PROCESSING QUEUE");

    String status;
    FileProcessingStatus(String status) {
        this.status = status;
    }
}
