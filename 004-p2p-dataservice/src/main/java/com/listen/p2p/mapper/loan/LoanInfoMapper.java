package com.listen.p2p.mapper.loan;

import com.listen.p2p.model.loan.LoanInfo;
import com.listen.p2p.model.vo.BidInfoExtUser;

import java.util.List;
import java.util.Map;

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /*
    * 获取历史平均年化收益率
    */
    Double selectHistoryAverageRate();

    List<LoanInfo> selectLoanInfoListByProductType(Map<String, Object> paramMap);

    /*
    * 查询产品总记录数
    */
    Long selectTotal(Map<String, Object> paramMap);


}