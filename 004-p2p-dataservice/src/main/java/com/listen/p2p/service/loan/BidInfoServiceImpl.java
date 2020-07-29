package com.listen.p2p.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.listen.constants.Constants;
import com.listen.p2p.mapper.loan.BidInfoMapper;
import com.listen.p2p.model.loan.BidInfo;
import com.listen.p2p.model.vo.BidInfoExtUser;
import com.listen.p2p.model.vo.BidInfoLoan;
import com.listen.p2p.service.BidInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author: Listen
 * @Date: 2020/7/25
 */
@Component
@Service(interfaceClass =BidInfoService.class,version = "1.0.0",timeout = 15000)
public class BidInfoServiceImpl implements BidInfoService {
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public Integer queryAllBidMoney() {
        BoundValueOperations<Object, Object> boundValueOps = redisTemplate.boundValueOps(Constants.ALL_BID_MONEY);
        Integer allBidMoney = (Integer) boundValueOps.get();
        if(!ObjectUtils.isNotEmpty(allBidMoney)){
            synchronized (this){
                allBidMoney = (Integer) boundValueOps.get();
                if(!ObjectUtils.isNotEmpty(allBidMoney)){
                    allBidMoney = bidInfoMapper.selectCountOfBidMoney();
                    boundValueOps.set(allBidMoney,25, TimeUnit.SECONDS);
                }else{
                    allBidMoney = (Integer) boundValueOps.get();
                }
            }
        }
        return allBidMoney;
    }

    @Override
    public List<BidInfoExtUser> queryRecentlyBidInfoByLoanId(Map<String, Object> paramMap) {
        return bidInfoMapper.selectRecentlyBidInfoByLoanId(paramMap);
    }

    @Override
    public List<BidInfoLoan> queryRecentlyBidInfoListByUid(Map<String, Object> paramMap) {
        return bidInfoMapper.selectRecentlyBidInfoListByUid(paramMap);
    }

}
