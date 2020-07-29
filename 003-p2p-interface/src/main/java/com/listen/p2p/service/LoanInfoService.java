package com.listen.p2p.service;

import com.listen.p2p.model.vo.BidInfoExtUser;
import com.listen.p2p.model.vo.PaginationVo;
import com.listen.p2p.model.loan.LoanInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author: Listen
 * @Date: 2020/7/25
 */
public interface LoanInfoService {
    Double queryHistoryAverageRate();

    /*
    * 根据产品类型获取产品列表
    */
    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap);

    PaginationVo<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap);

    /*
    * 根据产品标识获取产品详情
    */
    LoanInfo queryLoanInfoById(Integer id);

}
