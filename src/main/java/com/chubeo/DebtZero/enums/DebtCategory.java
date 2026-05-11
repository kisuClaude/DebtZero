package com.chubeo.DebtZero.enums;


public enum DebtCategory {
    BANK_LOAN("Vay ngân hàng", "REDUCING", "SAFE"),
    CREDIT_CARD("Thẻ tín dụng", "GRACE_PERIOD", "CAUTION"),
    BNPL("Mua trước trả sau", "FIXED", "CAUTION"),
    PERSONAL("Nợ cá nhân", "CUSTOM", "SAFE"),
    FINANCE_CO("Công ty tài chính", "FIXED", "HIGH"),
    BLACK_CREDIT("Tín dụng đen", "FIXED", "DANGER");

    final String displayName;
    final String interestType;
    final String riskLevel;

    DebtCategory(String displayName, String interestType, String riskLevel){
        this.displayName = displayName;
        this.interestType = interestType;
        this.riskLevel = riskLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getInterestType() {
        return interestType;
    }

    public String getRiskLevel() {
        return riskLevel;
    }
}
