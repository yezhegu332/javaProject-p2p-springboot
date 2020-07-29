package com.listen.p2p.service;

import com.listen.p2p.model.loan.RechargeRecord;

import java.util.List;
import java.util.Map;

/**
 * @Author: Listen
 * @Date: 2020/7/29
 */
public interface RechargeRecordService {
    List<RechargeRecord> queryRecentlyRechargeRecordListByUid(Map<String, Object> paramMap);
}
