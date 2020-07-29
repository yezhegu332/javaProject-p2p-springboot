package com.listen.p2p.mapper.loan;

import com.listen.p2p.model.loan.IncomeRecord;
import com.listen.p2p.model.vo.IncomeRecordExtLoanInfoVo;

import java.util.List;
import java.util.Map;

public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

    List<IncomeRecordExtLoanInfoVo> selectIncomeRecordExtLoanInfoVoListByUid(Map<String, Object> paramMap);
}