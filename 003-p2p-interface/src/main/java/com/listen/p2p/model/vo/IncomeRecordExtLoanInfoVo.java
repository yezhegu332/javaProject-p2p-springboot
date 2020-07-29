package com.listen.p2p.model.vo;

import com.listen.p2p.model.loan.IncomeRecord;
import com.listen.p2p.model.loan.LoanInfo;

/**
 * @Author: Listen
 * @Date: 2020/7/29
 */
public class IncomeRecordExtLoanInfoVo extends IncomeRecord {
    private LoanInfo loanInfo;

    public LoanInfo getLoanInfo() {
        return loanInfo;
    }

    public void setLoanInfo(LoanInfo loanInfo) {
        this.loanInfo = loanInfo;
    }

    @Override
    public String toString() {
        return "IncomeRecordExtLoanInfoVo{" +
                "loanInfo=" + loanInfo +
                '}';
    }
}
