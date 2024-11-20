package com.sr.capital.helpers.enums;

public enum LoanVendorName {
    FLEXI("flexi");

    final String loanVendorName;

    LoanVendorName(String loanVendorName) {
        this.loanVendorName = loanVendorName;
    }

    public String getLoanVendorName() {
        return this.loanVendorName;
    }
}
