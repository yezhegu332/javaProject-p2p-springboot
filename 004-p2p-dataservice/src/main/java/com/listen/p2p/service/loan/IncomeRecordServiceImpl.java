package com.listen.p2p.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.listen.p2p.mapper.loan.IncomeRecordMapper;
import com.listen.p2p.model.vo.IncomeRecordExtLoanInfoVo;
import com.listen.p2p.service.IncomeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: Listen
 * @Date: 2020/7/29
 */
@Component
@Service(interfaceClass = IncomeRecordService.class,version = "1.0.0",timeout = 15000)
public class IncomeRecordServiceImpl implements IncomeRecordService {
    @Autowired
    private IncomeRecordMapper incomeRecordMapper;
    @Override
    public List<IncomeRecordExtLoanInfoVo> queryIncomeRecordExtLoanInfoVoListByUid(Map<String, Object> paramMap) {
        return incomeRecordMapper.selectIncomeRecordExtLoanInfoVoListByUid(paramMap);
    }
}
