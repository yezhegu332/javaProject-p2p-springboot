package com.listen.p2p.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.listen.p2p.mapper.loan.RechargeRecordMapper;
import com.listen.p2p.model.loan.RechargeRecord;
import com.listen.p2p.service.RechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: Listen
 * @Date: 2020/7/29
 */
@Component
@Service(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 15000)
public class RechargeRecordServiceImpl implements RechargeRecordService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
    @Override
    public List<RechargeRecord> queryRecentlyRechargeRecordListByUid(Map<String, Object> paramMap) {
        return rechargeRecordMapper.selectRecentlyRechargeRecordListByUid(paramMap);
    }
}
