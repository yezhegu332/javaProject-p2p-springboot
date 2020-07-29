package com.listen.p2p.service;

import com.listen.p2p.model.vo.IncomeRecordExtLoanInfoVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: Listen
 * @Date: 2020/7/29
 */
public interface IncomeRecordService {

    List<IncomeRecordExtLoanInfoVo> queryIncomeRecordExtLoanInfoVoListByUid(Map<String, Object> paramMap);
}
