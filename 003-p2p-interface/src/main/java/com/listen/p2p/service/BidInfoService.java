package com.listen.p2p.service;

import com.listen.p2p.model.loan.BidInfo;
import com.listen.p2p.model.vo.BidInfoExtUser;
import com.listen.p2p.model.vo.BidInfoLoan;

import java.util.List;
import java.util.Map;

/**
 * @Author: Listen
 * @Date: 2020/7/25
 */
public interface BidInfoService {
    Integer queryAllBidMoney();

    /*
     * 根据产品标识查询近10笔投资记录
     */
    List<BidInfoExtUser> queryRecentlyBidInfoByLoanId(Map<String, Object> paramMap);

    /*
    * 根据用户id查询近5笔最近投资
    */
    List<BidInfoLoan> queryRecentlyBidInfoListByUid(Map<String, Object> paramMap);
}
