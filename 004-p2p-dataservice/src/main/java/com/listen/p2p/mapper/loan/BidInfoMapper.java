package com.listen.p2p.mapper.loan;

import com.listen.p2p.model.loan.BidInfo;
import com.listen.p2p.model.vo.BidInfoExtUser;
import com.listen.p2p.model.vo.BidInfoLoan;

import java.util.List;
import java.util.Map;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    Integer selectCountOfBidMoney();

    List<BidInfoExtUser> selectRecentlyBidInfoByLoanId(Map<String, Object> paramMap);

    List<BidInfoLoan> selectRecentlyBidInfoListByUid(Map<String, Object> paramMap);
}