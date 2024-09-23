package com.sr.capital.offer.calculator.helpers;

public enum ParameterName {
    CURRENT_RATIO("Current Ratio"),
    QUICK_RATIO("Quick Ratio"),
    DEBT_EQUITY("Debt/Equity"),
    DEBT_CAPITAL("Debt/Capital"),
    CASH_RATIO("Cash Ratio"),
    RETURN_ON_ASSETS("Return on Assets"),
    INTEREST_COVERAGE_RATIO("Interest coverage ratio"),
    BAD_DEBTS_GROSS_REVENUE("Bad Debts / Gross Revenue"),
    NUMBER_OF_RECURRING_CLIENTS("Number of recurring clients"),
    BAD_LOANS("Bad loans"),
    CURRENT_OVERDUE("Current overdue"),
    NET_PROFIT_MARGIN("Net profit margin"),
    OPERATING_PROFIT_MARGIN("Operating profit margin"),
    AVG_MARGIN_PER_MONTH_MONTHLY_EMI_PLUS_15K("Avg. margin per month/Monthly EMI + 15K"),
    AVG_MONTHLY_INVOICE_VALUE("Avg. monthly invoice value"),
    NUMBER_OF_DIRECTORS("Number of directors"),
    ITR_FILING_ON_TIME("ITR filing on Time"),
    NUMBER_OF_SHIPMENT("Number of shipment"),
    IS_RED_FLAG("Is Red Flag"),
    AVG_BALANCE("Avg. Balance"),
    NO_OF_NEGATIVE_BALANCE("No of Negative Balance"),
    CURRENT_DPD("Current DPD"),
    PROFIT_IN_LAST_ONE_YEAR("Profit in Last One Year"),
    PRESENCE_OF_LONG_TERM_BORROWING("Presence of Long-Term Borrowing"),
    SPEND_ON_MARKETING_EXPENDITURE("Spend on Marketing Expenditure"),
    EBITDA("EBITDA"),
    DSCR("DSCR"),
    LEVERAGE("Leverage"),
    PRESENCE_ON_MULTIPLE_PLATFORM("Presence on Multiple Platform"),
    GROWTH_IN_SALES("Growth in Sales"),
    STEADY_REVENUE("Steady Revenue"),
    GROWTH_IN_SALES_COMPARED_TO_TWELVE_MONTHS("Growth in Sales Compared to Twelve Months"),
    INVENTORY_TURN_OVER_DATA("Inventory Turn Over Data"),
    PERSONAL_CIBIL_SCORE("Personal Cibil Score"),
    COMMERCIAL_CIBIL_SCORE("Commercial Cibil Score"),
    ITR_FILLED_ON_TIME("ITR Filled on Time"),
    ITR_SCORE("ITR Score"),
    NET_REVENUE("Net Revenue"),
    GST_RISK_CHECK("GST Risk Check"),
    COMPLIANCE_CHECK("Compliance Check"),
    LIFE_TIME_OF_COMPLIANCES("Life Time of Compliances"),
    RECURRING_BUSINESS("Recurring Business");


    private final String displayName;

    ParameterName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
